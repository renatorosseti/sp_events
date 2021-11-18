package com.application.spevents.main.details

import com.application.spevents.main.EventsRepository
import com.application.spevents.util.NetworkUtil
import dagger.Module
import dagger.Provides

@Module
class EventDetailsViewModelModule {
    @Provides
    fun providesDetailsViewModel(
        repository: EventsRepository,
        networkUtil: NetworkUtil
    ) = EventDetailsViewModel(repository, networkUtil)
}