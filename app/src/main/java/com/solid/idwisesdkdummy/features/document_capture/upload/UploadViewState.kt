package com.solid.idwisesdkdummy.features.document_capture.upload

import com.solid.idwisesdkdummy.data.model.upload.UploadImageResponse
import java.io.File

sealed class UploadViewState {
    class Success(val message: Int, val uploadResponse: UploadImageResponse? = null, val file: File? = null, val uploadType: Int = UploadType.imageUpload) : UploadViewState()
    class Failure(val message: Int, val errorMessage: String = "", val file: File? = null, val uploadType: Int = UploadType.imageUpload) : UploadViewState()
    class Loading(val message: Int? = null) : UploadViewState()
    object Empty : UploadViewState()

    companion object {
        object UploadType {
            const val imageUpload: Int = 0x01
            const val signatureUpload: Int = 0x02
        }
    }
}