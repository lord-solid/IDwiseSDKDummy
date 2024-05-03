package com.solid.idwisesdkdummy.data.source

import com.solid.idwisesdkdummy.data.model.document_capture.DocumentCaptureDto
import com.solid.idwisesdkdummy.data.model.upload.UploadImageResponse
import okhttp3.MultipartBody

interface ISdkSource {
    suspend fun doImageUpload(file: List<MultipartBody.Part>): UploadImageResponse
    suspend fun performOcr(ocrSingleRequest: DocumentCaptureDto.OcrSingleRequest) : DocumentCaptureDto.OcrResponse
    suspend fun performOcrDouble(ocrDoubleRequest: DocumentCaptureDto.OcrDoubleRequest) : DocumentCaptureDto.OcrResponse
}