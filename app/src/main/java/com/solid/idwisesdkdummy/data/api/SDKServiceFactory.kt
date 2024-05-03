package com.solid.idwisesdkdummy.data.api

import com.solid.idwisesdkdummy.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object SDKServiceFactory {
    private const val BASE_URL: String = BuildConfig.SERVER_URL

    fun createApiService(isDebug: Boolean): SDKService {
        val okHttpClient: OkHttpClient = makeOkHttpClient(
            makeLoggingInterceptor((isDebug))
        )
        return makeApiService(okHttpClient)
    }

    private fun makeApiService(okHttpClient: OkHttpClient): SDKService {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(SDKService::class.java)
    }

    private fun makeOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()

        return builder
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (isDebug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return logging
    }
}