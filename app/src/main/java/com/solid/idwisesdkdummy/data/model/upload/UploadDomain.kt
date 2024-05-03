package com.solid.idwisesdkdummy.data.model.upload

import androidx.annotation.Keep

object UploadDomain {
    @Keep data class UploadResponse(
        val urls: List<String>?,
        val message: String?,
        val success: Boolean
    )
}