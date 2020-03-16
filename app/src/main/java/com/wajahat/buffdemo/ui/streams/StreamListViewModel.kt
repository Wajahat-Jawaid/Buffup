package com.wajahat.buffdemo.ui.streams

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.wajahat.buffdemo.base.BaseViewModel
import com.wajahat.buffup.events.OnStreamsListFetchedListener
import com.wajahat.buffup.model.stream.Stream
import com.wajahat.buffup.network.BuffApi
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
class StreamListViewModel : BaseViewModel() {

    val streamClickSubject = MutableLiveData<Stream>()
    val loadingVisibility = MutableLiveData<Int>()
    val streamListAdapter = StreamsListAdapter()
    private val streamListViewState = MutableLiveData<StreamListViewState>()

    fun getStreamListViewState(): MutableLiveData<StreamListViewState> {
        return streamListViewState
    }

    init {
        loadStreams()
    }

    /** Fetch Streams from Buff SDK */
    fun loadStreams() {
        onRetrievePostListStart()
        BuffApi.fetchStreams(object : OnStreamsListFetchedListener {
            override fun onStreamsListFetched(streams: List<Stream>) {
                onRetrievePostListSuccess(streams)
            }

            override fun onStreamsListError(t: Throwable) {
                onRetrievePostListError(t)
            }
        })
    }

     fun onRetrievePostListStart() {
        loadingVisibility.value = View.VISIBLE
    }

     fun onRetrievePostListSuccess(streams: List<Stream>) {
        loadingVisibility.value = View.GONE
        streamListAdapter.updateStreamList(streams)
        streamListAdapter.streamClickSubject
            .subscribe { stream -> streamClickSubject.value = stream }
    }

     fun onRetrievePostListError(t: Throwable) {
        loadingVisibility.value = View.GONE
    }
}