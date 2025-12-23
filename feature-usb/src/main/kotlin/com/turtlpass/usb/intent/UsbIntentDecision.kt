package com.turtlpass.usb.intent

sealed class UsbIntentDecision {
    object LaunchSelection : UsbIntentDecision()
    object LaunchMain : UsbIntentDecision()
    object Finish : UsbIntentDecision()
}
