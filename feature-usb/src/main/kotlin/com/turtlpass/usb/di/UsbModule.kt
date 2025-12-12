package com.turtlpass.usb.di

import android.content.Context
import android.hardware.usb.UsbManager
import com.turtlpass.usb.model.UsbUiState
import com.turtlpass.usb.state.UsbStateProvider
import com.turtlpass.usb.state.UsbStateProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow

@Module
@InstallIn(SingletonComponent::class)
object UsbModule {

    @Provides
    fun provideUsbManager(@ApplicationContext context: Context): UsbManager =
        context.getSystemService(Context.USB_SERVICE) as UsbManager

    @Provides
    @Singleton
    fun provideUsbStateFlow(): MutableStateFlow<UsbUiState> =
        MutableStateFlow(UsbUiState())

    @Provides
    @Singleton
    fun provideUsbStateProvider(impl: UsbStateProviderImpl): UsbStateProvider = impl
}
