package com.solid.idwisesdkdummy.di

import com.solid.idwisesdkdummy.util.helper.ProgressLoader
import com.solid.idwisesdkdummy.util.helper.ProgressLoaderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
interface UIModule {

    @get:Binds
    val ProgressLoaderImpl.progressLoader: ProgressLoader
}