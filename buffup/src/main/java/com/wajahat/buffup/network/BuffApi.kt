package com.wajahat.buffup.network

import com.wajahat.buffup.events.OnStreamDetailsFetchedListener
import com.wajahat.buffup.events.OnStreamsListFetchedListener
import com.wajahat.buffup.exception.APIErrorMessages
import com.wajahat.buffup.exception.BuffAPIException
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
class BuffApi {

    init {
        mInstance = this
    }

    companion object {
        private var mInstance: BuffApi? = null

        /** Fetch the list of streams from API
         * @param streamsListFetchedListener Event listener implemented by the calling
         *                                   activity/fragment to receive callbacks as
         *                                   the result of API response
         * */
        fun fetchStreams(streamsListFetchedListener: OnStreamsListFetchedListener) {
            BuffHTTPClient().getDataAccessService()
                .fetchStreams()
                .retry()
                .doOnError { e -> throw BuffAPIException(e) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response ->
                    run {
                        // Check all errors if present
                        // 1 - First case could be due to the server error. Ideally this use case
                        // should be handled by response codes returned by the API. But since no
                        // response code is returned now, so I've just checked it based on response
                        if (response?.listStreams == null) {
                            streamsListFetchedListener.onStreamsListError(
                                Throwable(APIErrorMessages.API_ERROR_FETCH_STREAMS)
                            )
                            return@subscribe
                        }
                        // 2 - This condition executes if API was successful but currently no
                        // active streams are on-going
                        if (response.listStreams.isEmpty()) {
                            streamsListFetchedListener.onStreamsListError(
                                Throwable(APIErrorMessages.STREAMS_NOT_PRESENT)
                            )
                            return@subscribe
                        }
                        // If no error is present, then call the success response
                        streamsListFetchedListener.onStreamsListFetched(response.listStreams)
                    }
                }
        }

        /** Fetch the details of particular stream from API
         * @param streamId Id of stream whose details are needed to be fetched
         * @param detailsListener Event listener implemented by the calling activity/fragment
         *                        to receive callbacks as the result of API response
         * */
        fun fetchStreamDetails(streamId: Int, detailsListener: OnStreamDetailsFetchedListener) {
            BuffHTTPClient().getDataAccessService().getStreamDetails(streamId)
                .retry()
                .doOnError { e -> throw BuffAPIException(e) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        run {
                            if (result.streamDetails != null) {
                                detailsListener.onStreamFetched(result.streamDetails!!)
                            } else {
                                detailsListener.onStreamError(
                                    Throwable("Error Fetching Stream Details")
                                )
                            }
                        }
                    },
                    { t -> detailsListener.onStreamError(t) }
                )
        }
    }
}