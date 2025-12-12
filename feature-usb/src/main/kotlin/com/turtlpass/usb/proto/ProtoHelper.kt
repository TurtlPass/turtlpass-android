package com.turtlpass.usb.proto

import com.felhr.usbserial.UsbSerialDevice
import timber.log.Timber
import turtlpass.Turtlpass.Command
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder


fun UsbSerialDevice.sendProtoCommand(cmd: Command) {
    val data = cmd.toByteArray()

    // 2-byte little-endian prefix
    val size = data.size
    val prefix = byteArrayOf(
        (size and 0xFF).toByte(),
        ((size shr 8) and 0xFF).toByte()
    )

    val packet = prefix + data

    Timber.i("Sending command: ${data.size} bytes")
    Timber.i("Packet hex: ${packet.joinToString(" ") { "%02X".format(it) }}")

    this.write(packet)
}

/** Serialize a protobuf message with a 2-byte little-endian length prefix. */
fun Command.toLengthPrefixedByteArray(): ByteArray {
    val data = this.toByteArray()
    val sizeBytes = ByteBuffer
        .allocate(2)
        .order(ByteOrder.LITTLE_ENDIAN)
        .putShort(data.size.toShort())
        .array()
    return sizeBytes + data
}

/** Read a complete protobuf message (2-byte length prefix) from a buffer. */
fun extractLengthPrefixedMessage(buffer: ByteArrayOutputStream): ByteArray? {
    val bytes = buffer.toByteArray()
    if (bytes.size < 2) return null // not even the size yet

    val size = ByteBuffer.wrap(bytes, 0, 2)
        .order(ByteOrder.LITTLE_ENDIAN)
        .short
        .toInt() and 0xFFFF // unsigned

    if (bytes.size - 2 < size) return null // incomplete

    val message = bytes.copyOfRange(2, 2 + size)
    // remove processed bytes
    val remaining = bytes.copyOfRange(2 + size, bytes.size)
    buffer.reset()
    buffer.write(remaining)
    return message
}
