package com.wajahat.buffup.model.stream

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
class StreamResponseImageHolder() : Parcelable {

    @SerializedName("0")
    var imageModel0: StreamImage? = null

    @SerializedName("1")
    var imageModel1: StreamImage? = null

    constructor(parcel: Parcel) : this()

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(imageModel0, flags)
        parcel.writeParcelable(imageModel1, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StreamResponseImageHolder> {
        override fun createFromParcel(parcel: Parcel): StreamResponseImageHolder {
            return StreamResponseImageHolder(parcel)
        }

        override fun newArray(size: Int): Array<StreamResponseImageHolder?> {
            return arrayOfNulls(size)
        }
    }
}