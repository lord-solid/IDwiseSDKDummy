package com.solid.idwisesdkdummy.domain.mapper

import com.solid.idwisesdkdummy.data.model.upload.UploadDomain
import com.solid.idwisesdkdummy.data.model.upload.UploadImageResponse

fun UploadImageResponse.map(): UploadDomain.UploadResponse {
    val fileUrls = data?.map { it.location }
    return UploadDomain.UploadResponse(
        urls = fileUrls,
        message = message,
        success =  success == true
    )
}