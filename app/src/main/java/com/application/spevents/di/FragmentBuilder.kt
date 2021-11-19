package com.application.spevents.di

import com.application.spevents.main.book.BookEventFragment
import com.application.spevents.main.book.BookProfilesFragment
import com.application.spevents.main.details.EventDetailsFragment
import com.application.spevents.main.events.EventsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {
    @ContributesAndroidInjector(modules = [AppModule::class])
    abstract fun contributeEventListFragment(): EventsFragment

    @ContributesAndroidInjector(modules = [AppModule::class])
    abstract fun contributeEventDetailsFragment(): EventDetailsFragment

    @ContributesAndroidInjector(modules = [AppModule::class])
    abstract fun contributeCheckInFragment(): BookEventFragment

    @ContributesAndroidInjector(modules = [AppModule::class])
    abstract fun contributeBookProfilesFragment(): BookProfilesFragment
}