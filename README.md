Visum - Android reactive MVP stack

The library encourages developers to use Android Clean MVP architecture pattern (http://fernandocejas.com/2015/07/18/architecting-android-the-evolution/).

There are two Dagger 2 scopes. One is common scope which is implemented by creating a component in Application class. And the other one is handled manually by creating and destroying components for current view via ComponentCache.

You can find an example of Visum usage in demo submodule
