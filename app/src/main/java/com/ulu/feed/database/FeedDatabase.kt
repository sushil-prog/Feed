package com.ulu.feed.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ulu.feed.models.Feed

/**
 * .Fees abstract database class important for using room database
 */
@Database(entities = [Feed::class], version = 1)
abstract  class FeedDatabase : RoomDatabase() {
    abstract val feedsDao: FeedsDao
}