package com.application.spevents.di

import com.application.spevents.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [AppModule::class])
    abstract fun contributeMainActivity(): MainActivity
}