package com.ulu.feed.remote


class RemoteDataSource  constructor(
    private val characterService: FeedsApi
) : BaseDataSource() {

    suspend fun getFeeds(pageKey:String) = getResult { characterService.getFeedResponse(pageKey) }
}