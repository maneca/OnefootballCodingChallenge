package com.onefootball.di

import android.app.Application
import androidx.room.Room
import com.onefootball.data.local.NewsDatabase
import com.onefootball.data.remote.NewsApi
import com.onefootball.data.remote.NewsApiImp
import com.onefootball.data.repository.NewsRepositoryImp
import com.onefootball.domain.repository.NewsRepository
import com.onefootball.util.DefaultDispatcherProvider
import com.onefootball.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsModule {

    @Provides
    @Singleton
    fun provideNewsRepository(
        api: NewsApi,
        database: NewsDatabase
    ): NewsRepository{
        return NewsRepositoryImp(api, database.dao)
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(app: Application): NewsDatabase{
        return Room.databaseBuilder(
            app, NewsDatabase::class.java, "news_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNewsApi(app: Application): NewsApi{
        return NewsApiImp(app.applicationContext)
    }

    @Provides
    @Singleton
    fun providesDispatcher(): DispatcherProvider = DefaultDispatcherProvider()
}