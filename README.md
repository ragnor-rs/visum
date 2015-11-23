Android reactive MVP stack

The library encourages developers to use Android clean architecture pattern (http://fernandocejas.com/2015/07/18/architecting-android-the-evolution/).

NB since this this app is using MVP pattern term view in documentation is relates to V in MVP (Model, View, Presenter).

There are two Dagger 2 scopes. One is common scope which is implemented by creating component in BaseApplication class. And the other one is handled manually by creating and destroying components for current view via ComponentCache.
