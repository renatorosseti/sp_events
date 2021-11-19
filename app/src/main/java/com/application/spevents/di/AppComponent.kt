package com.application.spevents.di

import com.application.spevents.SpEventsApp
import com.application.spevents.main.book.BookEventViewModelModule
import com.application.spevents.main.events.EventsViewModelModule
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton
import dagger.Component

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityBuilder::class,
    FragmentBuilder::class,
    EventsViewModelModule::class,
    BookEventViewModelModule::class])
interface AppComponent : AndroidInjector<SpEventsApp> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<SpEventsApp>()
}