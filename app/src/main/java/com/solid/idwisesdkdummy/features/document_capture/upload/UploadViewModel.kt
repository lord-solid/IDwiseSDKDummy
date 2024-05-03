package com.solid.idwisesdkdummy.features.document_capture.upload

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solid.idwisesdkdummy.data.functional.ResultState
import com.solid.idwisesdkdummy.domain.repository.ISDkRepository
import com.solid.idwisesdkdummy.util.helper.ErrorHelper
import com.solid.idwisesdkdummy.util.helper.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val sdkRepository: ISDkRepository
) : ViewModel() {

    val imagesUploadState = MutableLiveData<SingleEvent<ResultState<Pair<List<String>?, Int>>>>()

    fun uploadImages(uploadRequests: List<MultipartBody.Part>, imageType: Int = UploadViewState.Companion.UploadType.imageUpload, callback: (urls: List<String>) -> Unit) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            val message = ErrorHelper.handleException(throwable)
            imagesUploadState.postValue(SingleEvent(ResultState.Error(message)))
        }

        viewModelScope.launch(exceptionHandler) {
            imagesUploadState.postValue(SingleEvent(ResultState.Loading()))
           val response = sdkRepository.uploadImages(uploadRequests)
            if (response.success) {
                imagesUploadState.postValue(SingleEvent(ResultState.Success(Pair(response.urls, imageType))))
                callback(response.urls ?: emptyList())
            } else {
                imagesUploadState.postValue(SingleEvent(ResultState.Error(response.message ?: "An error occurred")))
            }
        }
    }
}