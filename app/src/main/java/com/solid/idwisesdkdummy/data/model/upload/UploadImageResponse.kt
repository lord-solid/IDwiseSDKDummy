package com.solid.idwisesdkdummy.data.model.upload

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UploadImageResponse(
    @SerializedName("data") val data: List<UploadImageData>?,
    @SerializedName("message") val message: String,
    @SerializedName("statusCode") val statusCode: Int?,
    @SerializedName("success") val success: Boolean?
)