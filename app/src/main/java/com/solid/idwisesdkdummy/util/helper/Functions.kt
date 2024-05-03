package com.solid.idwisesdkdummy.util.helper

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

fun createMultipart(file: File, mimetype: String = "image/png"): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        "files",
        file.name,
        file.asRequestBody(mimetype.toMediaTypeOrNull())
    )
}

fun formatDate(inputDate: String): String? {
    try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("d', 'MMMM yyyy", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return date?.let { outputFormat.format(it) }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "" // Return empty string on error
}