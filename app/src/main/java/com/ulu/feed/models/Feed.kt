package com.ulu.feed.models

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

/**
 * .storage model class for task item
 */
@Parcelize
@Entity(tableName = "Feeds")
data class Feed(
    @ColumnInfo(name = "thumbnail_image")
    var thumbnail_image: String,
    @ColumnInfo(name = "event_date")
    var event_date: Long,
    @PrimaryKey
    @ColumnInfo(name = "event_name")
    var event_name: String,
    @ColumnInfo(name = "views")
    var views: Int,
    @ColumnInfo(name = "likes")
    var likes: Int,
    @ColumnInfo(name = "shares")
    var shares: Int): Parcelable

//@Parcelize
//@Entity(tableName = "Feeds")
//data class Feed(
//    @ColumnInfo(name = "thumbnail_image")
//    var thumbnail_image: String,
//    @ColumnInfo(name = "event_date")
//    var event_date: Long,
//    @ColumnInfo(name = "index")
//    var index: Long,
//    @PrimaryKey
//    @ColumnInfo(name = "event_name")
//    var event_name: String,
//    @ColumnInfo(name = "views")
//    var views: Int,
//    @ColumnInfo(name = "likes")
//    var likes: Int,
//    @ColumnInfo(name = "shares")
//    var shares: Int): Parcelable





