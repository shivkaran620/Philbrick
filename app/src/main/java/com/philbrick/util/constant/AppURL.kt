package com.philbrick.util.constant

object AppURL {

    object Web {
        const val BASE_URL = "https://philbrickindia.com/product_scanner"
    }

    object API {
        const val BASE_URL = Web.BASE_URL + "/Philbrick_apis/"
        const val MOBILE_VERIFICATION = "app_login"
        const val OTP_VERIFY = "chk_otp"
        const val GET_PROFILE = "get_profile"
        const val GET_BANNER = "bannerlist"
        const val GET_PRODUCT_DETAIL = "productdetails"
        const val UPDATE_PROFILE = "profile_update"
    }
}