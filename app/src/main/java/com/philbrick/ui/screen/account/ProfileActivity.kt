package com.philbrick.ui.screen.account

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.philbrick.R
import com.philbrick.data.APIResult
import com.philbrick.data.local.PreferenceManager
import com.philbrick.databinding.ActivityProfileBinding
import com.philbrick.ui.customview.BaseActivity
import com.philbrick.ui.screen.product.SideMenuActivity
import com.philbrick.util.constant.Code
import com.philbrick.util.constant.Constant
import com.philbrick.util.constant.Extra
import com.philbrick.util.extension.setFullScreen
import com.philbrick.util.extension.setWindowBackground
import com.philbrick.util.helper.Helper
import com.philbrick.utility.FileUtils
import com.philbrick.utility.Glide4Engine
/*import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy*/
import java.io.File


class ProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var profileBinding: ActivityProfileBinding

    private lateinit var accountViewModel: AccountViewModel
    private lateinit var pref: PreferenceManager
    var isFromOTP: Boolean = false
    var path: String = ""
    var uri: Uri? = null
    var mobileNo: String = ""
    private lateinit var prefManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowBackground()
        setFullScreen()
        pref = PreferenceManager(this)
        profileBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(profileBinding.root)
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        prefManager = PreferenceManager(this)

        profileBinding.apply {
            tbProfile.tvTitle.text = resources.getString(R.string.menu_profile)
        }

        intent.let {
            if (intent != null) {
                if (intent.hasExtra(Extra.IS_FROM_OTP)) {
                    isFromOTP = intent.getBooleanExtra(Extra.IS_FROM_OTP, false)
                }
                if (intent.hasExtra(Extra.PHONE)) {
                    if (intent.getStringExtra(Extra.PHONE) != null) {
                        mobileNo = intent.getStringExtra(Extra.PHONE).toString()
                    } else {
                        mobileNo = pref.userMobile
                    }

                    profileBinding.edtPhoneNumber.setText(mobileNo)
                }
                else
                {
                    mobileNo = pref.userMobile
                    profileBinding.edtPhoneNumber.setText(mobileNo)
                }
                Log.e("phone", intent.getStringExtra(Extra.PHONE).toString())
                Log.e("phone", pref.userMobile)
            }
        }
        addClickListener()

        if (prefManager.userFirstName.isNotEmpty()) {
            getProfile()
        }

    }

    private fun setPreData() {
        profileBinding.apply {
            edtFirstName.setText(accountViewModel.user.firstName ?: "")
            edtLastName.setText(accountViewModel.user.lastName ?: "")
            edtAddress.setText(accountViewModel.user.address ?: "")
            edtEmailAddress.setText(accountViewModel.user.emailAddress ?: "")
            edtPhoneNumber.setText(accountViewModel.user.phoneNo ?: "")
            edtCompanyName.setText(accountViewModel.user.companyName ?: "")
            pb.visibility = View.VISIBLE
            ivProfile.visibility = View.INVISIBLE
            edtPhoneNumber.isEnabled = false

            if (accountViewModel.user.photo.isNotEmpty()) {
                ivProfile.load(accountViewModel.user.photo) {
                    placeholder(R.drawable.ic_user_icon)
                    transformations(CircleCropTransformation())
                    scale(Scale.FILL)
                    listener(
                        onSuccess = { imageRequest: ImageRequest, result ->
                            ivProfile.visibility = View.VISIBLE
                            pb.visibility = View.GONE
                        },
                        onError = { imageRequest: ImageRequest, error ->
                            placeholder(R.drawable.ic_user_icon)
                            ivProfile.visibility = View.VISIBLE
                            pb.visibility = View.GONE
                        },
                        onStart = {
                            ivProfile.visibility = View.INVISIBLE
                        })
                }
            } else {
                ivProfile.setImageResource(R.drawable.ic_user_icon)
                pb.visibility = View.GONE
            }
        }
    }

    private fun addClickListener() {
        with(profileBinding) {
            btnSave.setOnClickListener(this@ProfileActivity)
            tbProfile.imgBack.setOnClickListener(this@ProfileActivity)
            ivProfile.setOnClickListener(this@ProfileActivity)
            cvPickImage.setOnClickListener(this@ProfileActivity)
        }
    }

    private fun checkValidation(): Boolean {
        var isValid: Boolean = true

        with(profileBinding) {
            accountViewModel.user.firstName = edtFirstName.text.toString().trim()
            accountViewModel.user.lastName = edtLastName.text.toString().trim()
            accountViewModel.user.phoneNo = edtPhoneNumber.text.toString().trim()
            accountViewModel.user.emailAddress = edtEmailAddress.text.toString().trim()
            accountViewModel.user.companyName = edtCompanyName.text.toString().trim()
            accountViewModel.user.address = edtAddress.text.toString().trim()

            when {
                accountViewModel.user.firstName!!.isEmpty() -> {
                    edtFirstName.error = resources.getString(R.string.err_first_name_required)
                    isValid = false
                }
            }
            when {
                accountViewModel.user.lastName!!.isEmpty() -> {
                    edtLastName.error = resources.getString(R.string.err_last_name_required)
                    isValid = false
                }
            }

            if (accountViewModel.user.emailAddress.isNotEmpty()) {
                when {
                    accountViewModel.user.emailAddress!!.length < resources.getInteger(R.integer.min_email_character_length) || accountViewModel.user.emailAddress!!.length > resources.getInteger(
                        R.integer.max_email_character_length
                    ) -> {
                        edtEmailAddress.error = getString(R.string.err_email_length)
                        isValid = false
                    }
                    !accountViewModel.user.emailAddress!!.matches(Constant.EMAIL_RE) -> {
                        edtEmailAddress.error = getString(R.string.err_invalid_email_address)
                        isValid = false
                    }
                }
            }


            when {
                accountViewModel.user.phoneNo!!.isEmpty() -> {
                    edtPhoneNumber.error = resources.getString(R.string.err_phone_number_required)
                    isValid = false
                }
            }

            when {
                accountViewModel.user.companyName!!.isEmpty() -> {
                    edtCompanyName.error = resources.getString(R.string.err_company_name_required)
                    isValid = false
                }
            }

            /*  when {
                  accountViewModel.user.address!!.isEmpty() -> {
                      edtAddress.error = resources.getString(R.string.err_address_required)
                      isValid = false
                  }
              }*/
        }

//        if (isValid) {
//            if (path.isEmpty() && accountViewModel.user.photo?.isEmpty() == true) {
//                showError(resources.getString(R.string.err_photo_required))
//                isValid = false
//            }
//        }

        return isValid
    }

    override fun onClick(view: View?) {
        with(profileBinding) {
            when (view) {
                tbProfile.imgBack -> {
                    onBackPressed()
                }
                btnSave -> {
                    if (checkValidation()) {
                        editProfile()
                    }
                }
                ivProfile, cvPickImage -> {
                    selectPhoto()
                }
            }
        }
    }



    private fun selectPhoto() {
        if (checkPermission()) {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, Code.RequestCode.REQ_CHOOSE_IMAGE_GALLERY)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    Code.RequestCode.REQUEST_CODE_STORAGE
                )
            }
        }
    }

    /**
     * checked storage permission its given or not default android method.
     */
    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * for the handle request permission result
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Code.RequestCode.REQUEST_CODE_STORAGE -> {
                if (grantResults.size == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    //startMatisseActivityForImages()
                    val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                    startActivityForResult(gallery, Code.RequestCode.REQ_CHOOSE_IMAGE_GALLERY)
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                Code.RequestCode.REQ_CHOOSE_IMAGE -> {
                    if (data != null) {
                       /* val uriArrayList = Matisse.obtainResult(data)
                        if (uriArrayList.size != 0) {
                            val uri = Uri.fromFile(
                                File(
                                    FileUtils.getFilePathForURI(
                                        this,
                                        uriArrayList[0]
                                    )
                                )
                            )
                            profileBinding.ivProfile.load(uri) {
                                transformations(CircleCropTransformation())
                            }
                            // accountViewModel.user.photo = uri.path?:""
                            path = uri.path ?: ""

                            Log.d("path", path)

                        }*/
                    }
                }
                Code.RequestCode.REQ_CHOOSE_IMAGE_GALLERY -> {
                    val uri = Uri.fromFile(File(FileUtils.getFilePathForURI(this, data?.data)))
                    path = uri?.path ?: ""
                    profileBinding.ivProfile.load(uri) {
                        transformations(CircleCropTransformation())
                    }
                }
            }
        }
    }

    private fun startMatisseActivityForImages() {
      /*  Matisse.from(this)
            .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.BMP, MimeType.WEBP))
            .capture(true)
            .maxSelectable(1)
            .showSingleMediaType(true)
            .captureStrategy(CaptureStrategy(true, "com.philbrick.fileprovider"))
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            .imageEngine(Glide4Engine())
            .theme(com.zhihu.matisse.R.style.Matisse_Zhihu)
            .forResult(Code.RequestCode.REQ_CHOOSE_IMAGE)*/
    }


    private fun editProfile() {
        accountViewModel.editProfileApi(accountViewModel.user, path) {
            when (it) {
                is APIResult.Success -> {
                    PreferenceManager(this).userFirstName =
                        profileBinding.edtFirstName.text.toString().trim()
                    if (isFromOTP) {
                        startActivity(Intent(this, SideMenuActivity::class.java))
                        finish()
                    } else {
                        finish()
                    }
                    dismissDialog()
                }
                is APIResult.Failure -> {
                    dismissDialog()
                }
                APIResult.InProgress -> {
                    showDialog()
                }
            }
        }
    }

    private fun getProfile() {
        accountViewModel.getProfileAPI {
            when (it) {
                is APIResult.Success -> {
                    setPreData()
                    dismissDialog()
                }
                is APIResult.Failure -> {
                    showError(it.message ?: "")
                    dismissDialog()
                }
                APIResult.InProgress -> {
                    showDialog()
                }
            }
        }
    }

    private fun showError(message: String, isSuccess: Boolean = false) {
        if (message.isNotEmpty()) {
            Helper.customSnackBar(profileBinding.clRoot, message, this, isSuccess)
        }
    }
}