package com.wajahat.buffup.model.stream

import com.google.gson.annotations.SerializedName
import com.wajahat.buffup.network.JSONKeys

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
data class StreamDetails(
    val id: Int,
    @SerializedName(JSONKeys.timeToShow)
    val timeToShow: Int,
    val priority: Int,
    val author: Author?,
    val question: Question?,
    val answers: List<Answer>?
) {

    data class Author(
        @SerializedName(JSONKeys.firstName) val firstName: String?,
        @SerializedName(JSONKeys.lastName) val lastName: String?,
        val image: String
    )

    data class Question(
        val id: Int,
        val title: String
    )

    data class Answer(
        val id: Int,
        @SerializedName(JSONKeys.buffId) val buffId: Int,
        val title: String,
        val image: StreamResponseImageHolder
    )
}