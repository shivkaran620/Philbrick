package com.philbrick.data.entity.product
import com.google.gson.annotations.SerializedName


data class ProductBanner(
    @SerializedName("id")
    var id : String?,

    @SerializedName("img_url")
    var imgUrl : String?,

    @SerializedName("name")
    var name : String?,
)
