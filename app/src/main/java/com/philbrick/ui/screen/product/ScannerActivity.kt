package com.philbrick.ui.screen.product

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.philbrick.R
import com.philbrick.databinding.ActivityScannerBinding
import com.philbrick.ui.customview.BaseActivity
import com.philbrick.util.constant.Extra
import com.philbrick.util.extension.setFullScreen
import com.philbrick.util.extension.setWindowBackground
import com.philbrick.util.helper.Helper

class ScannerActivity : BaseActivity(),View.OnClickListener {

    private lateinit var qrScanBinding: ActivityScannerBinding
    private lateinit var codeScanner: CodeScanner
    private var isFlashAvailable: Boolean = true
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowBackground()
        setFullScreen()
        qrScanBinding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(qrScanBinding.root)
        init()
    }

    private fun init() {
        codeScanner = CodeScanner(this, qrScanBinding.scrView)
        qrScanBinding.tvTitle.text = resources.getString(R.string.scanner)
        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,

        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not
        isFlashAvailable = applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            cameraId = cameraManager.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        addClickListener()
    }

    /**
     * added click listener.
     */
    private fun addClickListener() {
        qrScanBinding.apply {
            imgBack.setOnClickListener(this@ScannerActivity)
        }
        codeScanner.decodeCallback = DecodeCallback {
            this.runOnUiThread {
                val list = it.text.trim().split("\r")

                Log.d("token1111",list.toString())
                if (it.text.trim().isNotEmpty()) {
                    showError(it.text.trim(),true)
                    startActivity(Intent(this,ProductDetailsActivity::class.java)
                        .putExtra(Extra.PRODUCT_ID,list.toString()))
                    finish()
                }
            }
        }
        codeScanner.errorCallback = ErrorCallback {
            // or ErrorCallback.SUPPRESS
            this.runOnUiThread {
                // Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                showError(it.message?:"")
            }
        }
    }

    override fun onClick(view: View?) {
        qrScanBinding.apply {
            when (view) {
                imgBack -> {
                    onBackPressed()
                }
                scrView -> {

                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }


    private fun showError(message: String, isSuccess: Boolean = false) {
        if (message.isNotEmpty()) {
            Helper.customSnackBar(qrScanBinding.clRoot, message, this, isSuccess)
        }
    }

}