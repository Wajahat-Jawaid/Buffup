package com.wajahat.buffup.model.response

import com.google.gson.annotations.SerializedName
import com.wajahat.buffup.network.JSONKeys
import com.wajahat.buffup.model.stream.Stream

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
class FetchStreamsListResponse {

    @SerializedName(JSONKeys.RESULT)
    val listStreams: List<Stream>? = null
}