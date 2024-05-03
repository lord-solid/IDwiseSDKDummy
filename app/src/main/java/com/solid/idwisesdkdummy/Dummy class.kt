package com.solid.idwisesdkdummy

import android.graphics.Bitmap
import com.solid.idwisesdkdummy.databinding.ActivityMainBinding

//private var journeyId = ""
//private var imageBitmap: Bitmap? = null
//private lateinit var binding: ActivityMainBinding
//private val clientKey =
//    "QmFzaWMgWmpBNU16QTRNR0V0WmpCbE9DMDBPREJoTFdGak5UWXRaREUxWkdObFlUTmpaVEEyT2twaU5EQTBRMU5sV1ZwSU5XMDVWMk5CZGxWTVRUZENNWEk0UlZSMGRWWnpNM0JqVDJaU01uZz0="
//private val flowId = "f093080a-f0e8-480a-ac56-d15dcea3ce06"
//private val TAG = "IDWiseSDKCallback"
//    private val journeyCallback = object : IDWiseSDKCallback {
//        override fun onJourneyStarted(journeyInfo: JourneyInfo) {
//            Log.d(TAG, "onJourneyStarted")
//            println("$TAG => $journeyInfo")
//            onLoading(true)
//            journeyId = journeyInfo.journeyId
//        }
//
//        override fun onJourneyCompleted(journeyInfo: JourneyInfo, isSucceeded: Boolean) {
//            Log.d(TAG, "onJourneyCompleted")
//            binding.journeyText.text =
//                "========================   Journey Result  ======================== \n\n Is Suceeded: $isSucceeded \n Journey has ended"
//            IDWise.getJourneySummary { journeySummary, _ ->
//                binding.journText.text =
//                    StringBuilder("Journey ID: ${journeySummary?.journeyId.toString()}").append("\nJourney Summary: ${journeySummary?.journeyResult}")
//                        .append("\nStep summaries: ${journeySummary?.stepSummaries}")
//            }
//        }
//
//        override fun onJourneyResumed(journeyInfo: JourneyInfo) {
//            Log.d(TAG, "onJourneyResumed")
//            println("$TAG => $journeyInfo")
//        }
//
//        override fun onJourneyCancelled(journeyInfo: JourneyInfo?) {
//            Log.d(TAG, "onJourneyCancelled")
//            println("$TAG => $journeyInfo")
//        }
//
//        override fun onError(error: IDWiseSDKError) {
//            Log.d(TAG, "onError ${error.message}")
//            println("$TAG => $error")
//        }
//    }
//
//    private val stepCallback = object : IDWiseSDKStepCallback {
//        override fun onStepCaptured(stepId: String, bitmap: Bitmap?, croppedBitmap: Bitmap?) {
//            //This event triggers when User has captured the image from the camera
//            imageBitmap = bitmap ?: croppedBitmap
//            Toast.makeText(this@MainActivity, "Image: $imageBitmap", Toast.LENGTH_SHORT).show()
//        }
//
//        override fun onStepResult(stepId: String, stepResult: StepResult?) {
//            //This event is triggered when Image processing is completed at the backend.
//            //stepResult contains the details of the processing output
//            onLoading(false)
//            if (stepId == "10") {
//                Log.d("onStepResult", "Step $stepId result!!")
//                Log.d("onStepResult", "StepResult $stepResult result!!")
//                binding.stepText.text = """
//                    ========================   Step Result  ========================
//                    Image Bitmap: $imageBitmap
//                    Extracted Fields:
//                    Passed Rules: ${stepResult?.hasPassedRules}
//                    Is Concluded: ${stepResult?.isConcluded}
//                    Status: ${stepResult?.status}
//                    Full Name: ${stepResult?.extractedFields?.get("Full Name")}
//                    Sex: ${stepResult?.extractedFields?.get("Sex")}
//                    Nationality Code: ${stepResult?.extractedFields?.get("Nationality Code")}
//                    Personal Number: ${stepResult?.extractedFields?.get("Personal Number")}
//                    Machine Readable Zone: ${stepResult?.extractedFields?.get("Machine Readable Zone")}
//                    Nationality: ${stepResult?.extractedFields?.get("Nationality")}
//                    Birth Place: ${stepResult?.extractedFields?.get("Birth Place")}
//                    Document Number: ${stepResult?.extractedFields?.get("Document Number")}
//                    Issuing Authority: ${stepResult?.extractedFields?.get("Issuing Authority")}
//                    Issue Date: ${stepResult?.extractedFields?.get("Issue Date")}
//                    Expiry Date: ${stepResult?.extractedFields?.get("Expiry Date")}
//                    Birth Date: ${stepResult?.extractedFields?.get("Birth Date")}
//                """.trimIndent()
//                Glide.with(this@MainActivity).load(imageBitmap).into(binding.imageView)
//            } else if (stepId == "20") {
//                Log.d("onStepResult", "Step $stepId result!!")
//                binding.stepText.text =
//                    "========================   Step Result  ======================== \n\n $stepResult"
//                Glide.with(this@MainActivity).load(imageBitmap).into(binding.imageView)
//            }
//            Glide.with(this@MainActivity).load(imageBitmap).into(binding.imageView)
//            IDWise.finishDynamicJourney()
//        }
//
//        override fun onStepConfirmed(stepId: String) {
//            Log.d("onStepConfirmed", "Step $stepId confirmed!!")
//        }
//
//        override fun onStepCancelled(stepId: String) {
//            Log.d("onStepCancelled", "Step $stepId cancelled!!")
//        }
//
//        override fun onStepSkipped(stepId: String) {
//            Log.d("onStepSkipped", "Step $stepId skipped!!")
//        }
//    }


//        IDWise.initialize(clientKey, IDWiseSDKTheme.SYSTEM_DEFAULT) { error: IDWiseSDKError? ->
//            error?.printStackTrace()
//        }
//        IDWise.startDynamicJourney(
//            this@MainActivity, flowId, "1", "en", journeyCallback, stepCallback
//        )
//
//        binding.livenessButton.setOnClickListener() {
//            IDWise.startStep(this@MainActivity, "20")
//        }
//
//        binding.documentButton.setOnClickListener() {
//            IDWise.startStep(this@MainActivity, "10")
//        }
//}

//    private fun onLoading(loading: Boolean) {
//        binding.indicator.visibleIf(loading)
//    }