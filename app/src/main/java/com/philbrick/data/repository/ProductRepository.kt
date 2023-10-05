package com.philbrick.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.philbrick.R
import com.philbrick.data.APIResponse
import com.philbrick.data.APIResult
import com.philbrick.data.entity.product.Product
import com.philbrick.data.entity.product.ProductBanner
import com.philbrick.data.enum.APIErrorCode
import com.philbrick.data.enum.Authorized
import com.philbrick.data.remote.APIManager
import com.philbrick.util.constant.Constant
import com.philbrick.util.helper.Helper

class ProductRepository (val context: Context) {
    private val api = APIManager.invoke(context)


    /**
     * this method is used for get banner list
     */
    suspend fun getBannerListApi(
        listener: (APIResult<ArrayList<ProductBanner>>) -> Unit
    ) {
        if (Helper.isNetworkAvailable(context)) {
            listener.invoke(APIResult.InProgress)

            try {
                val response = api.getBannerList()
                if (response.isSuccessful) {

                    response.body()?.asJsonObject?.let { jsonObject ->
                        val apiResponse: APIResponse = Gson().fromJson(jsonObject, object : TypeToken<APIResponse>() {}.type)
                        Log.d("APIRes", Gson().toJson(apiResponse))
                        when (apiResponse.authorized) {
                            Authorized.AUTHORIZED.isAuthorized -> {
                                var productBannerList : ArrayList<ProductBanner> = arrayListOf()
                                if (jsonObject.has("data") && !jsonObject.get("data").isJsonNull){
                                    productBannerList = Gson().fromJson(jsonObject.get("data"), object : TypeToken<ArrayList<ProductBanner>>() {}.type)
                                }
                                listener.invoke(APIResult.Success(productBannerList, apiResponse.message))
                                return
                            }
                            else -> {
                                listener(
                                    APIResult.Failure(
                                        APIErrorCode.INVALID_DATA, context.getString(
                                            R.string.error_msg)))
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
     * this method is used get product detail after scanning QR.
     */
    suspend fun getProductDetails(
        productId : String,
        listener: (APIResult<Product>) -> Unit
    ) {
        if (Helper.isNetworkAvailable(context)) {
            listener.invoke(APIResult.InProgress)
            val params = HashMap<String,String>()
            params["productid"] = productId
            params["internal_version"] = Constant.INTERNAL_VERSION
            params["external_version"] = Constant.EXTERNAL_VERSION
            params["device_type"] = Constant.DEVICE_TYPE
            Log.e("@@@@@@@@@json",productId)
            Log.e("@@@@@@@@@json",Constant.INTERNAL_VERSION)
            Log.e("@@@@@@@@@json",Constant.EXTERNAL_VERSION)
            Log.e("@@@@@@@@@json",Constant.DEVICE_TYPE)
            try {
                val response = api.getProductDetail(params)
                if (response.isSuccessful) {

                    response.body()?.asJsonObject?.let { jsonObject ->
                        val apiResponse: APIResponse = Gson().fromJson(jsonObject, object : TypeToken<APIResponse>() {}.type)
                        Log.d("APIRes", Gson().toJson(apiResponse))
                        when (apiResponse.authorized) {
                            Authorized.AUTHORIZED.isAuthorized -> {
                                var product = Product()
                                if (jsonObject.has("data") && !jsonObject.get("data").isJsonNull){
                                    product = Gson().fromJson(jsonObject.get("data").asJsonObject, object : TypeToken<Product>() {}.type)
                                    val gson = Gson()
                                    val json = gson.toJson(product)
                                    Log.e("@@@@@@@@@json",json)
                                }
                                listener.invoke(APIResult.Success(product, apiResponse.message))
                                return
                            }
                            else -> {
                                listener(
                                    APIResult.Failure(
                                        APIErrorCode.INVALID_DATA, context.getString(
                                            R.string.error_msg)))
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