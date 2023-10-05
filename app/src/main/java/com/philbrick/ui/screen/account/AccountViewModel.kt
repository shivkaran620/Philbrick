package com.philbrick.ui.screen.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.philbrick.data.APIResult
import com.philbrick.data.entity.User
import com.philbrick.data.local.PreferenceManager
import com.philbrick.data.repository.AccountRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.Main
    private val scope: CoroutineScope = CoroutineScope(coroutineContext)
    private val accountRepo = AccountRepository(application)
    private var pref = PreferenceManager(application)
    var user = User()
    fun isUserLoggedIn() = pref.accessToken.isNotEmpty() && pref.userFirstName.isNotEmpty()

    //private val prefRepo = PreferenceRepository(application)

    /**
     * this method is used for MobileVerification Api
     */
    fun mobileVerificationApi(mobileNumber: String, listener: (APIResult<String>) -> Unit) {
        scope.launch {
            accountRepo.mobileVerificationApi(mobileNumber) { it ->
                when (it) {
                    is APIResult.Success -> {
                        listener(it)
                    }
                    is APIResult.Failure -> {
                        listener(it)
                    }
                    APIResult.InProgress -> {
                        listener(APIResult.InProgress)
                    }
                }
            }
        }
    }

    /**
     * this method is used for called verify otp Api
     */
    fun verifyOtpApi(otp: String, mobileNo: String, listener: (APIResult<String>) -> Unit) {
        scope.launch {
            accountRepo.verifyOtpApi(otp, mobileNo) { it ->
                when (it) {
                    is APIResult.Success -> {
                        pref.accessToken = it.data.token ?: ""
                        listener(APIResult.Success("", ""))
                    }
                    is APIResult.Failure -> {
                        listener(it)
                    }
                    APIResult.InProgress -> {
                        listener(APIResult.InProgress)
                    }
                }
            }
        }
    }

    /**
     * this method is used for called edit profile Api
     */
    fun editProfileApi(user: User, path: String, listener: (APIResult<String>) -> Unit) {
        scope.launch {
            accountRepo.editProfileApi(user,path) { it ->
                when (it) {
                    is APIResult.Success -> {
                        pref.userModel = Gson().toJson(it.data)
                        listener(it)
                    }
                    is APIResult.Failure -> {
                        listener(it)
                    }
                    APIResult.InProgress -> {
                        listener(APIResult.InProgress)
                    }
                }
            }
        }
    }


    /**
     * this method is used for called edit profile Api
     */
    fun getProfileAPI(listener: (APIResult<String>) -> Unit) {
        scope.launch {
            accountRepo.getProfile {
                when (it) {
                    is APIResult.Success -> {
                        user = it.data
                        listener(APIResult.Success("", ""))
                    }
                    is APIResult.Failure -> {
                        listener(it)
                    }
                    APIResult.InProgress -> {
                        listener(APIResult.InProgress)
                    }
                }
            }
        }
    }

}