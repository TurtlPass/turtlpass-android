package com.turtlpass

import android.app.Application
import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.turtlpass.log.FileLogTree
import com.turtlpass.usb.model.AppState
import com.turtlpass.usb.tracker.UsbStateTracker
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject
import timber.log.Timber

@HiltAndroidApp
class App : Application(), DefaultLifecycleObserver, SingletonImageLoader.Factory  {

    @Inject
    lateinit var imageLoader: ImageLoader
    @Inject
    lateinit var usbStateTracker: UsbStateTracker

    override fun onCreate() {
        super<Application>.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        System.loadLibrary("sqlcipher")
        if (BuildConfig.DEBUG) Timber.plant(FileLogTree(this))
    }

    override fun onStart(owner: LifecycleOwner) {
        AppState.setForeground(true)
    }

    override fun onStop(owner: LifecycleOwner) {
        AppState.setForeground(false)
    }

    override fun newImageLoader(context: Context): ImageLoader {
        return imageLoader
    }
}
