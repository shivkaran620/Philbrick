package com.philbrick.util.extension

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.philbrick.R

fun Activity.setFullScreen() {
    this.window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            statusBarColor = Color.TRANSPARENT
        }
    }
}

fun Activity.setWindowBackground() {
    this.window.apply {
        setBackgroundDrawableResource(R.drawable.img_window_bg)
    }
}

/**
 * Show soft input keyboard
 */
fun Activity.showKeyBoard(view: View?) {
    if (view!=null)
    {
        view.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        imm.restartInput(view)
    }else
    {
        hideKeyboard()
    }
}

/**
 * Hide soft input keyboard
 */
fun Activity.hideKeyboard() {
    val view = window.decorView.rootView
    val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}