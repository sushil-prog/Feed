package ccom.ulu.feed.repo

import androidx.lifecycle.MutableLiveData
import com.aiegroup.todo.utils.Utility
import com.ulu.feed.database.FeedsDao
import com.ulu.feed.models.Feed
import com.ulu.feed.models.FeedResponse
import com.ulu.feed.remote.RemoteDataSource
import com.ulu.feed.utils.Resource
import com.ulu.feed.utils.Sort

/**
 * .Repository class isolate data source transactions from app components
 */
class Repository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: FeedsDao
) {

    suspend fun getFeedsData(key: String, feedsData: MutableLiveData<Resource<FeedResponse>>) {
        feedsData.value = Resource.loading(FeedResponse(emptyList(), 0))
        var response = remoteDataSource.getFeeds(pageKey = key)
        if (response.status == Resource.Status.SUCCESS) {
            response.data?.let {
                localDataSource.insertAll(it.posts)
            }
            response.data?.posts = FeedResponse(localDataSource.getFeeds(), 0).posts
            feedsData.value = response


        } else if (response.status == Resource.Status.ERROR) {
            var feed = localDataSource.getFeeds()
            if (feed.isEmpty()) {
                feedsData.value = response
            } else {
                feedsData.value = Resource.success(FeedResponse(localDataSource.getFeeds(), 0))

            }
        }
    }

    suspend fun sort(
        sortingMethod: Sort,
        items: MutableLiveData<Resource<FeedResponse>>
    ): MutableLiveData<Resource<FeedResponse>> {
        when (sortingMethod) {
            Sort.SORT_DSC_LIKE -> {
                items.value = Resource.sort(
                    FeedResponse(
                        localDataSource.getFeedSortedByLikeDSC(), -1
                    )
                )
            }
            Sort.SORT_DSC_VIEW -> {
                items.value = Resource.sort(
                    FeedResponse(
                        localDataSource.getFeedSortedByViewsDSC(), -1
                    )
                )
            }
            Sort.SORT_DSC_SHARE -> {
                items.value = Resource.sort(
                    FeedResponse(
                        localDataSource.getFeedSortedByShareDSC(), -1
                    )
                )
            }
            Sort.SORT_DSC_DATE -> {
                items.value = Resource.sort(
                    FeedResponse(
                        localDataSource.getFeedSortedByDateDSC(), -1
                    )
                )
            }
            Sort.SORT_NONE -> {
                items.value = Resource.sort(
                    FeedResponse(
                        localDataSource.getFeeds(), -1
                    )
                )
            }
        }
        return items
    }
}

