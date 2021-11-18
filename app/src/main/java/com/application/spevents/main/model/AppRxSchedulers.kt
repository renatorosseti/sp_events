package com.application.spevents.main.model

import io.reactivex.Scheduler

data class AppRxSchedulers(
        val database: Scheduler,
        val disk: Scheduler,
        val network: Scheduler,
        val main: Scheduler
)