package com.solid.idwisesdkdummy

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.solid.idwisesdkdummy.MainActivity.Companion.CAPTURE_DOUBLE
import com.solid.idwisesdkdummy.MainActivity.Companion.CAPTURE_SINGLE
import com.solid.idwisesdkdummy.databinding.ActivityImageDisplayBinding
import com.solid.idwisesdkdummy.util.COUNTRY
import com.solid.idwisesdkdummy.util.DATE_OF_BIRTH
import com.solid.idwisesdkdummy.util.DISPLAY_VALUE
import com.solid.idwisesdkdummy.util.DOCUMENT_TYPE
import com.solid.idwisesdkdummy.util.DOUBLE_URI
import com.solid.idwisesdkdummy.util.EXPIRY_DATE
import com.solid.idwisesdkdummy.util.FORMAT
import com.solid.idwisesdkdummy.util.GENDER
import com.solid.idwisesdkdummy.util.ID_NUMBER
import com.solid.idwisesdkdummy.util.IMAGE_URL_ONE
import com.solid.idwisesdkdummy.util.IMAGE_URL_TWO
import com.solid.idwisesdkdummy.util.ISSUE_DATE
import com.solid.idwisesdkdummy.util.NAME
import dagger.hilt.android.AndroidEntryPoint
import java.io.InputStream

@AndroidEntryPoint
class ImageDisplayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageDisplayBinding

    companion object {
        const val EXTRA_IMAGE_PATH = "extra_image_path"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageOne = intent.getStringExtra(MainActivity.EXTRA_CAPTURE_URI_ONE)
        val imageTwo = intent.getStringExtra(MainActivity.EXTRA_CAPTURE_URI_TWO)
        val captureMode = intent.getIntExtra(MainActivity.EXTRA_CAPTURE_MODE, -1)
        val isDoubleUri = intent.getBooleanExtra(DOUBLE_URI, false)

        if (captureMode == CAPTURE_SINGLE) {
            val resultMap = getMappedData(imageOne)
            setResultViews(resultMap, captureMode)
        }else if (captureMode == CAPTURE_DOUBLE){
            val resultMap = getMappedData(imageOne, imageTwo)
            setResultViews(resultMap, captureMode, isDoubleUri)
        }

//            if (!isPicked) processUriForBarcode(Uri.parse(imageTwo))

    }

    private fun getMappedData(imageOne: String?, imageTwo: String? = null): MutableMap<String, String> {
        val resultMap = mutableMapOf<String, String>()
        resultMap[NAME] = intent.getStringExtra(NAME) ?: ""
        resultMap[DATE_OF_BIRTH] = intent.getStringExtra(DATE_OF_BIRTH) ?: ""
        resultMap[ID_NUMBER] = intent.getStringExtra(ID_NUMBER) ?: ""
        resultMap[GENDER] = intent.getStringExtra(GENDER) ?: ""
        resultMap[COUNTRY] = intent.getStringExtra(COUNTRY) ?: ""
        resultMap[ISSUE_DATE] = intent.getStringExtra(ISSUE_DATE) ?: ""
        resultMap[EXPIRY_DATE] = intent.getStringExtra(EXPIRY_DATE) ?: ""
        resultMap[DOCUMENT_TYPE] = intent.getStringExtra(DOCUMENT_TYPE) ?: ""
        resultMap[NAME] = intent.getStringExtra(NAME) ?: ""
        resultMap[IMAGE_URL_ONE] = imageOne ?: ""
        resultMap[IMAGE_URL_TWO] = imageTwo ?: ""
        return resultMap
    }

    private fun processUriForBarcode(uri: Uri) {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        inputStream?.let {
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            // Create an InputImage object from the Bitmap
            val inputImage = InputImage.fromBitmap(bitmap, 0)

            // Perform barcode scanning
            processBarcode(inputImage, uri)
        }
    }

    private fun formatDate(inputDate: String): String {
        // Check if the inputDate is valid (length should be 7)
        if (inputDate.length != 8) {
            return "Invalid date format"
        }

        // Extract day, month, and year parts from the inputDate string
        val day = inputDate.substring(0, 2)
        val month = inputDate.substring(2, 4)
        val year = inputDate.substring(4)

        // Construct the formatted date string
        return "$day-$month-$year"
    }

    private fun processBarcode(inputImage: InputImage, uri: Uri) {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_PDF417,
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC
            )
            .build()
        val scanner = BarcodeScanning.getClient(options)

        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                // Task completed successfully

                val sb = StringBuilder()
                val resultMap = mutableMapOf<String, String>()
                for (barcode in barcodes) {
                    println(
                        """
                        ========================================================================
                        Bounds: ${barcode.boundingBox}
                        Corners: ${barcode.cornerPoints}
                        Raw value: ${barcode.rawValue}
                        ValueType: ${barcode.valueType}
                        Url: ${barcode.url.toString()}
                        Wifi SSID: ${barcode.wifi?.ssid}
                        Wifi password: ${barcode.wifi?.password}
                        Wifi encryption type: ${barcode.wifi?.encryptionType}
                        Calendar event start: ${barcode.calendarEvent?.start?.rawValue}
                        Calendar event end: ${barcode.calendarEvent?.end?.rawValue}
                        Calendar event description: ${barcode.calendarEvent?.description}
                        Contact Info: ${barcode.contactInfo.toString()}
                        Display value: 
                        ${barcode.displayValue}
                        Driver license: ${barcode.driverLicense.toString()}
                              First name: ${barcode.driverLicense?.firstName}
                              Middle name: ${barcode.driverLicense?.middleName}
                              Last name: ${barcode.driverLicense?.lastName}
                              Address City: ${barcode.driverLicense?.addressCity}
                              Address state: ${barcode.driverLicense?.addressState}
                              Address Zip: ${barcode.driverLicense?.addressZip}
                              Birth Date: ${barcode.driverLicense?.birthDate}
                              Document type: ${barcode.driverLicense?.documentType.toString()}
                              Expiry Date: ${barcode.driverLicense?.expiryDate}
                              Gender: ${barcode.driverLicense?.gender}
                              Issuance date: ${barcode.driverLicense?.issueDate}
                              Issuing country: ${barcode.driverLicense?.issuingCountry}
                              License Number: ${barcode.driverLicense?.licenseNumber}
                        Email: ${barcode.email}
                        Format: ${barcode.format}
                        Geo-point(Lat-Long): ${barcode.geoPoint?.lng}  ${barcode.geoPoint?.lat} 
                        Phone: ${barcode.phone}
                        SMS Message: ${barcode.sms?.message}
                        SMS Phone number: ${barcode.sms?.phoneNumber}
                        Raw bytes: ${barcode.rawBytes?.decodeToString()}
                        ========================================================================
                    """.trimIndent()
                    )

                    resultMap[NAME] =
                        "${barcode.driverLicense?.firstName} ${barcode.driverLicense?.middleName} ${barcode.driverLicense?.lastName}"
                    resultMap[DATE_OF_BIRTH] = formatDate("${barcode.driverLicense?.birthDate}")
                    resultMap[ID_NUMBER] = "${barcode.driverLicense?.licenseNumber}"
                    resultMap[GENDER] =
                        if (barcode.driverLicense?.gender == "1") "MALE" else "FEMALE"
                    resultMap[COUNTRY] = "${barcode.driverLicense?.issuingCountry}"
                    resultMap[ISSUE_DATE] = formatDate("${barcode.driverLicense?.issueDate}")
                    resultMap[EXPIRY_DATE] = formatDate("${barcode.driverLicense?.expiryDate}")
                    resultMap[DOCUMENT_TYPE] = barcode.driverLicense?.documentType.toString()
                    resultMap[FORMAT] = if (barcode.format == 2048) "FORMAT_PDF417" else ""
                    resultMap[DISPLAY_VALUE] = "${barcode.displayValue}"
                }
                binding.barcodeResult.text = resultMap["format"]

