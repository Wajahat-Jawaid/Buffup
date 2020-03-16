package com.wajahat.buffup.network

import com.wajahat.buffup.model.response.FetchStreamDetailsResponse
import com.wajahat.buffup.model.response.FetchStreamsListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

interface BuffDataAccessService {

    /**
     * Fetch the list of the streams from the API
     */
    @GET("streams")
    fun fetchStreams(): Observable<FetchStreamsListResponse>

    /**
     * Fetch particular stream details
     */
    @GET("buffs/{stream_id}")
    fun getStreamDetails(@Path("stream_id") id: Int): Observable<FetchStreamDetailsResponse>
}