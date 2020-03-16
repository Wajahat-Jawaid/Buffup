package com.wajahat.buffup.events

import com.wajahat.buffup.model.stream.StreamDetails

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
interface OnStreamDetailsFetchedListener {

    fun onStreamFetched(streamsDetails: StreamDetails)
    fun onStreamError(t: Throwable)
}