package com.solid.idwisesdkdummy

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Size
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.solid.idwisesdkdummy.databinding.ActivityCameraBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class CameraActivity : AppCompatActivity() {
    private var captureMode: Int = CAPTURE_SINGLE
    private var imageCapture: ImageCapture? = null
    private lateinit var binding: ActivityCameraBinding
    private var firstImageFile: File? = null
    private var secondImageFile: File? = null

    companion object {
        const val CAPTURE_SINGLE = 1
        const val CAPTURE_DOUBLE = 2
        const val EXTRA_CAPTURE_MODE = "extra_capture_mode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        captureMode = intent.getIntExtra(EXTRA_CAPTURE_MODE, CAPTURE_SINGLE)

        binding.textCaptureStatus.text =
            if (captureMode == CAPTURE_SINGLE) "Capture ID Card" else "Capture front of the document"

        // Initialize the camera
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()


            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.textureView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetResolution(Size(1080, 1920))
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))

        // Capture image on tap
        binding.stopBtn.setOnClickListener {
            captureImage()
        }
    }


    private fun captureImage() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            externalMediaDirs.firstOrNull(),
            "${System.currentTimeMillis()}.jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(baseContext, "Capture failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    if (captureMode == CAPTURE_DOUBLE) {
                        firstImageFile = photoFile
                        binding.textCaptureStatus.text = "Capture back of the document"
                        captureSecondImage()
                    } else {
                        val intent = Intent(this@CameraActivity, ImageDisplayActivity::class.java)
                        intent.putExtra(ImageDisplayActivity.EXTRA_IMAGE_PATH, photoFile.absolutePath)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        )
    }

    private fun captureSecondImage() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            externalMediaDirs.firstOrNull(),
            "${System.currentTimeMillis()}_2.jpg" // Add suffix to differentiate second image
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(baseContext, "Second capture failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    secondImageFile = photoFile
                    // Combine images and navigate to display
                    combineAndNavigateToDisplay()
                }
            }
        )
    }

    private fun combineAndNavigateToDisplay() {
//        firstImageFile?.let { firstImage ->
//            secondImageFile?.let { secondImage ->
//
//            }
//        }

        val combinedBitmap = combineBitmaps(firstImageFile!!, secondImageFile!!)
        val combinedFile = saveCombinedBitmap(combinedBitmap)
        navigateToImageDisplay(combinedFile.absolutePath)
    }

    private fun combineBitmaps(firstFile: File, secondFile: File): Bitmap {
        // Decode the Bitmaps from the image files
        val firstBitmap = BitmapFactory.decodeFile(firstFile.absolutePath)
        val secondBitmap = BitmapFactory.decodeFile(secondFile.absolutePath)

        // Create a new bitmap for the combined image
        val combinedWidth = firstBitmap.height.coerceAtLeast(secondBitmap.height)
        val combinedHeight = firstBitmap.width + secondBitmap.width
        val combinedBitmap = Bitmap.createBitmap(combinedWidth, combinedHeight, firstBitmap.config)

        // Create a Canvas to draw the combined bitmap
        val canvas = Canvas(combinedBitmap)

        // Draw the first bitmap on the left side of the combined bitmap
        canvas.drawBitmap(firstBitmap, null, Rect(0, 0, firstBitmap.width, combinedHeight), null)

        // Draw the second bitmap on the right side of the combined bitmap
        canvas.drawBitmap(secondBitmap, firstBitmap.width.toFloat(), 0f, null)

        // Return the combined bitmap
        return combinedBitmap
    }

    private fun saveCombinedBitmap(bitmap: Bitmap): File {
        val combinedFile = File(
            externalMediaDirs.firstOrNull(),
            "${System.currentTimeMillis()}_combined.jpg" // Save as JPEG
        )

        try {
            val outputStream = FileOutputStream(combinedFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return combinedFile
    }

    private fun navigateToImageDisplay(imagePath: String) {
        val intent = Intent(this@CameraActivity, ImageDisplayActivity::class.java)
        intent.putExtra(ImageDisplayActivity.EXTRA_IMAGE_PATH, imagePath)
        startActivity(intent)
        finish()
    }
}