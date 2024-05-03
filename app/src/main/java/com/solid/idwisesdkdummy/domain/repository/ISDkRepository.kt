package com.solid.idwisesdkdummy.domain.repository

import com.solid.idwisesdkdummy.data.model.document_capture.DocumentCaptureDto
import com.solid.idwisesdkdummy.data.model.upload.UploadDomain
import okhttp3.MultipartBody

interface ISDkRepository {
    suspend fun uploadImages(files: List<MultipartBody.Part>): UploadDomain.UploadResponse
    suspend fun performOcr(ocrSingleRequest: DocumentCaptureDto.OcrSingleRequest) : DocumentCaptureDomain.OcrResponse
    suspend fun performOcrDouble(ocrDOubleRequest: DocumentCaptureDto.OcrDoubleRequest) : DocumentCaptureDomain.OcrResponse
}