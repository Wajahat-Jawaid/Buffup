package com.wajahat.buffup.events

import com.wajahat.buffup.model.stream.Stream

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
interface OnStreamsListFetchedListener {

    fun onStreamsListFetched(streams: List<Stream>)
    fun onStreamsListError(t: Throwable)
}