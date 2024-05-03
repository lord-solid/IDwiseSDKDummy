package com.solid.idwisesdkdummy.domain.repository

import androidx.annotation.Keep

object DocumentCaptureDomain {

    @Keep data class OcrResponse(
        val success: Boolean,
        val message: String,
        val documentData: DocumentData
    )

    @Keep data class DocumentData(
        val fullName: String?,
        val dateOfBirth: String?,
        val gender: String?,
        val rmzString: String?,
        val issuingAuthority: String?,
        val placeOfBirth: String?,
        val issuanceDate: String?,
        val expiryDate: String?,
        val nationality: String?,
        val documentType: String?,
        val documentNumber: String?,
    )
}