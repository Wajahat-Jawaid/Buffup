package com.wajahat.buffup.events

import com.wajahat.buffup.model.stream.StreamDetails

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
interface OnBuffAnswerListener {

    /** Called when the buff answer is selected  */
    fun onBuffAnswerSelected(answer: StreamDetails.Answer)
}