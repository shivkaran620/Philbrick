package com.philbrick.ui.screen.account

import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.philbrick.R
import com.philbrick.data.APIResult
import com.philbrick.data.local.PreferenceManager
import com.philbrick.databinding.ActivityOtpBinding
import com.philbrick.ui.customview.BaseActivity
import com.philbrick.ui.screen.product.SideMenuActivity
import com.philbrick.util.constant.Extra
import com.philbrick.util.extension.hideKeyboard
import com.philbrick.util.extension.setFullScreen
import com.philbrick.util.extension.setWindowBackground
import com.philbrick.util.extension.showKeyBoard
import com.philbrick.util.helper.Helper

class OtpActivity : BaseActivity(),View.OnClickListener {

    private lateinit var otpBinding: ActivityOtpBinding
    lateinit var otp: String
    private var otpTextView: OtpTextView? = null
    private var mobileNo : String = ""
    private lateinit var accountViewModel : AccountViewModel
    private lateinit var prefManager : PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowBackground()
        setFullScreen()
        otpBinding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(otpBinding.root)
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        prefManager = PreferenceManager(this)

        intent.let {
            if (intent!=null){
                if (intent.hasExtra(Extra.PHONE)){
                    mobileNo = intent.getStringExtra(Extra.PHONE)?:""
                }
            }
        }

        otpBinding.apply {
            showKeyBoard(otpView)
            btnVerify.setOnClickListener {
                startActivity(Intent(this@OtpActivity, SideMenuActivity::class.java))
            }
            tvNumber.text = resources.getString(R.string.otp_sent_number_)+ " "+mobileNo
            tbOtp.imgBack.visibility = View.VISIBLE
            tbOtp.tvTitle.visibility = View.GONE
            tbOtp.view.visibility = View.GONE
            txtResend.setOnClickListener {
                mobileVerificationApi();
            }
        }
        addClickListener()

    }

    private fun addClickListener(){
        with(otpBinding){
            tbOtp.imgBack.setOnClickListener(this@OtpActivity)
            btnVerify.setOnClickListener(this@OtpActivity)

            otpTextView = otpView
            otpTextView?.requestFocusOTP()

            otpTextView?.otpListener = object : OTPListener {
                override fun onInteractionListener() {
                }
                override fun onOTPComplete(otp: String) {
                    hideKeyboard()
                }
            }
        }
    }


    private fun checkValidation() : Boolean {
        var isValid: Boolean = true
        otp = otpBinding.otpView.otp.toString()

        if (otp.isEmpty()){
            showError(resources.getString(R.string.err_otp_required))
            isValid = false
        }
        return isValid
    }

    private fun verifyOtpApi() {
        accountViewModel.verifyOtpApi(otp,mobileNo) {
            when (it) {
                is APIResult.Success -> {
                    dismissDialog()
                    if (prefManager.userFirstName.isNotEmpty()){
                        baseHandler.postDelayed({
                            startActivity(Intent(this@OtpActivity,SideMenuActivity::class.java))
                            finish()
                        },1000)
                    }
                    else{
                        baseHandler.postDelayed({
                            startActivity(Intent(this@OtpActivity,ProfileActivity::class.java)
                                .putExtra(Extra.IS_FROM_OTP,true)
                                .putExtra(Extra.PHONE,mobileNo))
                            finish()
                        },1000)
                    }

                }
                is APIResult.Failure -> {
                    showError( it.message?:"")
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
            Helper.customSnackBar(otpBinding.clRoot, message, this, isSuccess)
        }
    }

    override fun onClick(view: View?) {
        otpBinding.apply {
            when(view){
                tbOtp.imgBack -> {
                    onBackPressed()
                }
                btnVerify -> {
                    hideKeyboard()
                    if (checkValidation()){
                        verifyOtpApi()
                    }
                }
            }
        }
    }

    private fun mobileVerificationApi() {
        accountViewModel.mobileVerificationApi(mobileNo) {
            when (it) {
                is APIResult.Success -> {
                    dismissDialog()
                    //finish()
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
}