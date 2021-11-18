package com.application.spevents.di

import com.application.spevents.SpEventsApp
import com.application.spevents.api.Api
import com.application.spevents.util.NetworkUtil
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class NetworkModule {
    @Provides
    fun providesRetrofit(): Api = Retrofit.Builder()
            .baseUrl(Api.URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(Api::class.java)

    @Provides
    fun providesNetworkUtil(application: SpEventsApp) = NetworkUtil(application)
}