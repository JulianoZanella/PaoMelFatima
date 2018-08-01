package br.com.julianozanella.doces.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

fun Bitmap.toBase64(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val imageBytes = stream.toByteArray()
    return Base64.encodeToString(imageBytes, Base64.DEFAULT).toByteArray()
}

fun ByteArray.toImage(): Bitmap {
    val data = android.util.Base64.decode(this, android.util.Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(data, 0, data.size)
}