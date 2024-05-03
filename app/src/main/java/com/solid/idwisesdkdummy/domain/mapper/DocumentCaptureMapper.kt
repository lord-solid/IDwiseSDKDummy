package com.solid.idwisesdkdummy.domain.mapper

import com.solid.idwisesdkdummy.data.model.document_capture.DocumentCaptureDto
import com.solid.idwisesdkdummy.domain.repository.DocumentCaptureDomain
import com.solid.idwisesdkdummy.util.helper.formatDate

fun DocumentCaptureDto.OcrResponse.map(): DocumentCaptureDomain.OcrResponse {
    return DocumentCaptureDomain.OcrResponse(
        success = this.success,
        message = this.message,
        documentData = DocumentCaptureDomain.DocumentData(
            fullName = this.data.fullName ?: "${this.data.firstName ?: ""} ${this.data.middleName ?: ""} ${this.data.lastName ?: ""}",
            dateOfBirth = formatDate(this.data.dateOfBirth) ?: "",
            gender = this.data.gender ?: "",
            rmzString = this.data.rawMRZString ?: "",
            issuingAuthority = this.data.issuingAuthority ?: "",
            placeOfBirth = this.data.placeOfBirth ?: "",
            issuanceDate = this.data.dateOfIssue?.let { formatDate(it) } ?: "",
            expiryDate = this.data.dateOfExpiry?.let { formatDate(it) } ?: "",
            nationality = this.data.nationality ?: "",
            documentType = this.data.documentType ?: "",
            documentNumber = this.data.documentNumber ?: ""
        )
    )
}