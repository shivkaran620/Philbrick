package com.philbrick.util.helper

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.philbrick.R

object Helper {
    /*------------------------------------------*/


    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo

            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }

        return false
    }

    fun customSnackBar(
        view: View,
        message: String,
        context: Context,
        isSuccess: Boolean? = null,
    ) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)

        val tvSnackBar =
            snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snackBar.view.setPadding(0, 0, 0, 0)
        tvSnackBar.maxLines = 3

        tvSnackBar.setTextColor(ContextCompat.getColor(context, R.color.white))
        snackBar.setBackgroundTint(
            ContextCompat.getColor(
                context,
                when (isSuccess) {
                    true -> R.color.snackBar_green
                    false -> R.color.snackBar_red
                    else -> R.color.black
                }
            )
        )
        snackBar.show()
    }




}