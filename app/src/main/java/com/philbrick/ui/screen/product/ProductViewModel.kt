package com.philbrick.ui.screen.product

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.philbrick.data.APIResult
import com.philbrick.data.entity.product.Product
import com.philbrick.data.entity.product.ProductBanner
import com.philbrick.data.local.PreferenceManager
import com.philbrick.data.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.Main
    private val scope: CoroutineScope = CoroutineScope(coroutineContext)
    private val productRepo = ProductRepository(application)
    private var pref = PreferenceManager(application)
     var bannerList : ArrayList<ProductBanner> = arrayListOf()
    var product = Product()
    fun isUserLoggedIn() = pref.accessToken.isNotEmpty()


    /**
     * this method is used for MobileVerification Api
     */
    fun getProductBannerList(listener: (APIResult<String>) -> Unit) {
        scope.launch {
            productRepo.getBannerListApi{ it->
                when (it) {
                    is APIResult.Success -> {
                        bannerList = it.data
                        listener(APIResult.Success("",it.message))
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
     * this method is used for MobileVerification Api
     */
    fun getProductDetails(productId: String, listener: (APIResult<String>) -> Unit) {
        scope.launch {
            productRepo.getProductDetails(productId){ it->
                when (it) {
                    is APIResult.Success -> {
                        product = it.data
                        val gson = Gson()
                        val json = gson.toJson(product)
                        Log.e("@@@@json",json)
                        listener(APIResult.Success("",it.message))
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