package com.philbrick.ui.customview

import android.app.ProgressDialog
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.philbrick.R

open class BaseActivity : AppCompatActivity() {
    //handler
    var progressDialog: ProgressDialog? = null

    val baseHandler = Handler(Looper.getMainLooper())
    var runnable = Runnable { /*Set runnable and use its handler in entire project*/ }

    fun showDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
            progressDialog?.setProgressStyle(R.style.Progressbar)
            progressDialog?.setMessage(getString(R.string.please_wait))
            progressDialog?.show()
            progressDialog?.setCancelable(false)
        }
    }

    fun dismissDialog() {
        if (progressDialog != null && progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
            progressDialog = null
        }
    }


}