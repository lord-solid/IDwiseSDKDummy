package com.solid.idwisesdkdummy.data.source

import com.solid.idwisesdkdummy.data.api.SDKService
import com.solid.idwisesdkdummy.data.model.document_capture.DocumentCaptureDto
import com.solid.idwisesdkdummy.data.model.upload.UploadImageResponse
import okhttp3.MultipartBody
import javax.inject.Inject

class SdkDataSource @Inject constructor (private val service: SDKService): ISdkSource {

    override suspend fun doImageUpload(file: List<MultipartBody.Part>): UploadImageResponse {
        return service.uploadImage(file)
    }

    override suspend fun performOcr(ocrSingleRequest: DocumentCaptureDto.OcrSingleRequest): DocumentCaptureDto.OcrResponse {
        return service.performOcr(ocrSingleRequest)
    }

    override suspend fun performOcrDouble(ocrDoubleRequest: DocumentCaptureDto.OcrDoubleRequest): DocumentCaptureDto.OcrResponse {
        return service.performOcrDouble(ocrDoubleRequest)
    }
}