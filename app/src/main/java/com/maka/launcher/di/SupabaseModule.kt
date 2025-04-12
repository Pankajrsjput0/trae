package com.maka.launcher.di

import com.maka.launcher.data.repository.AppUsageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {
    @Provides
    @Singleton
    fun provideAppUsageRepository(): AppUsageRepository {
        return AppUsageRepository()
    }
}