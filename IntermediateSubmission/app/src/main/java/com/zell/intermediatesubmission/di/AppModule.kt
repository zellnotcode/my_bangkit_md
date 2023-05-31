package com.zell.intermediatesubmission.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.paging.PagingSource
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.zell.intermediatesubmission.BuildConfig
import com.zell.intermediatesubmission.api.ApiServices
import com.zell.intermediatesubmission.local.DataStoreManager
import com.zell.intermediatesubmission.response.ListStoryItem
import com.zell.intermediatesubmission.response.StoriesResponse
import com.zell.intermediatesubmission.utils.StoryPagingSource
import com.zell.intermediatesubmission.utils.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUrl() = "https://story-api.dicoding.dev/v1/"

    @Provides
    @Singleton
    fun provideGson() : Gson = GsonBuilder().setLenient().create()

    fun provideOKHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.addInterceptor(loggingInterceptor)

        clientBuilder.build()
    } else {
        OkHttpClient.Builder().build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(url: String, gson: Gson) : ApiServices =
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(provideOKHttpClient())
            .build()
            .create(ApiServices::class.java)

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideDataStoreManager(dataStore: DataStore<Preferences>) : DataStoreManager {
        return DataStoreManager(dataStore)
    }
}