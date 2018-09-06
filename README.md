# Visum
[![Build Status](https://travis-ci.org/ragnor-rs/visum.svg?branch=develop)](https://travis-ci.org/ragnor-rs/visum)
[![Release](https://jitpack.io/v/ragnor-rs/visum.svg)](https://jitpack.io/#ragnor-rs/visum)

Android reactive MVP stack.

The library encourages developers to use [Android Clean MVP architecture pattern](http://fernandocejas.com/2015/07/18/architecting-android-the-evolution/).

There are two Dagger 2 scopes. One is common scope which is implemented by creating a component in Application class. And the other one is handled manually by creating and destroying components for current view via ComponentCache.

You can find an example of Visum usage in demo submodule.

Compatibility
-------------

This library is compatible from API 16 (4.1)


Download
--------

Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

Add the dependency

```groovy
dependencies {
    implementation 'com.github.ragnor-rs:visum:bb0bc22489'
}
```

License
=======

    Copyright 2016 Renat Sarymsakov.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
