package com.solid.idwisesdkdummy.data.model.upload

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

object TaskDto {
    @Keep
    data class GenericResponse(
        @SerializedName("success") val success: Boolean?,
        @SerializedName("statusCode", alternate = ["status_code"]) val statusCode: Int?,
        @SerializedName("message") val message: String?,
    )
}