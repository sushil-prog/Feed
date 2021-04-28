package com.ulu.feed.di

import android.app.Application
import android.content.SharedPreferences


import androidx.room.Room
import ccom.ulu.feed.repo.Repository
import com.ulu.feed.viewmodels.FeedsViewModel
import com.ulu.feed.R
import com.ulu.feed.database.FeedDatabase
import com.ulu.feed.database.FeedsDao
import com.ulu.feed.remote.RemoteDataSource
import com.ulu.feed.remote.FeedsApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig.DEBUG
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

// dependency modules


val apiModule = module {

    fun provideFeedsApi(retrofit: Retrofit): FeedsApi {
        return retrofit.create(FeedsApi::class.java)
    }

    single { provideFeedsApi(get()) }

}

// This module used for saving index of page .
val sharedPreferences = module {

    fun getSharedPrefs(androidApplication: Application): SharedPreferences{
        return  androidApplication.getSharedPreferences("default",  android.content.Context.MODE_PRIVATE)
    }
    single{
        getSharedPrefs(androidApplication())
    }

    single<SharedPreferences.Editor> {
        getSharedPrefs(androidApplication()).edit()
    }
}


val databaseModule = module {

    fun provideDatabase(application: Application): FeedDatabase {
        return Room.databaseBuilder(application, FeedDatabase::class.java, "feeds")
            .build()
    }

    fun provideFeedsDao(database: FeedDatabase): FeedsDao {
        return database.feedsDao
    }


    single { provideDatabase(androidApplication()) }
    single { provideFeedsDao(get()) }
}

val remoteDataSource = module {

    fun provideRemoteDataSource(
        api: FeedsApi
    ): RemoteDataSource {
        return RemoteDataSource(api)
    }
    single { provideRemoteDataSource(get()) }

}

val repositoryModule = module {

    fun provideRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: FeedsDao
    ): Repository {
        return Repository(remoteDataSource, localDataSource)
    }
    single { provideRepository(get(), get()) }

}
val viewModelModule = module {

    viewModel { FeedsViewModel(repository = get()) }


}

val retrofitModule = module {
    val connectTimeout: Long = 30// 20s
    val readTimeout: Long = 30 // 20s

    fun provideHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
        if (DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        okHttpClientBuilder.build()
        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(baseUrl: String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    single { provideHttpClient() }
    single {
        val baseUrl = androidContext().getString(R.string.BASE_URL)
        provideRetrofit(baseUrl, get())
    }

}
