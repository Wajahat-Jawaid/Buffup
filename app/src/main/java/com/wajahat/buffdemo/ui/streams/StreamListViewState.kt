package com.wajahat.buffdemo.ui.streams

import com.wajahat.buffdemo.base.BaseViewState
import com.wajahat.buffup.model.stream.Stream

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
class StreamListViewState private constructor(
    data: Stream?,
    currentState: Int?,
    error: Throwable?
) : BaseViewState<Stream>(data, error, currentState) {
    init {
        this.data = data
        this.error = error
        this.currentState = currentState
    }

    companion object {

        var ERROR_STATE = StreamListViewState(null, State.FAILED.value, Throwable())
        var LOADING_STATE = StreamListViewState(null, State.LOADING.value, null)
        var SUCCESS_STATE =
            StreamListViewState(Stream(0, "", "", "", null),
                State.SUCCESS.value, null)
    }
}