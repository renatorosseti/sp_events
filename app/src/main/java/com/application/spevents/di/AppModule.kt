package com.application.spevents.di

import dagger.Module

@Module(includes = [
    SchedulerModule::class,
    NetworkModule::class,
    DialogModule::class])
class AppModule