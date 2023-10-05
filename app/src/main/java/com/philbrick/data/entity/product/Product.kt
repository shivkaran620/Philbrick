package com.philbrick.data.entity.product

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("product_id")
    var productId: String?,
    @SerializedName("product_ser_id")
    var productSerId: String?,
    @SerializedName("product_name")
    var productName: String?,
    @SerializedName("product_category")
    var productCategory: String?,
    @SerializedName("product_srno")
    var productSrNo: String?,
    @SerializedName("product_client")
    var productClient: String?,
    @SerializedName("product_date_mfg")
    var productDateMfg: String?,
    @SerializedName("product_floor")
    var productFloor: String?,
    @SerializedName("product_driveno")
    var productDriveNo: String?,
    @SerializedName("product_dbr_value")
    var productDbrValue: String?,
    @SerializedName("product_speaker")
    var productSpeaker: String?,
    @SerializedName("product_magnets")
    var productMagnets: String?,
    @SerializedName("product_photoswitch")
    var productPhotoSwitch: String?,
    @SerializedName("product_drw_mnl")
    var productDrwMnl: String?,
    @SerializedName("product_hassembly")
    var productHasseMBLY: String?,
    @SerializedName("product_sill")
    var productSill: String?,
    @SerializedName("product_skate")
    var productSkate: String?,
    @SerializedName("product_sill_set")
    var productSillSet: String?,
    @SerializedName("product_car_hed_fclamp")
    var productCarHedFClamp: String?,
    @SerializedName("product_mdp_supply")
    var productMdpSupply: String?,
    @SerializedName("product_motor")
    var productMotor: String?,
    @SerializedName("product_drive")
    var productDrive: String?,
    @SerializedName("product_hard_kit")
    var productHardKit: String?,
    @SerializedName("product_qrcode")
    var productQrCode: String?,
    @SerializedName("product_cdate")
    var productCDate: String?,
    @SerializedName("product_udate")
    var productUDate: String?,
    @SerializedName("proser_id")
    var proserId: String?,
    @SerializedName("proser_name")
    var proserName: String?,
    @SerializedName("proser_logo")
    var proserLogo: String?,
    @SerializedName("proser_cdate")
    var proserCdate: String?,
    @SerializedName("proser_udate")
    var proserUdate: String?,
    @SerializedName("cat_logo")
    var catLogo: String?,

    @SerializedName("cat_name")
    var cat_name: String?,
    @SerializedName("subcat_name")
    var subcat_name: String?,
    @SerializedName("proname_name")
    var proname_name: String?,
    @SerializedName("partname_name")
    var partname_name: String?,
    @SerializedName("partcode_name")
    var partcode_name: String?,
    @SerializedName("product_serialno")
    var product_serialno: String?,

    @SerializedName("product_version")
    var product_version: String?,

    @SerializedName("product_powersupply")
    var product_powersupply: String?,
    @SerializedName("product_hp")
    var product_hp: String?,
    @SerializedName("product_supply")
    var product_supply: String?,
    @SerializedName("product_model")
    var product_model: String?,


) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",

    )

    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "" ,
        "",
        "",
        "",
        "",
        "",
        "" ,
        "",
        "",
        "" ,
        "",
        "",

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(productSerId)
        parcel.writeString(productName)
        parcel.writeString(productCategory)
        parcel.writeString(productSrNo)
        parcel.writeString(productClient)
        parcel.writeString(productDateMfg)
        parcel.writeString(productFloor)
        parcel.writeString(productDriveNo)
        parcel.writeString(productDbrValue)
        parcel.writeString(productSpeaker)
        parcel.writeString(productMagnets)
        parcel.writeString(productPhotoSwitch)
        parcel.writeString(productDrwMnl)
        parcel.writeString(productHasseMBLY)
        parcel.writeString(productSill)
        parcel.writeString(productSkate)
        parcel.writeString(productSillSet)
        parcel.writeString(productCarHedFClamp)
        parcel.writeString(productMdpSupply)
        parcel.writeString(productMotor)
        parcel.writeString(productDrive)
        parcel.writeString(productHardKit)
        parcel.writeString(productQrCode)
        parcel.writeString(productCDate)
        parcel.writeString(productUDate)
        parcel.writeString(proserId)
        parcel.writeString(proserName)
        parcel.writeString(proserLogo)
        parcel.writeString(proserCdate)
        parcel.writeString(proserUdate)
        parcel.writeString(catLogo)
        parcel.writeString(cat_name)
        parcel.writeString(subcat_name)
        parcel.writeString(proname_name)
        parcel.writeString(partname_name)
        parcel.writeString(partcode_name)
        parcel.writeString(product_serialno)
        parcel.writeString(product_version)
        parcel.writeString(product_powersupply)
        parcel.writeString(product_hp)
        parcel.writeString(product_supply)
        parcel.writeString(product_model)


    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }

}
