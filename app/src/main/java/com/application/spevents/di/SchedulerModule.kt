package com.application.spevents.di

import com.application.spevents.main.model.AppRxSchedulers
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class SchedulerModule {
    @Provides
    fun provideRxSchedulers() = AppRxSchedulers(
            database = Schedulers.single(),
            disk = Schedulers.io(),
            network = Schedulers.io(),
            main = AndroidSchedulers.mainThread()
    )
}