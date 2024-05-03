package com.solid.idwisesdkdummy.features.document_capture.ocr

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.solid.idwisesdkdummy.data.functional.ResultState
import com.solid.idwisesdkdummy.data.model.document_capture.DocumentCaptureDto
import com.solid.idwisesdkdummy.domain.repository.DocumentCaptureDomain
import com.solid.idwisesdkdummy.domain.repository.ISDkRepository
import com.solid.idwisesdkdummy.util.helper.ErrorHelper
import com.solid.idwisesdkdummy.util.helper.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OcrViewModel @Inject constructor(
    private val repository: ISDkRepository,
    app: Application
) : AndroidViewModel(app) {

    val performOcrState = MutableLiveData<SingleEvent<ResultState<DocumentCaptureDomain.OcrResponse>>>()

    fun performOcr(ocrSingleRequest: DocumentCaptureDto.OcrSingleRequest) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            val message = ErrorHelper.handleException(throwable)
            performOcrState.postValue(SingleEvent(ResultState.Error(message)))
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            performOcrState.postValue(SingleEvent(ResultState.Loading()))
            val response = repository.performOcr(ocrSingleRequest)
            if (response.success) {
                performOcrState.postValue(
                    SingleEvent(
                        ResultState.Success(
                            response
                        )
                    )
                )
            } else {
                performOcrState.postValue(
                    SingleEvent(
                        ResultState.Error(
                            response.message ?: "An error occurred"
                        )
                    )
                )
            }
        }
    }

    fun performOcrDouble(ocrDoubleRequest: DocumentCaptureDto.OcrDoubleRequest) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            val message = ErrorHelper.handleException(throwable)
            performOcrState.postValue(SingleEvent(ResultState.Error(message)))
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            performOcrState.postValue(SingleEvent(ResultState.Loading()))
            val response = repository.performOcrDouble(ocrDoubleRequest)
            if (response.success) {
                performOcrState.postValue(
                    SingleEvent(
                        ResultState.Success(
                            response
                        )
                    )
                )
            } else {
                performOcrState.postValue(
                    SingleEvent(
                        ResultState.Error(
                            response.message ?: "An error occurred"
                        )
                    )
                )
            }
        }
    }
}
