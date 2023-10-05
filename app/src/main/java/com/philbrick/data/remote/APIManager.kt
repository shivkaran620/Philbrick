package com.philbrick.data.remote

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.philbrick.data.enum.HTTPCode
import com.philbrick.data.local.PreferenceManager
import com.philbrick.data.repository.AccountRepository
import com.philbrick.ui.screen.account.LoginActivity
import com.philbrick.util.constant.AppURL
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


interface APIManager {
    companion object {

        private const val API_KEY = "ehello"
        private fun getOkHttpClient(context: Context): OkHttpClient {

            var request: Request?
            val httpClient = OkHttpClient.Builder()
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)

            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val accountRepository = AccountRepository(context)
                val hasToken = PreferenceManager(context).accessToken.isNotEmpty()
                Log.e("@@@hasToken",PreferenceManager(context).accessToken)
                request = when {
                    hasToken -> {
                        original.newBuilder()
                            .header("x-api-key",API_KEY)
                            .header("Accept", "application/json")
                            .header("Auth-Key","authforall")
                            .header("Client-Service","secureclient")
                            .header("auth-token", PreferenceManager(context).accessToken)
                            .method(original.method, original.body)
                            .build()
                    }
                    else -> {
                        original.newBuilder()
                            .header("x-api-key",API_KEY)
                            .header("Client-Service","secureclient")
                            .header("Auth-Key","authforall")
                            .header("Accept", "application/json")
                            .method(original.method, original.body)
                            .build()
                    }
                }
                Log.d("Token","ElseToken")
                val response = chain.proceed(request!!)
                val responseBody = response.body
                val contentType = responseBody?.contentType()
                val content = responseBody?.string()


                try {
                    val objResponse = Gson().fromJson(content, JsonElement::class.java).asJsonObject
                    Log.v("ForceLogoutCode1",objResponse.get("status").asInt.toString())
                    Log.v("ForceLogoutCode2",response.code.toString())
                    if (response.code == HTTPCode.FORCE_LOGOUT.code) {
                        try {
                            val message = "can't proceed. Please Login again"

                            if (context is Activity) {
                                context.runOnUiThread {
                                    if (message.isNotBlank()) {
                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                           // accountRepository.logOut()
                            context.startActivity(
                                Intent(context,LoginActivity::class.java).addFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                response.newBuilder().body(ResponseBody.create(contentType, content!!)).build()
            }

            return httpClient.build()
        }

        operator fun invoke(context: Context): APIManager {
            val retrofit = Retrofit.Builder()
                .baseUrl(AppURL.API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient(context))
                .build()
            return retrofit.create(APIManager::class.java)
        }
    }

    @FormUrlEncoded
    @POST(AppURL.API.MOBILE_VERIFICATION)
    suspend fun mobileVerificationApi(@FieldMap params: Map<String,String>): Response<JsonElement>

    @FormUrlEncoded
    @POST(AppURL.API.OTP_VERIFY)
    suspend fun otpVerifyAPI(@FieldMap params: Map<String,String>): Response<JsonElement>

    @FormUrlEncoded
    @POST(AppURL.API.GET_PROFILE)
    suspend fun getProfile(@FieldMap params: Map<String,String>): Response<JsonElement>

    @POST(AppURL.API.GET_BANNER)
    suspend fun getBannerList(): Response<JsonElement>


    @FormUrlEncoded
    @POST(AppURL.API.GET_PRODUCT_DETAIL)
    suspend fun getProductDetail(@FieldMap params: Map<String,String>): Response<JsonElement>

    @Multipart
    @POST(AppURL.API.UPDATE_PROFILE)
    suspend fun updateProfile(@PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>): Response<JsonElement>

}