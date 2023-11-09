package com.example.cryptowallet.DataClasses

import android.os.Parcel
import android.os.Parcelable

data class SaveCoinsModel(
    var coinId: Long,
    var coinName: String,
    var time : String,
    var date : String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    constructor() : this(0, "", "","")

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<SaveCoinsModel> {
        override fun createFromParcel(parcel: Parcel): SaveCoinsModel {
            return SaveCoinsModel(parcel)
        }

        override fun newArray(size: Int): Array<SaveCoinsModel?> {
            return arrayOfNulls(size)
        }
    }
}
