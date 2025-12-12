package com.turtlpass.usb.hardware

import android.hardware.usb.UsbDevice
import com.felhr.usbserial.UsbSerialInterface

object HardwareConfiguration {
    const val baudRate: Int = 115200
    const val dataBits: Int = UsbSerialInterface.DATA_BITS_8
    const val stopBits: Int = UsbSerialInterface.STOP_BITS_1
    const val parity: Int = UsbSerialInterface.PARITY_NONE
    const val flowControl: Int = UsbSerialInterface.FLOW_CONTROL_OFF

    // Manufacturer and product strings
    private const val MANUFACTURER_NAME = "TurtlPass"
    private const val PRODUCT_NAME = "TurtlPass Firmware"

    // USB Vendor and Product ID of TurtlPass Firmware
    private const val TURTLPASS_VENDOR_ID = 0x1209
    private const val TURTLPASS_PRODUCT_ID = 0xFA55

    // Check if this USB device matches the TurtlPass firmware IDs
    fun isSupported(device: UsbDevice): Boolean {
        return device.vendorId == TURTLPASS_VENDOR_ID &&
                device.productId == TURTLPASS_PRODUCT_ID
    }

    fun describeDevice(device: UsbDevice): String {
        return "Device: ${device.deviceName}, " +
                "VID=0x${device.vendorId.toString(16)}, " +
                "PID=0x${device.productId.toString(16)}, " +
                "Manufacturer=${device.manufacturerName}, " +
                "Product=${device.productName}"
    }
}