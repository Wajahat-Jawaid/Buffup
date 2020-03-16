package com.wajahat.buffup.model.stream

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.wajahat.buffup.network.JSONKeys

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
data class StreamImage(
    val id: String,
    val key: String, @SerializedName(JSONKeys.url) val imageUrl: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(key)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StreamImage> {
        override fun createFromParcel(parcel: Parcel): StreamImage {
            return StreamImage(parcel)
        }

        override fun newArray(size: Int): Array<StreamImage?> {
            return arrayOfNulls(size)
        }
    }
}