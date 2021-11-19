package com.application.spevents.main.book

import com.application.spevents.main.EventsRepository
import com.application.spevents.util.NetworkUtil
import dagger.Module
import dagger.Provides

@Module
class BookEventViewModelModule {
    @Provides
    fun providesDetailsViewModel(
        repository: EventsRepository,
        networkUtil: NetworkUtil
    ) = BookEventViewModel(repository, networkUtil)
}