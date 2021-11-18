# Application SPEvents
- Android application.

# Important external libraries used in this project:

-> Dagger 2: it is a dependency injection in Android
- @Module and @Provides: define classes and methods which provide dependencies
- @Inject: request dependencies. Can be used on a constructor, a field, or a method
- @Component: enable selected modules and used for performing dependency injection

-> Retrofit: it is a HTTP client for Android. It is possible to use RXJava Observables, Single and Flowables.

-> RxJava2: it is a library for composing asynchronous and event-based programs by using observable sequences.

-> Glide: it is a popular android library for image downloading and caching.

-> Mockk: builds proxies for mocked classes for testing.

# Important architecture patterns used in this project:

-> MVVM - stands for Model, View, ViewModel.
- Model: holds the data of the application. In the project it exposes the data to the ViewModel through the repository.
- View: It represents the UI of the application devoid of any Application Logic. It observes the ViewModel.
- ViewModel: it is responsible for transforming the data from the Model.

-> Dependency Injection - used through Dagger2. It allows the creation of dependent objects outside of a class and provides those objects to a class through different ways.
 It enables more code reuse, facilitates refactoring and testing.

