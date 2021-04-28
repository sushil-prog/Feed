package com.ulu.feed.models

data class FeedResponse(
    var posts: List<Feed>,
    val page: Int){
}
