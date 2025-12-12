package com.turtlpass.usb.di

import com.turtlpass.usb.usecase.RequestUsbPermissionUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface UsbPermissionReceiverEntryPoint {
    fun requestUsbPermissionUseCase(): RequestUsbPermissionUseCase
}
