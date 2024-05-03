package com.solid.idwisesdkdummy.data.model.document_capture

import com.google.gson.annotations.SerializedName

object DocumentCaptureDto {

    data class OcrResponse(
        @SerializedName("data") val data: Data,
        @SerializedName("links") val links: List<Any>,
        @SerializedName("message") val message: String,
        @SerializedName("statusCode") val statusCode: Int,
        @SerializedName("success") val success: Boolean
    )

    data class Data(
        @SerializedName("address") val address: Any,
        @SerializedName("bloodGroup") val bloodGroup: Any,
        @SerializedName("businessId") val businessId: String,
        @SerializedName("city") val city: Any,
        @SerializedName("components") val components: List<Any>,
        @SerializedName("country") val country: Any,
        @SerializedName("createdAt") val createdAt: String,
        @SerializedName("dateOfBirth") val dateOfBirth: String,
        @SerializedName("dateOfExpiry") val dateOfExpiry: String?,
        @SerializedName("dateOfIssue") val dateOfIssue: String?,
        @SerializedName("district") val district: Any,
        @SerializedName("districtOfBirth") val districtOfBirth: Any,
        @SerializedName("division") val division: Any,
        @SerializedName("documentDiscriminator") val documentDiscriminator: Any,
        @SerializedName("documentNumber") val documentNumber: String,
        @SerializedName("documentType") val documentType: String,
        @SerializedName("eyeColor") val eyeColor: Any,
        @SerializedName("faceImage") val faceImage: Any,
        @SerializedName("firstName") val firstName: String?,
        @SerializedName("fullDocumentBackImage") val fullDocumentBackImage: Any,
        @SerializedName("fullDocumentFrontImage") val fullDocumentFrontImage: String,
        @SerializedName("fullName") val fullName: String?,
        @SerializedName("gender") val gender: String,
        @SerializedName("height") val height: Any,
        @SerializedName("id") val id: String,
        @SerializedName("isConsent") val isConsent: Boolean,
        @SerializedName("issuingAuthority") val issuingAuthority: String,
        @SerializedName("issuingCountry") val issuingCountry: Any,
        @SerializedName("issuingState") val issuingState: Any,
        @SerializedName("lastModifiedAt") val lastModifiedAt: String,
        @SerializedName("lastName") val lastName: String?,
        @SerializedName("licenseDetails") val licenseDetails: LicenseDetails,
        @SerializedName("maritalStatus") val maritalStatus: Any,
        @SerializedName("metadata") val metadata: Any,
        @SerializedName("method") val method: String,
        @SerializedName("middleName") val middleName: String?,
        @SerializedName("nationality") val nationality: String,
        @SerializedName("notifyWhenIdExpire") val notifyWhenIdExpire: Boolean,
        @SerializedName("occupation") val occupation: Any,
        @SerializedName("placeOfBirth") val placeOfBirth: String,
        @SerializedName("postalCode") val postalCode: Any,
        @SerializedName("rawMRZString") val rawMRZString: String,
        @SerializedName("region") val region: Any,
        @SerializedName("requestedAt") val requestedAt: String,
        @SerializedName("restrictions") val restrictions: Any,
        @SerializedName("road") val road: Any,
        @SerializedName("serialNumber") val serialNumber: Any,
        @SerializedName("signatureImage") val signatureImage: Any,
        @SerializedName("state") val state: Any,
        @SerializedName("status") val status: String,
        @SerializedName("street") val street: Any,
        @SerializedName("trackingId") val trackingId: Any,
        @SerializedName("vehicleRestriction") val vehicleRestriction: Any,
        @SerializedName("weight") val weight: Any
    )

    data class LicenseDetails(
        val conditions: Any,
        val endorsements: Any,
        val vehicleClass: Any
    )

    data class OcrSingleRequest(
        val publicMerchantID: String,
        val documentType: String,
        val frontImgData: String,
    )

    data class OcrDoubleRequest(
        val publicMerchantID: String,
        val documentType: String,
        val countryCode: String?,
        val frontImgData: String,
        val backImgData: String?
    )
}