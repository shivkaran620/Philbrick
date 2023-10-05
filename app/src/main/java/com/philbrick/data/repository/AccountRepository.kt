package com.philbrick.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.philbrick.R
import com.philbrick.data.APIResponse
import com.philbrick.data.APIResult
import com.philbrick.data.entity.User
import com.philbrick.data.enum.APIErrorCode
import com.philbrick.data.enum.HTTPCode
import com.philbrick.data.local.PreferenceManager
import com.philbrick.data.remote.APIManager
import com.philbrick.util.constant.Constant
import com.philbrick.util.helper.Helper
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class AccountRepository(val context: Context) {
    private val api = APIManager.invoke(context)
    private val pref = PreferenceManager(context)
   //private val preferenceRepository = PreferenceRepository(context)

    /**
     * This method is called for mobile verification API.
     * @param listener: Invokes when api response comes -> Message & isSuccess
     * @param mobileNo input params
     */
    suspend fun mobileVerificationApi(
        mobileNo : String,
        listener: (APIResult<String>) -> Unit
    ) {
        if (Helper.isNetworkAvailable(context)) {
            listener.invoke(APIResult.InProgress)
            val params = HashMap<String,String>()
            params["mobile"] = mobileNo

            Log.d("SignInParams", Gson().toJson(params))
            try {
                val response = api.mobileVerificationApi(params)
                if (response.isSuccessful) {
                    Log.d("SignInResponse", Gson().toJson(response.body()))

                    response.body()?.asJsonObject?.let { jsonObject ->
                        val apiResponse: APIResponse = Gson().fromJson(jsonObject, object : TypeToken<APIResponse>() {}.type)
                        Log.d("APIRes",Gson().toJson(apiResponse))
                        when (response.code()) {
                            HTTPCode.SUCCESS.code, HTTPCode.SUCCESS_1.code -> {
                                listener.invoke(APIResult.Success("", apiResponse.message))
                                return
                            }
                            else -> {
                                listener(APIResult.Failure(APIErrorCode.INVALID_DATA, context.getString(R.string.error_msg)))
                                return
                            }
                        }
                    }.let {

                        listener(
                            APIResult.Failure(
                                APIErrorCode.NO_RESPONSE,
                                context.getString(R.string.error_msg)
                            )
                        )
                        return
                    }
                } else {
                    listener(
                        APIResult.Failure(
                            APIErrorCode.INVALID_DATA,
                            context.getString(R.string.error_msg)
                        )
                    )
                    return
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }

        } else {
            listener(
                APIResult.Failure(
                    APIErrorCode.NETWORK_ERROR,
                    context.getString(R.string.network_error_msg)
                )
            )
            return
        }
    }

    /**
     * this method is for called verify otp.
     * @param otp
     * return @param listener for success, InProgress, Failed.
     */
    suspend fun verifyOtpApi(
        otp : String,
        mobileNo: String,
        listener: (APIResult<User>) -> Unit
    ) {
        if (Helper.isNetworkAvailable(context)) {
            listener.invoke(APIResult.InProgress)
            val params = HashMap<String,String>()
            params["otp"] = otp
            params["mobile"] = mobileNo
            params["internal_version"] = Constant.INTERNAL_VERSION
            params["external_version"] = Constant.EXTERNAL_VERSION
            params["device_type"] = Constant.DEVICE_TYPE
            // params["device_token"] = preferenceRepository.getDeviceToken()

            try {
                val response = api.otpVerifyAPI(params)
                if (response.isSuccessful) {
                    response.body()?.asJsonObject?.let { jsonObject ->
                        val apiResponse: APIResponse = Gson().fromJson(jsonObject, object : TypeToken<APIResponse>() {}.type)
                        when (response.code()) {
                            HTTPCode.SUCCESS.code, HTTPCode.SUCCESS_1.code -> {
                                var user = User()
                                if (jsonObject.has("is_otp_match")&&!jsonObject.get("is_otp_match").isJsonNull){
                                    val isMatch = jsonObject.get("is_otp_match").asBoolean
                                    if (isMatch){
                                        user = Gson().fromJson(jsonObject.get("data").asJsonObject, object : TypeToken<User>() {}.type)
                                        pref.userFirstName= user.firstName
                                        pref.userId= user.userId?:""
                                        pref.userMobile= user.phoneNo?:""
                                        listener.invoke(APIResult.Success(user, apiResponse.message))
                                        return
                                    }else{
                                        listener.invoke(APIResult.Failure(APIErrorCode.INVALID_DATA, "otp doesn't match"))
                                        return
                                    }
                                }
                                listener.invoke(APIResult.Success(user, apiResponse.message))
                                return
                            }
                            else -> {
                                listener(
                                    APIResult.Failure(
                                        APIErrorCode.INVALID_DATA,
                                        context.getString(R.string.error_msg)
                                    )
                                )
                                return
                            }
                        }
                    }.let {

                        listener(
                            APIResult.Failure(
                                APIErrorCode.NO_RESPONSE,
                                context.getString(R.string.error_msg)
                            )
                        )
                        return
                    }
                } else {
                    listener(
                        APIResult.Failure(
                            APIErrorCode.INVALID_DATA,
                            context.getString(R.string.error_msg)
                        )
                    )
                    return
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }

        } else {
            listener(
                APIResult.Failure(
                    APIErrorCode.NETWORK_ERROR,
                    context.getString(R.string.network_error_msg)
                )
            )
            return
        }
    }


    /**
     * this method is for called verify otp.
     * @param user its an object of USER entity class
     * return @param listener for success, InProgress, Failed.
     */
    suspend fun editProfileApi(
        user : User,
        newPath : String,
        listener: (APIResult<String>) -> Unit
    ) {
        if (Helper.isNetworkAvailable(context)) {
            listener.invoke(APIResult.InProgress)
            val params = HashMap<String,RequestBody>()


            params["fname"] = (user.firstName ?:"").toRequestBody("text/plain".toMediaTypeOrNull())
            params["lname"] = (user.lastName ?:"").toRequestBody("text/plain".toMediaTypeOrNull())
            params["phoneNo"] = (user.phoneNo?:"").toRequestBody("text/plain".toMediaTypeOrNull())
            params["company"] = (user.companyName?:"").toRequestBody("text/plain".toMediaTypeOrNull())
           // params["photo"] = (user.photo?:"").toRequestBody("text/plain".toMediaTypeOrNull())

            if (newPath.isNotEmpty() && newPath!=user.photo){
                val compressedImageFile = Compressor.compress(context,File(newPath)) {
                    resolution(Constant.COMPRESSED_IMG_RESOLUTION_WIDTH, Constant.COMPRESSED_IMG_RESOLUTION_HEIGHT)
                    quality(Constant.COMPRESSED_IMG_QUALITY)
                    format(Constant.COMPRESSED_IMG_FORMAT)
                    size(Constant.COMPRESSED_IMG_SIZE)
                }
                params["user_image\"; filename=\"${compressedImageFile.name}"] = (compressedImageFile).asRequestBody("file".toMediaTypeOrNull())
               // params["user_image"] = n
            }
            else{
             //   params["user_image"] = ("").toRequestBody("text/plain".toMediaTypeOrNull())
            }

            params["email"] = (user.emailAddress?:"").toRequestBody("text/plain".toMediaTypeOrNull())
            params["address"] = (user.address?:"").toRequestBody("text/plain".toMediaTypeOrNull())
            params["userid"] =  (pref.userId).toRequestBody("text/plain".toMediaTypeOrNull())
            params["internal_version"] = Constant.INTERNAL_VERSION.toRequestBody("text/plain".toMediaTypeOrNull())
            params["external_version"] = Constant.EXTERNAL_VERSION.toRequestBody("text/plain".toMediaTypeOrNull())
            params["device_type"] = Constant.DEVICE_TYPE.toRequestBody("text/plain".toMediaTypeOrNull())

            Log.d("Params", (Gson() to params).toString())
            try {
                val response = api.updateProfile(params)
                if (response.isSuccessful) {
                    response.body()?.asJsonObject?.let { jsonObject ->
                        val apiResponse: APIResponse = Gson().fromJson(jsonObject, object : TypeToken<APIResponse>() {}.type)
                        when (response.code()) {
                            HTTPCode.SUCCESS.code, HTTPCode.SUCCESS_1.code -> {
                                listener.invoke(APIResult.Success("", apiResponse.message))
                                return
                            }
                            else -> {
                                listener(
                                    APIResult.Failure(
                                        APIErrorCode.INVALID_DATA,
                                        context.getString(R.string.error_msg)
                                    )
                                )
                                return
                            }
                        }
                    }.let {

                        listener(
                            APIResult.Failure(
                                APIErrorCode.NO_RESPONSE,
                                context.getString(R.string.error_msg)
                            )
                        )
                        return
                    }
                } else {
                    listener(
                        APIResult.Failure(
                            APIErrorCode.INVALID_DATA,
                            context.getString(R.string.error_msg)
                        )
                    )
                    return
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }

        } else {
            listener(
                APIResult.Failure(
                    APIErrorCode.NETWORK_ERROR,
                    context.getString(R.string.network_error_msg)
                )
            )
            return
        }
    }

    /**
     * this method is for called verify otp.
     * @param otp
     * return @param listener for success, InProgress, Failed.
     */
    suspend fun getProfile(
        listener: (APIResult<User>) -> Unit
    ) {
        if (Helper.isNetworkAvailable(context)) {
            listener.invoke(APIResult.InProgress)
            val params = HashMap<String,String>()
            params["userid"] = PreferenceManager(context).userId
            params["internal_version"] = Constant.INTERNAL_VERSION
            params["external_version"] = Constant.EXTERNAL_VERSION
            params["device_type"] = Constant.DEVICE_TYPE

            try {
                val response = api.getProfile(params)
                if (response.isSuccessful) {
                    response.body()?.asJsonObject?.let { jsonObject ->
                        val apiResponse: APIResponse = Gson().fromJson(jsonObject, object : TypeToken<APIResponse>() {}.type)
                        when (response.code()) {
                            HTTPCode.SUCCESS.code, HTTPCode.SUCCESS_1.code -> {
                                var user = User()
                                if (jsonObject.has("data") && !jsonObject.get("data").isJsonNull){
                                    user = Gson().fromJson(jsonObject.get("data").asJsonObject, object : TypeToken<User>() {}.type)
                                    listener.invoke(APIResult.Success(user, apiResponse.message))
                                    return
                                }
                                listener.invoke(APIResult.Success(user, apiResponse.message))
                                return
                            }
                            else -> {
                                listener(
                                    APIResult.Failure(
                                        APIErrorCode.INVALID_DATA,
                                        context.getString(R.string.error_msg)
                                    )
                                )
                                return
                            }
                        }
                    }.let {

                        listener(
                            APIResult.Failure(
                                APIErrorCode.NO_RESPONSE,
                                context.getString(R.string.error_msg)
                            )
                        )
                        return
                    }
                } else {
                    listener(
                        APIResult.Failure(
                            APIErrorCode.INVALID_DATA,
                            context.getString(R.string.error_msg)
                        )
                    )
                    return
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }

        } else {
            listener(
                APIResult.Failure(
                    APIErrorCode.NETWORK_ERROR,
                    context.getString(R.string.network_error_msg)
                )
            )
            return
        }
    }
}