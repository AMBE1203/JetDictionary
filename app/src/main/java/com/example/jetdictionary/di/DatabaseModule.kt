package com.example.jetdictionary.di

import android.app.Application
import androidx.room.Room
import com.example.jetdictionary.data.source.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    internal fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, AppDatabase.DB_NAME)
            .allowMainThreadQueries().build()
    }
}