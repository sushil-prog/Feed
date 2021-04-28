package com.ulu.feed.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ulu.feed.models.Feed

@Dao
interface FeedsDao {

    @Query("SELECT * FROM Feeds ORDER BY event_name ASC")
    suspend fun getFeeds(): List<Feed>

    @Query("SELECT * from Feeds ORDER BY views DESC")
    suspend fun getFeedSortedByViewsDSC(): List<Feed>

    @Query("SELECT * from Feeds ORDER BY shares DESC")
    suspend fun getFeedSortedByShareDSC(): List<Feed>

    @Query("SELECT * from Feeds ORDER BY likes DESC")
    suspend fun getFeedSortedByLikeDSC(): List<Feed>

    @Query("SELECT * from Feeds ORDER BY event_date DESC")
    suspend fun getFeedSortedByDateDSC(): List<Feed>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<Feed>)


}