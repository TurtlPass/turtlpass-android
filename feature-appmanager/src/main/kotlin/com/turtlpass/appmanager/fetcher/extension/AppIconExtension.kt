package com.turtlpass.appmanager.fetcher.extension

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette

fun PackageManager.getApplicationIconOrNull(pkg: String): Drawable? =
    try { getApplicationIcon(pkg) } catch (_: Exception) { null }

fun isDrawableFullyTransparent(drawable: Drawable): Boolean {
    val bmp = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    for (x in 0 until bmp.width) {
        for (y in 0 until bmp.height) {
            if ((bmp.getPixel(x, y) ushr 24) != 0) {
                return false // visible pixel found
            }
        }
    }
    return true
}

fun getAppBackgroundColorPallet(packageManager: PackageManager, packageName: String, fallbackColor: Int): Int {
    val iconDrawable: Drawable = try {
        packageManager.getApplicationIcon(packageName)
    } catch (e: PackageManager.NameNotFoundException) {
        return fallbackColor
    }
    val bitmap: Bitmap = iconDrawable.toBitmap(
        width = 48, // reasonable icon size
        height = 48
    )
    val palette = Palette.from(bitmap).generate()
    return palette.getLightVibrantColor(fallbackColor)
}
