package com.solid.idwisesdkdummy.di

import com.solid.idwisesdkdummy.BuildConfig
import com.solid.idwisesdkdummy.data.api.SDKService
import com.solid.idwisesdkdummy.data.api.SDKServiceFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RemoteModule {

    companion object {

        @[Provides Singleton]
        fun provideUploadApiService(): SDKService =
            SDKServiceFactory.createApiService(BuildConfig.DEBUG)
    }

}