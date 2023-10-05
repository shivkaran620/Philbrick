package com.philbrick.data.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("userid")
    var userId: String,
    @SerializedName("fname")
    var firstName: String,
    @SerializedName("lname")
    var lastName: String,
    @SerializedName("emailid")
    var emailAddress: String,
    @SerializedName("Mobile_No")
    var phoneNo: String,
    @SerializedName("company")
    var companyName: String,
    @SerializedName("address")
    var address: String,
    @SerializedName("img_url")
    var photo: String,
    @SerializedName("auth_token")
    var token : String
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
        parcel.readString() ?: ""
    ) {
    }

    constructor() : this("", "", "", "", "", "", "", "","")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(emailAddress)
        parcel.writeString(phoneNo)
        parcel.writeString(companyName)
        parcel.writeString(address)
        parcel.writeString(photo)
        parcel.writeString(token)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}