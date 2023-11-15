package com.aynlss.jumboxproject.di

import com.aynlss.jumboxproject.data.repository.ProductRepository
import com.aynlss.jumboxproject.data.source.local.ProductDao
import com.aynlss.jumboxproject.data.source.remote.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductsRepository(
        productService: ProductService,
        productDao: ProductDao
    ) = ProductRepository(productService, productDao)
}