//                setResultViews(resultMap, uri, false)
            }
            .addOnFailureListener {
                // Task failed with an exception
                Toast.makeText(this, "Barcode scanning failed!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setResultViews(
        resultMap: MutableMap<String, String>,
        captureMode: Int,
        isDoubleUri: Boolean? = null
    ) {
        with(binding) {
            idText.text = resultMap[ID_NUMBER]
            nameText.text = resultMap[NAME]
            dobText.text = resultMap[DATE_OF_BIRTH]
            countryText.text = resultMap[COUNTRY]
            genderText.text = resultMap[GENDER]
            issueText.text = resultMap[ISSUE_DATE]
            expiryText.text = resultMap[EXPIRY_DATE]
            typeText.text = resultMap[DOCUMENT_TYPE]

            if (captureMode == CAPTURE_SINGLE) {
                Glide.with(this@ImageDisplayActivity).load(resultMap[IMAGE_URL_ONE])
                    .into(binding.imageOne)
            } else {
                if(isDoubleUri != null && isDoubleUri == true){
                    binding.imageOne.setImageURI(Uri.parse(resultMap[IMAGE_URL_ONE]))
                    binding.imageTwo.setImageURI(Uri.parse(resultMap[IMAGE_URL_TWO]))
                }else{
                    Glide.with(this@ImageDisplayActivity).load(resultMap[IMAGE_URL_ONE])
                        .into(binding.imageOne)
                    Glide.with(this@ImageDisplayActivity).load(resultMap[IMAGE_URL_TWO])
                        .into(binding.imageTwo)
                }
            }
        }
    }
}