package com.application.spevents.di

import com.application.spevents.dialog.MessageDialog
import com.application.spevents.dialog.ProgressDialog
import dagger.Module
import dagger.Provides

@Module
class DialogModule {
    @Provides
    fun providesErrorDialog(): MessageDialog = MessageDialog()

    @Provides
    fun providesProgressDialog(): ProgressDialog = ProgressDialog()
}