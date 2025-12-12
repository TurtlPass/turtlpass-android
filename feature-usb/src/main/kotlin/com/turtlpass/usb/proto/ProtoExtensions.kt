package com.turtlpass.usb.proto

import com.google.protobuf.ByteString
import turtlpass.Turtlpass
import turtlpass.Turtlpass.Charset
import turtlpass.Turtlpass.Command
import turtlpass.Turtlpass.CommandType


/**
 * Builder extension for creating a [turtlpass.Turtlpass.Command] in a clean DSL style.
 *
 * Example:
 * val cmd = buildCommand(CommandType.GENERATE_PASSWORD) {
 *     genPass = GeneratePasswordParams.newBuilder()
 *         .setEntropy(ByteString.copyFromUtf8("abc"))
 *         .setLength(64)
 *         .build()
 * }
 */
inline fun buildCommand(
    type: CommandType,
    block: Turtlpass.Command.Builder.() -> Unit = {}
): Turtlpass.Command {
    return Command.newBuilder().apply {
        setType(type)
        this.block()
    }.build()
}

/** Convenience builders for specific commands */
fun buildGeneratePasswordCommand(
    entropy: ByteArray,
    length: Int = 100,
    charset: Charset = Charset.LETTERS_NUMBERS
): Turtlpass.Command = buildCommand(CommandType.GENERATE_PASSWORD) {
    genPass = Turtlpass.GeneratePasswordParams.newBuilder()
        .setEntropy(ByteString.copyFrom(entropy))
        .setLength(length)
        .setCharset(charset)
        .build()
}

fun buildGeneratePasswordCommand(
    entropyHex: String,
    length: Int = 100,
    charset: Charset = Charset.LETTERS_NUMBERS
): Command {
    val entropyBytes = entropyHex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    return Command.newBuilder()
        .setType(CommandType.GENERATE_PASSWORD)
        .setGenPass(
            Turtlpass.GeneratePasswordParams.newBuilder()
                .setEntropy(ByteString.copyFrom(entropyBytes))
                .setLength(length)
                .setCharset(charset)
                .build()
        )
        .build()
}

fun buildInitializeSeedCommand(seed: ByteArray): Turtlpass.Command =
    buildCommand(CommandType.INITIALIZE_SEED) {
        initSeed = Turtlpass.InitializeSeedParams.newBuilder()
            .setSeed(ByteString.copyFrom(seed))
            .build()
    }

