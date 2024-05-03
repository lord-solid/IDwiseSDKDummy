package com.solid.idwisesdkdummy.data.model.upload

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UploadImageData(
    @SerializedName("key") val key: String,
    @SerializedName("cachedLocation") val cachedLocation: String,
    @SerializedName("location") val location: String,
)