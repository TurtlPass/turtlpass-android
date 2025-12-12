# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Print proper explanation of the R8 Shrinking process
-verbose

# Uncomment this to preserve the line number information for debugging stack traces
-keepattributes LineNumberTable,SourceFile

# If you keep the line number information, uncomment to hide the original source file name
-renamesourcefileattribute SourceFile

# Moving everything to the default package
-repackageclasses

# don't ignore any non-public library classes
-dontskipnonpubliclibraryclasses

# It helps in preserving some attributes that may be required for reflection.
# For example, the Exceptions, InnerClasses, and Signature attributes are used when processing a library.
# SourceFile and LineNumberTable attributes for producing obfuscated stack traces with source file name
# and line number of method. Annotation to keep all the annotations used.
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod

# preserve R class
-keepclassmembers class **.R$* {
    public static <fields>;
}

# preserve enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# preserve both class and its member method names after the shrinking process is over
-keepclasseswithmembernames class * {
    native <methods>;
}

# preserve Creator field in each class which implements Parcelable to be accessible in case of reflection
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# preserve @Keep annotation class named Keep in the respective package
-keep class android.support.annotation.Keep
-keep class androidx.annotation.Keep

-keep @android.support.annotation.Keep class * {*;}
-keep @androidx.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}

# This rule specifies not to print any details for these classes as they are duplicated
# between android.jar and org.apache.http.legacy.jar
-dontnote org.apache.http.**
-dontnote android.net.http.**

# This rule specifies not to print any details for these classes as they are duplicated
# between android.jar and core-lambda-stubs.jar
-dontnote java.lang.invoke.**

# JSR 305 annotations are for embedding nullability information
-dontwarn javax.annotation.**

# OkHttp platform used only on JVM and when Conscrypt and other security providers are available
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Keep class names of Hilt injected ViewModels since their name are used as a multibinding map key.
-keepnames @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel

### PROTOBUF ###
# Keep all protobuf messages and prevent obfuscation of their fields
-keep class turtlpass.** { *; }
-keep class com.turtlpass.usb.proto.** { *; }
# Protobuf runtime
-keep class com.google.protobuf.** { *; }
# Keep GeneratedMessageLite subclasses and builder fields
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite {
    <fields>;
    <methods>;
}
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite$Builder {
    <fields>;
    <methods>;
}
