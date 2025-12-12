package com.turtlpass.log

import android.content.Context
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

class FileLogTree @Inject constructor(private val context: Context) : Timber.DebugTree() {

    private val writeFile = AtomicReference<File>()
    private val accumulatedLogs = ConcurrentHashMap<String, String>()

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println(throwable)
    }
    private val coroutineScope: CoroutineScope = CoroutineScope(
        SupervisorJob() + exceptionHandler
    )


    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
        try {
            accumulatedLogs[convertLongToTime(
                System.currentTimeMillis()
            )] = message
            createLogFile()

        } catch (e: IOException) {
            Timber.e("Error while logging into file: $e")
        }
    }

    private fun createLogFile() =
        coroutineScope.launch {
            writeFile.lazySet(context.createFile(fileName = "logs.txt"))
            writeToLogFile()
        }

    private suspend fun writeToLogFile() {
        val result = runCatching {
            writeFile.get().bufferedWriter().use {
                it.write(accumulatedLogs.toString())
            }
        }
        if (result.isFailure) {
            result.exceptionOrNull()?.printStackTrace()
        }
    }

    private fun convertLongToTime(long: Long): String {
        val date = Date(long)
        val format = SimpleDateFormat(ANDROID_LOG_TIME_FORMAT, Locale.UK)
        return format.format(date)
    }

    private fun Context.createFile(fileName: String): File {
        val file = File(externalCacheDir, fileName)
        if (file.exists()) {
            file.delete()
        }
        return file
    }

    companion object {
        private const val ANDROID_LOG_TIME_FORMAT = "dd-MM-yy_kk:mm:ss.SSS"
    }
}