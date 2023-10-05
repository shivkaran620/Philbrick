package com.philbrick.util.constant

import android.graphics.Bitmap
import com.philbrick.BuildConfig

/**
 * Created by Authentic Android Developer on 14-07-2022.
 */
object Constant {
    const val LINK_PRIVACY_AND_POLICY = "https://www.google.com/"
    const val LINK_TERMS_AND_USE = "https://www.google.com/"
    const val LINK_CONTACT_US = "https://philbrickindia.com/contact-us/"
    const val LINK_ABOUTS_US = "https://philbrickindia.com/company/"
    const val INTERNAL_VERSION = BuildConfig.VERSION_CODE.toString()
    const val EXTERNAL_VERSION = BuildConfig.VERSION_NAME
    const val DEVICE_TYPE = "android"
    val EMAIL_RE = "^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-.]+)\\.([a-zA-Z]{2,5})\$".toRegex()
    // compressed image
    const val COMPRESSED_IMG_RESOLUTION_WIDTH = 1220
    const val COMPRESSED_IMG_RESOLUTION_HEIGHT = 720
    const val COMPRESSED_IMG_QUALITY = 80
    val COMPRESSED_IMG_FORMAT = Bitmap.CompressFormat.JPEG
    const val COMPRESSED_IMG_SIZE = 1_097_152L
}