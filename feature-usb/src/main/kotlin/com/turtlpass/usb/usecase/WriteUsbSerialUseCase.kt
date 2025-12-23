package com.turtlpass.usb.usecase

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import com.felhr.usbserial.UsbSerialDevice
import com.google.protobuf.InvalidProtocolBufferException
import com.turtlpass.di.IoDispatcher
import com.turtlpass.usb.hardware.HardwareConfiguration
import com.turtlpass.usb.model.UsbWriteResult
import com.turtlpass.usb.proto.buildGeneratePasswordCommand
import com.turtlpass.usb.proto.extractLengthPrefixedMessage
import com.turtlpass.usb.proto.sendProtoCommand
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import turtlpass.Turtlpass
import turtlpass.Turtlpass.Response
import java.io.ByteArrayOutputStream
import javax.inject.Inject


class WriteUsbSerialUseCase @Inject constructor(
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val usbManager: UsbManager
) {
    private var connection: UsbDeviceConnection? = null
    private var usbSerialDevice: UsbSerialDevice? = null

    /**
     * Send a protobuf command with 2-byte length prefix over USB serial.
     */
    suspend operator fun invoke(usbDevice: UsbDevice, hash: String): Flow<UsbWriteResult> {
        return callbackFlow {
            try {
                Timber.d("Detected device: ${HardwareConfiguration.describeDevice(usbDevice)}")

                if (!HardwareConfiguration.isSupported(usbDevice)) {
                    Timber.e("Unsupported USB device: $usbDevice")
                    trySend(UsbWriteResult.Error(Turtlpass.ErrorCode.UNRECOGNIZED))
                    close()
                    return@callbackFlow
                }

                connection = usbManager.openDevice(usbDevice)
                usbSerialDevice = UsbSerialDevice.createUsbSerialDevice(usbDevice, connection)

                val serial = usbSerialDevice ?: run {
                    trySend(UsbWriteResult.Error(Turtlpass.ErrorCode.UNRECOGNIZED))
                    close(Exception("Failed to create serial device"))
                    return@callbackFlow
                }

                if (!serial.open()) {
                    trySend(UsbWriteResult.Error(Turtlpass.ErrorCode.UNRECOGNIZED))
                    close(Exception("Error opening USB serial device"))
                    return@callbackFlow
                }

                // Configure serial connection
                with(HardwareConfiguration) {
                    serial.setBaudRate(baudRate)
                    serial.setDataBits(dataBits)
                    serial.setStopBits(stopBits)
                    serial.setParity(parity)
                    serial.setFlowControl(flowControl)
                }

                // Build and send protobuf command
                val command = buildGeneratePasswordCommand(entropyHex = hash)
                serial.sendProtoCommand(command)
                Timber.i("Sent Command: ${command.type}")

                // Read and decode protobuf responses
                val buffer = ByteArrayOutputStream()

                serial.read { incoming ->
                    buffer.write(incoming)

                    // Try extracting messages until no full one remains
                    while (true) {
                        val messageBytes = extractLengthPrefixedMessage(buffer) ?: break
                        try {
                            val response = Response.parseFrom(messageBytes)
                            Timber.i("Received response: success=${response.success}, error=${response.error}")

                            if (response.success) {
                                trySend(UsbWriteResult.Success)
                            } else {
                                trySend(UsbWriteResult.Error(response.error))
                            }
                        } catch (e: InvalidProtocolBufferException) {
                            Timber.e(e, "Failed to parse protobuf response")
                        }
                    }
                }

            } catch (e: Exception) {
                Timber.e(e, "USB write error")
                trySend(UsbWriteResult.Error(Turtlpass.ErrorCode.UNRECOGNIZED))
                close(e)
            }

            awaitClose {
                try {
                    usbSerialDevice?.close()
                    connection?.close()
                } catch (_: Exception) {}
            }
        }.catch {
            emit(UsbWriteResult.Error(Turtlpass.ErrorCode.UNRECOGNIZED))
        }.flowOn(dispatcher)
    }
}
