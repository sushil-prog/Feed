package com.ulu.feed.viewmodels

import androidx.lifecycle.*
import ccom.ulu.feed.repo.Repository
import com.ulu.feed.models.FeedResponse
import com.ulu.feed.utils.PageIndexKey
import com.ulu.feed.utils.Resource
import com.ulu.feed.utils.Sort
import kotlinx.coroutines.launch


class FeedsViewModel(private val repository: Repository) : ViewModel() {

    var feedsData = MutableLiveData<Resource<FeedResponse>>()

    init {
        var key: String = PageIndexKey.pageMap[0].toString()
        fetchFeedItems(key)
    }

    fun fetchFeedItems( key:String){
        viewModelScope.launch {
            repository.getFeedsData(key,feedsData)
        }
    }



    fun  sortBy(sortingMethod: Sort) {
       viewModelScope.launch {
            repository.sort(sortingMethod,items = feedsData)
        }

    }



}