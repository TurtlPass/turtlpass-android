package com.turtlpass.module.usb.usecase

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import com.felhr.usbserial.UsbSerialDevice
import com.felhr.utils.ProtocolBuffer
import com.turtlpass.di.IoDispatcher
import com.turtlpass.module.chooser.UsbWriteResult
import com.turtlpass.module.usb.HardwareConfiguration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class WriteUsbSerialUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val usbManager: UsbManager
) {
    private var connection: UsbDeviceConnection? = null
    private var usbSerialDevice: UsbSerialDevice? = null
    private val protocolBuffer = ProtocolBuffer(ProtocolBuffer.TEXT)

    suspend operator fun invoke(usbDevice: UsbDevice, hash: String): Flow<UsbWriteResult> {
        return callbackFlow {
            try {
                if (HardwareConfiguration.isSupported(usbDevice)) {
                    connection = usbManager.openDevice(usbDevice)
                    usbSerialDevice = UsbSerialDevice.createUsbSerialDevice(usbDevice, connection)
                    usbSerialDevice?.let { serialDevice ->
                        if (serialDevice.open()) {
                            with(HardwareConfiguration) {
                                protocolBuffer.setDelimiter(lineFeedDelimiter)
                                serialDevice.apply {
                                    setBaudRate(baudRate)
                                    setDataBits(dataBits)
                                    setStopBits(stopBits)
                                    setParity(parity)
                                    setFlowControl(flowControl)
                                    read { bytes ->
                                        protocolBuffer.appendData(bytes)
                                        while (protocolBuffer.hasMoreCommands()) {
                                            val command = protocolBuffer.nextTextCommand()
                                            if (command.startsWith("OK")) {
                                                trySend(UsbWriteResult.Success)
                                            } else {
                                                Timber.e("Invalid command: $command")
                                                trySend(UsbWriteResult.Error)
                                            }
                                        }
                                    }
                                    write("/$hash\n".toByteArray(charset("UTF-8")))
                                    setRTS(false); // Not Raised
                                    setDTR(true); // Raised
                                }
                            }
                        } else {
                            trySend(UsbWriteResult.Error)
                            close(Exception("Error opening USB serial device"))
                        }
                    } ?: run {
                        trySend(UsbWriteResult.Error)
                        close(Exception("USB device not supported"))
                    }
                } else {
                    trySend(UsbWriteResult.Error)
                    Timber.e("Usb device not supported: $usbDevice")
                }
            } catch (e: Exception) {
                trySend(UsbWriteResult.Error)
                close(e)
            }
            awaitClose {
                try {
                    usbSerialDevice?.let {
                        it.close()
                        usbSerialDevice = null
                    }
                } catch (ignore: Exception) {}
                try {
                    connection?.let {
                        it.close()
                        connection = null
                    }
                } catch (ignore: Exception) {}
            }
        }.catch {
            emit(UsbWriteResult.Error)
        }.flowOn(dispatcher)
    }
}
