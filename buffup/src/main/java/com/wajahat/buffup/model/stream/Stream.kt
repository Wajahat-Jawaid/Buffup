package com.wajahat.buffup.model.stream

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.wajahat.buffup.network.JSONKeys

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
data class Stream(
    val id: Int,
    val title: String,
    val description: String,
    @SerializedName(JSONKeys.url)
    val streamVideoUrl: String,
    val image: StreamResponseImageHolder?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(StreamResponseImageHolder::class.java.classLoader)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(streamVideoUrl)
        parcel.writeParcelable(image, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Stream> {
        override fun createFromParcel(parcel: Parcel): Stream {
            return Stream(parcel)
        }

        override fun newArray(size: Int): Array<Stream?> {
            return arrayOfNulls(size)
        }
    }
}