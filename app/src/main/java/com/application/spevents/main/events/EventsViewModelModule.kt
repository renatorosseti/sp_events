package com.application.spevents.main.events

import com.application.spevents.repository.EventsRepository
import com.application.spevents.util.NetworkUtil
import dagger.Module
import dagger.Provides

@Module
class EventsViewModelModule {
    @Provides
    fun providesEventsViewModel(
        repository: EventsRepository,
        networkUtil: NetworkUtil
    ) = EventsViewModel(repository, networkUtil)
}