package com.solid.idwisesdkdummy.domain.repository

import com.solid.idwisesdkdummy.data.model.document_capture.DocumentCaptureDto
import com.solid.idwisesdkdummy.data.model.upload.UploadDomain
import com.solid.idwisesdkdummy.data.source.ISdkSource
import com.solid.idwisesdkdummy.domain.mapper.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

class SDKRepositoryImpl @Inject constructor(
    private val source: ISdkSource,
    private val dispatcher: CoroutineDispatcher,
) : ISDkRepository {

    override suspend fun uploadImages(files: List<MultipartBody.Part>): UploadDomain.UploadResponse = withContext(dispatcher) {
        source.doImageUpload(files).map()
    }

    override suspend fun performOcr(ocrSingleRequest: DocumentCaptureDto.OcrSingleRequest): DocumentCaptureDomain.OcrResponse = withContext(dispatcher) {
        source.performOcr(ocrSingleRequest).map()
    }

    override suspend fun performOcrDouble(ocrDoubleRequest: DocumentCaptureDto.OcrDoubleRequest): DocumentCaptureDomain.OcrResponse = withContext(dispatcher) {
        source.performOcrDouble(ocrDoubleRequest).map()
    }
}