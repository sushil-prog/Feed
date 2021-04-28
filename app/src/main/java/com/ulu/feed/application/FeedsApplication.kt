package com.ulu.feed.application

import android.app.Application
import com.ulu.feed.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.util.*

/**
 * .FeedsApplication application
 */

class FeedsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@FeedsApplication)
            modules(
                apiModule,
                remoteDataSource,
                viewModelModule,
                repositoryModule,
                retrofitModule,
                databaseModule,
                sharedPreferences
            )
        }
    }


}