package com.example.dreamixmlversion.di

import com.example.dreamixmlversion.data.repository.CategoryRepository
import com.example.dreamixmlversion.data.repository.CategoryRepositoryImpl
import com.example.dreamixmlversion.data.repository.StoreRepository
import com.example.dreamixmlversion.data.repository.StoreRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
abstract class RepositoryModule {

    @ViewModelScoped
    @Binds
    abstract fun bindStoreRepository(impl: StoreRepositoryImpl): StoreRepository

    @ViewModelScoped
    @Binds
    abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository
}