package com.ulu.feed.remote

import com.ulu.feed.models.FeedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FeedsApi {

    @GET("{pageKey}")
    suspend fun getFeedResponse(@Path(value = "pageKey", encoded = true) pageKey: String?): Response<FeedResponse>

}
