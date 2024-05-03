package com.solid.idwisesdkdummy.data.api

import com.solid.idwisesdkdummy.data.model.document_capture.DocumentCaptureDto
import com.solid.idwisesdkdummy.data.model.upload.UploadImageResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SDKService {

    @Multipart
    @POST("https://cdn.svc.youverify.co/v1/uploads/media")
    suspend fun uploadImage(@Part files: List<MultipartBody.Part>) : UploadImageResponse

    @POST("https://identity.dev.svc.youverify.co/v1/sdk/identity/document-capture/ocr")
    suspend fun performOcr(@Body ocrSingleRequest: DocumentCaptureDto.OcrSingleRequest): DocumentCaptureDto.OcrResponse

    @POST("https://identity.dev.svc.youverify.co/v1/sdk/identity/document-capture/ocr")
    suspend fun performOcrDouble(@Body ocrDoubleRequest: DocumentCaptureDto.OcrDoubleRequest): DocumentCaptureDto.OcrResponse
}