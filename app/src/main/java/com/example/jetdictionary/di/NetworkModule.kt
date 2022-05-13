package com.example.jetdictionary.di

import android.content.Context
import com.example.jetdictionary.core.Constants.BASE_URL
import com.example.jetdictionary.core.DataStoreManager
import com.example.jetdictionary.core.NetworkHelper
import com.example.jetdictionary.data.repository.LoginRepositoryImpl
import com.example.jetdictionary.data.repository.RegisterRepositoryImpl
import com.example.jetdictionary.data.source.local.AppDatabase
import com.example.jetdictionary.data.source.local.ILocalSource
import com.example.jetdictionary.data.source.remote.IRemoteApi
import com.example.jetdictionary.domain.repository.ILoginRepository
import com.example.jetdictionary.domain.repository.IRegisterRepository
import com.example.jetdictionary.domain.usecase.LoginUseCase
import com.example.jetdictionary.domain.usecase.RegisterUseCase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Provides
    @Singleton
    fun providesRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        @ApplicationContext context: Context,
        iLocalSource: ILocalSource
    ): OkHttpClient {
        val cacheSize = (5 * 1024 * 1024).toLong()
        val mCache = Cache(context.cacheDir, cacheSize)
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .cache(mCache) // make your app offline-friendly without a database!
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)
            .addInterceptor { chain ->
                var request = chain.request()

                request =
                    if (true) request.newBuilder()
                        .header("Cache-Control", "public, max-age=" + 5)
                        .header("Authorization", "Bearer " + iLocalSource.getAccessToken())
                        .build()
                    else request.newBuilder().header(
                        "Cache-Control",
                        "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                    ).header("Authorization", "Bearer " + iLocalSource.getAccessToken())
                        .build()
                chain.proceed(request)
            }
        return client.build()
    }


    @Provides
    @Singleton
    fun providesGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun providesGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun providesRxJavaCallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    @Singleton
    fun providerRemoteApi(retrofit: Retrofit): IRemoteApi {
        return retrofit.create(IRemoteApi::class.java)
    }

    // repository
    @Provides
    @Singleton
    fun providerLoginRepository(
        iRemoteApi: IRemoteApi,
        iLocalSource: ILocalSource,
        appDatabase: AppDatabase,
        networkHelper: NetworkHelper,
        dataStoreManager: DataStoreManager
    ): ILoginRepository {
        return LoginRepositoryImpl(iRemoteApi, iLocalSource, appDatabase, networkHelper, dataStoreManager)
    }

    @Provides
    @Singleton
    fun providerRegisterRepository(
        iRemoteApi: IRemoteApi,
        networkHelper: NetworkHelper
    ): IRegisterRepository {
        return RegisterRepositoryImpl(iRemoteApi, networkHelper)
    }
    // use-case
    @Provides
    @Singleton
    fun providerLoginUseCase(repository: ILoginRepository): LoginUseCase =
        LoginUseCase(repository)

    @Provides
    @Singleton
    fun providerRegisterUseCase(repository: IRegisterRepository): RegisterUseCase =
        RegisterUseCase(repository)

}