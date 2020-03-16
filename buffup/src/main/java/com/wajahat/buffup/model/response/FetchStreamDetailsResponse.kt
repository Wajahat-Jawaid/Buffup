package com.wajahat.buffup.model.response

import com.google.gson.annotations.SerializedName
import com.wajahat.buffup.model.stream.StreamDetails
import com.wajahat.buffup.network.JSONKeys

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
class FetchStreamDetailsResponse {

    @SerializedName(JSONKeys.RESULT)
    var streamDetails: StreamDetails? = null
}