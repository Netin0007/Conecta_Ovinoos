package com.example.conectaovinos.utils


import android.content.Context
import android.net.Uri
import java.io.File
import java.util.UUID

fun copyImageToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val input = context.contentResolver.openInputStream(uri) ?: return null

        val dir = File(context.filesDir, "animal_photos")
        if (!dir.exists()) dir.mkdirs()

        val file = File(dir, "${UUID.randomUUID()}.jpg")

        input.use { ins ->
            file.outputStream().use { outs ->
                ins.copyTo(outs)
            }
        }

        file.toURI().toString()
    } catch (_: Exception) {
        null
    }
}
