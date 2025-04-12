package com.maka.launcher.di

import com.maka.launcher.data.repository.SupabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideSupabaseRepository(): SupabaseRepository {
        return SupabaseRepository()
    }
}