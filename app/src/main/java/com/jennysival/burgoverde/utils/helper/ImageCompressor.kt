package com.jennysival.burgoverde.utils.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object ImageCompressor {

    /**
     * Comprime uma imagem a partir do Uri e retorna o Uri do novo arquivo salvo no cache.
     */
   fun compressImage(context: Context, imageUri: Uri, quality: Int = 50): Uri {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val outputStream = ByteArrayOutputStream()
        originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

        val compressedFile = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
        val fileOutput = FileOutputStream(compressedFile)
        fileOutput.write(outputStream.toByteArray())
        fileOutput.flush()
        fileOutput.close()

        return Uri.fromFile(compressedFile)
    }
}