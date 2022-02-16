package com.example.jetdictionary.di

import com.example.jetdictionary.presenter.navigation.ComposeCustomNavigator
import com.example.jetdictionary.presenter.navigation.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Provides
    @Singleton
    fun providerNavigator(): Navigator {
        return ComposeCustomNavigator()
    }
}