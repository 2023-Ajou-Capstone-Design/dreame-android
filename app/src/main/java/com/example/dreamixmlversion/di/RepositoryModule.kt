package com.example.dreamixmlversion.di

import com.example.dreamixmlversion.data.repository.SpotRepository
import com.example.dreamixmlversion.data.repository.SpotRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(ViewModelComponent::class)
@Module
abstract class RepositoryModule {

    @ViewModelScoped
    @Binds
    abstract fun bindSpotRepository(impl: SpotRepositoryImpl): SpotRepository
}