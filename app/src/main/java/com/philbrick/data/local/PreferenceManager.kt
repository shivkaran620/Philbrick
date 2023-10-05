package com.philbrick.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(val context : Context) {
    private val pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    init {
        editor.apply()
    }

    companion object {
        private const val PREF_NAME = "Philbrick"

        private object Key {
            const val ACCESS_TOKEN = "accessToken"
            const val USER_ID = "userId"
            const val USER_MODEL = "userModel"
            const val USER_FIRST_NAME = "firstName"
            const val USER_MOBILE_NO = "mobileno"
        }
    }

    var userModel: String
        get() = pref.getString(Key.USER_MODEL,"")?:""
        set(userModel) {
            editor.putString(Key.USER_MODEL, userModel)
            editor.commit()
        }

    var userFirstName: String
        get() = pref.getString(Key.USER_FIRST_NAME,"")?:""
        set(userFirstName) {
            editor.putString(Key.USER_FIRST_NAME, userFirstName)
            editor.commit()
        }

    var accessToken: String
        get() = pref.getString(Key.ACCESS_TOKEN,"")?:""
        set(accessToken) {
            editor.putString(Key.ACCESS_TOKEN, accessToken)
            editor.commit()
        }

    var userId: String
        get() = pref.getString(Key.USER_ID,"")?:""
        set(userId) {
            editor.putString(Key.USER_ID, userId)
            editor.commit()
        }
    var userMobile: String
        get() = pref.getString(Key.USER_MOBILE_NO,"")?:""
        set(mobileNo) {
            editor.putString(Key.USER_MOBILE_NO, mobileNo)
            editor.commit()
        }

    fun logOut(){
        editor.clear()
        editor.commit()
    }

}