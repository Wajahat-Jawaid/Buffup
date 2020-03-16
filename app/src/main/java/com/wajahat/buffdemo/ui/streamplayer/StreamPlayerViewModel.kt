package com.wajahat.buffdemo.ui.streamplayer

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.wajahat.buffdemo.base.BaseViewModel
import com.wajahat.buffup.events.OnStreamDetailsFetchedListener
import com.wajahat.buffup.model.stream.Stream
import com.wajahat.buffup.model.stream.StreamDetails
import com.wajahat.buffup.network.BuffApi

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
class StreamPlayerViewModel : BaseViewModel() {

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val streamDetails: MutableLiveData<StreamDetails> = MutableLiveData()

    /** Fetch Stream Details from Buff SDK
     * @param stream Selected Stream to fetch details for
     * */
    fun fetchStreamDetails(stream: Stream) {
        onRetrievePostListStart()
        // Instead of stream id, '1' is passed. Because at the time of test development,
        // 2 streams are present with id's 11 and 12. Whereas the stream details could be
        // fetched only on id 1 and 2.
        // In actual it would be 'stream.id'
        BuffApi.fetchStreamDetails(1, object : OnStreamDetailsFetchedListener {
            override fun onStreamFetched(streamsDetails: StreamDetails) {
                onRetrievePostListSuccess(streamsDetails)
            }

            override fun onStreamError(t: Throwable) {
                onRetrievePostListError(t)
            }
        })
    }

    private fun onRetrievePostListStart() {
        loadingVisibility.value = View.VISIBLE
    }

    private fun onRetrievePostListSuccess(details: StreamDetails) {
        streamDetails.value = details
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListError(t: Throwable) {
        loadingVisibility.value = View.GONE
    }
}