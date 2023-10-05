package com.philbrick.util.extension

import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.philbrick.R


fun Fragment.setFullScreen() {
    val window = requireActivity().window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.primary)
}

fun Fragment.setWindowBackground() {
    val window = requireActivity().window
    window.apply {
        setBackgroundDrawableResource(R.drawable.img_window_bg)
    }
}
