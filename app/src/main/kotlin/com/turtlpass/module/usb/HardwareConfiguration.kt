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

    object RaspberryPiPico {
        const val vendorId: Int = 11914
        const val productId: Int = 32778
    }

    fun isSupported(device: UsbDevice): Boolean {
        with(RaspberryPiPico) {
            return device.vendorId == vendorId && device.productId == productId
        }
    }
}
