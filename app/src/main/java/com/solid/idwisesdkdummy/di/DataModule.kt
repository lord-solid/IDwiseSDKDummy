package com.solid.idwisesdkdummy.di

import com.solid.idwisesdkdummy.data.source.ISdkSource
import com.solid.idwisesdkdummy.data.source.SdkDataSource
import com.solid.idwisesdkdummy.domain.repository.ISDkRepository
import com.solid.idwisesdkdummy.domain.repository.SDKRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @get:Binds
    val SdkDataSource.agentDataSource: ISdkSource


    @get:Binds
    val SDKRepositoryImpl.sdkRepository: ISDkRepository
}