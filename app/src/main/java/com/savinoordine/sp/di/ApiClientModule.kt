package com.savinoordine.sp.di

import com.savinoordine.sp.repository.BeerApiClient
import com.savinoordine.sp.repository.BeerApiClientImpl
import com.savinoordine.sp.repository.BeerRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiClientModule {

    @Binds
    @Singleton
    abstract fun BeerApiClientImpl.bindBeerRepository(): BeerRepository

    companion object {
        @Provides
        @Singleton
        fun provideBeerApiClient(retrofit: Retrofit): BeerApiClient =
            retrofit.create(BeerApiClient::class.java)
    }
}