package com.solid.idwisesdkdummy.util.helper

import com.solid.idwisesdkdummy.data.model.upload.TaskDto
import com.solid.idwisesdkdummy.util.extensions.toObject
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

object ErrorHelper {
    fun handleException(exception: Throwable): String {
        val message = when (exception) {
            is TimeoutException -> "Connection timeout. Please try again"
            is ConnectException -> "Couldn't connect. Please check your internet"
            is SocketTimeoutException -> "Connection timeout. Please check your internet connection"
            is UnknownHostException -> "Couldn't connect to server. Please check your internet connection"
            is HttpException -> { extractMessage(exception.response()?.errorBody()?.string())
            }
            else -> "An error occurred. Please try again"
        }
        return if (message.length > 100) "Server returned an error. Please try again" else message
    }

    private fun extractMessage(json: String?): String {
        Timber.d("Exception: $json")
        val message = "Server returned an error. Please try again"
        if (json == null) return message
        return try {
            val response = json.toObject<TaskDto.GenericResponse>()
            response.message ?: message
        } catch (exception: Exception) {
            exception.printStackTrace()
            message
        }
    }
}