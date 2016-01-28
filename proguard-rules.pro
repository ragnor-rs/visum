# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Reist/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontshrink
-keepattributes Signature
-dontnote android.net.http.**
-dontnote org.apache.http.**
-dontnote com.google.vending.licensing.**
-dontnote com.android.vending.licensing.**
-dontnote android.support.v7.widget.Toolbar

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8

# Okio
-keep class okio.**
-dontwarn okio.**

# OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontnote okhttp3.internal.Platform