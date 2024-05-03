package com.solid.idwisesdkdummy

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.preference.PreferenceManager
import com.afollestad.materialdialogs.MaterialDialog
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.solid.idwisesdkdummy.data.functional.ResultState
import com.solid.idwisesdkdummy.data.model.document_capture.DocumentCaptureDto
import com.solid.idwisesdkdummy.databinding.ActivityMainBinding
import com.solid.idwisesdkdummy.databinding.LayoutCountryBinding
import com.solid.idwisesdkdummy.features.document_capture.upload.UploadViewModel
import com.solid.idwisesdkdummy.features.document_capture.ocr.OcrViewModel
import com.solid.idwisesdkdummy.util.COUNTRY
import com.solid.idwisesdkdummy.util.DATE_OF_BIRTH
import com.solid.idwisesdkdummy.util.DOCUMENT_TYPE
import com.solid.idwisesdkdummy.util.DOUBLE_URI
import com.solid.idwisesdkdummy.util.EXPIRY_DATE
import com.solid.idwisesdkdummy.util.GENDER
import com.solid.idwisesdkdummy.util.ID_NUMBER
import com.solid.idwisesdkdummy.util.ISSUE_DATE
import com.solid.idwisesdkdummy.util.NAME
import com.solid.idwisesdkdummy.util.extensions.inflateDialog
import com.solid.idwisesdkdummy.util.extensions.showDialog
import com.solid.idwisesdkdummy.util.helper.ProgressLoader
import com.solid.idwisesdkdummy.util.helper.createMultipart
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var singleScannerLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var doubleScannerLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var singleOptions: GmsDocumentScannerOptions
    private lateinit var doubleOptions: GmsDocumentScannerOptions
    @Inject
    lateinit var progressLoader: ProgressLoader
    private var imageUrls = listOf<String>()
    private var imageUris = listOf<String>()
    private var countryCode = ""
    private var capType = 0

    companion object {
        const val CAPTURE_SINGLE = 1
        const val CAPTURE_DOUBLE = 2
        const val EXTRA_CAPTURE_MODE = "extra_capture_mode"
        const val EXTRA_CAPTURE_URI_ONE = "extra_capture_uri_one"
        const val EXTRA_CAPTURE_URI_TWO = "extra_capture_uri_two"
    }

    private val uploadViewModel: UploadViewModel by viewModels()
    private val ocrViewModel: OcrViewModel by viewModels()

    private var imageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val selectedImageUri: Uri? = result.data?.data
            selectedImageUri?.let { uri ->
                imageUri = uri
                val file = uriToFile(this@MainActivity, uri) // Call the uriToFile function to convert URI to File
                file?.let {
                    // Now 'file' contains the image data from the URI
                    uploadImage(file) // Example: Call your uploadImage function with the file
                } ?: kotlin.run {
                    // Handle case when file creation fails
                    println("FileError, Failed to create file from URI")
                }
            }
        }
    }


    private fun uriToFile(context: Context, uri: Uri): File? {
        val contentResolver: ContentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        inputStream?.use { input ->
            val file = File(context.cacheDir, "temp_file.jpg")
            FileOutputStream(file).use { output ->
                val buffer = ByteArray(4 * 1024) // 4k buffer
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
                return file
            }
        }
        return null // Return null if file creation fails
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setObservers()
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
        singleScannerLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            handleSingleActivityResult(it)
        }
        doubleScannerLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            handleDoubleActivityResult(it)
        }

        singleOptions = GmsDocumentScannerOptions.Builder()
            .setGalleryImportAllowed(false)
            .setPageLimit(1)
            .setResultFormats(RESULT_FORMAT_JPEG)
            .setScannerMode(SCANNER_MODE_FULL)
            .build()
        doubleOptions = GmsDocumentScannerOptions.Builder()
            .setGalleryImportAllowed(false)
            .setPageLimit(2)
            .setResultFormats(RESULT_FORMAT_JPEG)
            .setScannerMode(SCANNER_MODE_FULL)
            .build()

        binding.btnSingleCapture.setOnClickListener {
            capType = CAPTURE_SINGLE
            startSingleOptionSetup()
        }

        binding.btnDoubleCapture.setOnClickListener {
            capType = CAPTURE_DOUBLE
            startDoubleOptionSetup()
        }

        binding.pickImageBtn.setOnClickListener{
            pickImageFromGallery()
        }
    }

    private fun setObservers(){
        uploadViewModel.imagesUploadState.observe(this) {
            val state = it.getContentIfNotHandled() ?: return@observe
            when(state) {
                is ResultState.Loading -> progressLoader.show("Working, please wait...")
                is ResultState.Error -> {
                    progressLoader.hide()
                    this.showDialog(
                        "Error",
                        state.error ?: "An error occurred, please try again",
                        dismiable = true
                    ) {  }
                }
                is ResultState.Success -> {}
            }
        }

        ocrViewModel.performOcrState.observe(this) {
            val state = it.getContentIfNotHandled() ?: return@observe
            when(state) {
                is ResultState.Loading -> progressLoader.show("Working, please wait...")
                is ResultState.Error -> {
                    progressLoader.hide()
                    this.showDialog(
                        "Error",
                        state.error ?: "An error occurred, please try again",
                        dismiable = true
                    ) {  }
                }
                is ResultState.Success -> {
                    progressLoader.hide()
                    val data =  state.data.documentData

                    if(capType == CAPTURE_SINGLE){
                        startActivity(Intent(this, ImageDisplayActivity::class.java).apply {
                            putExtra(EXTRA_CAPTURE_URI_ONE, imageUrls[0])
                            putExtra(EXTRA_CAPTURE_MODE, CAPTURE_SINGLE)
                            putExtra(NAME, data.fullName)
                            putExtra(DATE_OF_BIRTH, data.dateOfBirth)
                            putExtra(ID_NUMBER, data.documentNumber)
                            putExtra(GENDER, data.gender)
                            putExtra(COUNTRY, data.nationality)
                            putExtra(ISSUE_DATE, data.issuanceDate)
                            putExtra(EXPIRY_DATE, data.expiryDate)
                            putExtra(DOCUMENT_TYPE, data.documentType)
                        })
                        finish()
                    }else if(capType == CAPTURE_DOUBLE){
                        startActivity(Intent(this, ImageDisplayActivity::class.java).apply {
                            putExtra(EXTRA_CAPTURE_URI_ONE, imageUrls[0])
                            putExtra(EXTRA_CAPTURE_MODE, CAPTURE_SINGLE)
                            putExtra(NAME, data.fullName)
                            putExtra(DATE_OF_BIRTH, data.dateOfBirth)
                            putExtra(ID_NUMBER, data.documentNumber)
                            putExtra(GENDER, data.gender)
                            putExtra(COUNTRY, data.nationality)
                            putExtra(ISSUE_DATE, data.issuanceDate)
                            putExtra(EXPIRY_DATE, data.expiryDate)
                            putExtra(DOCUMENT_TYPE, data.documentType)
                            putExtra(EXTRA_CAPTURE_URI_ONE, imageUrls[0])
                            putExtra(EXTRA_CAPTURE_URI_TWO, imageUrls[1])
                            putExtra(DOUBLE_URI, false)
                            putExtra(EXTRA_CAPTURE_MODE, CAPTURE_DOUBLE)
                        })
                        finish()
                    }
                }
            }
        }
    }

    private fun startSingleOptionSetup(){
        GmsDocumentScanning.getClient(singleOptions).getStartScanIntent(this)
            .addOnSuccessListener { intentSender: IntentSender ->
                singleScannerLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
            }.addOnFailureListener() { e: Exception ->
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun startDoubleOptionSetup(){
        GmsDocumentScanning.getClient(doubleOptions).getStartScanIntent(this)
            .addOnSuccessListener { intentSender: IntentSender ->
                doubleScannerLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
            }.addOnFailureListener() { e: Exception ->
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun handleSingleActivityResult(activityResult: ActivityResult?) {
        val resultCode = activityResult?.resultCode
        val result = GmsDocumentScanningResult.fromActivityResultIntent(activityResult?.data)
        if (resultCode == Activity.RESULT_OK && result != null) {
            val pages = result.pages
            if (!pages.isNullOrEmpty()) {
                // Show the dialog that YVOS SDK is working...
                // Call the endpoint to upload the image
                // Call the endpoint to perform ocr
                // Pass ocr result to ImageDisplayActivity and display Image and details

                result.pages?.get(0)?.imageUri?.toFile()?.let { uploadImage(it) }
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            //Handle cancelled case
            Toast.makeText(this, "Document capture cancelled", Toast.LENGTH_SHORT).show()
        } else {
            // Handle failure case
            Toast.makeText(this, "Document capture failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleDoubleActivityResult(activityResult: ActivityResult?) {
        val resultCode = activityResult?.resultCode
        val result = GmsDocumentScanningResult.fromActivityResultIntent(activityResult?.data)
        if (resultCode == Activity.RESULT_OK && result != null) {
            val pages = result.pages
            if (!pages.isNullOrEmpty()) {
                // Show the dialog that YVOS SDK is working...
                // Perform barcode scan for back of the image
                // if driver license value is null, ask user their country and call the endpoint to perform ocr
                // else pass barcode result to ImageDisplayActivity and display Image and details
                imageUris = listOf(result.pages?.get(0)?.imageUri.toString(), result.pages?.get(1)?.imageUri.toString())
                result.pages?.get(1)?.imageUri?.let { processUriForBarcode(it) }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //Handle cancelled case
            Toast.makeText(this, "Document capture cancelled", Toast.LENGTH_SHORT).show()
        } else {
            // Handle failure case
            Toast.makeText(this, "Document capture failed", Toast.LENGTH_SHORT).show()
        }
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

    private fun processBarcode(inputImage: InputImage, uri: Uri) {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_PDF417,
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC)
            .enableAllPotentialBarcodes()
            .build()
        val scanner = BarcodeScanning.getClient(options)

        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                // Task completed successfully
                if(barcodes[0].driverLicense == null){
                    //ask user to select their country's country code
                    //make call to api endpoint.
                    showMessage()
                }else{
                    for (barcode in barcodes) {
                        startActivity(Intent(this@MainActivity, ImageDisplayActivity::class.java).apply {
                            putExtra(NAME, "${barcode.driverLicense?.firstName} ${barcode.driverLicense?.middleName} ${barcode.driverLicense?.lastName}")
                            putExtra(DATE_OF_BIRTH, formatDate("${barcode.driverLicense?.birthDate}"))
                            putExtra(ID_NUMBER, "${barcode.driverLicense?.licenseNumber}")
                            putExtra(GENDER, if(barcode.driverLicense?.gender == "1") "MALE" else "FEMALE")
                            putExtra(COUNTRY, "${barcode.driverLicense?.issuingCountry}")
                            putExtra(ISSUE_DATE, formatDate("${barcode.driverLicense?.issueDate}"))
                            putExtra(EXPIRY_DATE, formatDate("${barcode.driverLicense?.expiryDate}"))
                            putExtra(DOCUMENT_TYPE, barcode.driverLicense?.documentType.toString())
                            putExtra(EXTRA_CAPTURE_URI_ONE, imageUris[0])
                            putExtra(EXTRA_CAPTURE_URI_TWO, imageUris[1])
                            putExtra(DOUBLE_URI, true)
                            putExtra(EXTRA_CAPTURE_MODE, CAPTURE_DOUBLE)
                        })
                        finish()
                    }
                }
            }
            .addOnFailureListener {
                // Task failed with an exception
                Toast.makeText(this, "Barcode scanning failed!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImage(fileSingle: File? = null, filesDouble: List<File>? = null) {
        if(filesDouble == null){
            uploadViewModel.uploadImages(uploadRequests = listOf(createMultipart(fileSingle!!))){ urls ->
                imageUrls = urls
                val ocrSingleRequest = DocumentCaptureDto.OcrSingleRequest(
                    publicMerchantID = "61d880f1e8e15aaf24558f1a",
                    documentType = "passport",
                    frontImgData = urls[0],
                )
                ocrViewModel.performOcr(ocrSingleRequest)
            }
        }else{
            uploadViewModel.uploadImages(uploadRequests = listOf(createMultipart(filesDouble[0]), createMultipart(filesDouble[1]))){ urls ->
                imageUrls = urls
                val ocrDoubleRequest = DocumentCaptureDto.OcrDoubleRequest(
                    publicMerchantID = "61d880f1e8e15aaf24558f1a",
                    documentType = "id_card",
                    frontImgData = urls[0],
                    backImgData = urls[1],
                    countryCode = countryCode
                )
                ocrViewModel.performOcrDouble(ocrDoubleRequest)
            }
        }
    }

    private fun showMessage() {
        var dialog: MaterialDialog? = null
        val binding = LayoutCountryBinding.inflate(layoutInflater)
        binding.selectIdEditText.setOnClickListener {
            binding.selectIdEditText.showDropDown()
        }
        val arrayAdapter = ArrayAdapter(
            this,
            R.layout.country_drop_down_item,
            resources.getStringArray(R.array.country_codes)
        )
        binding.selectIdEditText.setAdapter(arrayAdapter)

        binding.process.setOnClickListener {
            dialog?.dismiss()
            countryCode = binding.layoutSelectCountry.editText?.text?.trim().toString()
            if(countryCode.isNotEmpty()){
                uploadImage(filesDouble = listOf(Uri.parse(imageUris[0]).toFile(), Uri.parse(imageUris[1]).toFile()))
            } else {
                Toast.makeText(this, "Please select your country code to continue", Toast.LENGTH_SHORT).show()
            }
        }

        dialog = this.inflateDialog(binding.root)
        dialog.setCancelable(false)
    }
}