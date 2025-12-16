package com.turtlpass

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.turtlpass.log.FileLogTree
import com.turtlpass.usb.tracker.UsbStateTracker
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject
import timber.log.Timber

@HiltAndroidApp
class App : Application(), SingletonImageLoader.Factory  {

    @Inject
    lateinit var imageLoader: ImageLoader
    @Inject
    lateinit var usbStateTracker: UsbStateTracker

    override fun onCreate() {
        super.onCreate()
        System.loadLibrary("sqlcipher")
        if (BuildConfig.DEBUG) Timber.plant(FileLogTree(this))
    }

    override fun newImageLoader(context: Context): ImageLoader {
        return imageLoader
    }
}
