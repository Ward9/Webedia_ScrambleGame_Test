package com.example.webedia_scrumble_game.di

import android.content.Context
import com.example.webedia_scrumble_game.data.local.AppDatabase
import com.example.webedia_scrumble_game.data.local.IUserDAO
import com.example.webedia_scrumble_game.data.repository.UserRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module use for dependency injection to provide constructors
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideDatabasePaging(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase) = db.userDao()

    @Singleton
    @Provides
    fun provideRepository(localDataSource: IUserDAO) =
        UserRepository(localDataSource)
}