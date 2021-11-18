package com.application.spevents

import com.application.spevents.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class SpEventsApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out SpEventsApp> = DaggerAppComponent.builder().create(this)
}