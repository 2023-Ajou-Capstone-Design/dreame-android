package com.example.dreamixmlversion.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat

fun String.convertBase64ToStr(): String {
    val decodedEmailBytes = Base64.decode(this, Base64.NO_WRAP)
    return String(decodedEmailBytes)
}

fun String.convertStrToBase64(): String {
    return Base64.encodeToString(this.toByteArray(), Base64.NO_WRAP)
}

fun String.convertDateFullToTimestamp(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm")
    val date = sdf.format(this.toLong())
    return date.toString()
}

fun Uri.convertToBitmap(context: Context): Bitmap {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(context.contentResolver, this)
        ImageDecoder.decodeBitmap(source)
    } else {
        MediaStore.Images.Media.getBitmap(context.contentResolver, this)
    }
}

fun Bitmap.encodeToBase64(): String {
    val outputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.NO_WRAP)
}

fun String.decodeForBase64(): Bitmap {
    val decodedBytes = Base64.decode(this, Base64.NO_WRAP)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}