package com.turtlpass.module.usb

import android.hardware.usb.UsbDevice
import com.felhr.usbserial.UsbSerialInterface

object HardwareConfiguration {
    const val baudRate: Int = 115200
    const val dataBits: Int = UsbSerialInterface.DATA_BITS_8
    const val stopBits: Int = UsbSerialInterface.STOP_BITS_1
    const val parity: Int = UsbSerialInterface.PARITY_NONE
    const val flowControl: Int = UsbSerialInterface.FLOW_CONTROL_OFF
    const val lineFeedDelimiter: String = Delimiter.LF

    object Delimiter {
        const val CR_LF = "\r\n"
        const val LF = "\n"
        const val CR = "\r"
    }

    private const val raspberryPiVendorId: Int = 11914 // HEX: 0x2E8A
    fun isSupported(device: UsbDevice) = device.vendorId == raspberryPiVendorId
}
