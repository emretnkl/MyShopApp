package com.emretnkl.myshopapp.data.di

import com.emretnkl.myshopapp.data.remote.api.ProductsService
import com.emretnkl.myshopapp.data.remote.source.ProductsRemoteDataSource
import com.emretnkl.myshopapp.data.remote.source.impl.ProductsRemoteDataSourceImpl
import com.emretnkl.myshopapp.domain.repository.ProductsRepository
import com.emretnkl.myshopapp.domain.repository.impl.ProductsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProductsModule {

    @Singleton
    @Provides
    fun provideProductsService(retrofit: Retrofit) = retrofit.create(ProductsService::class.java)

    @Singleton
    @Provides
    fun provideProductsRemoteDataSource(productsService: ProductsService): ProductsRemoteDataSource =
        ProductsRemoteDataSourceImpl(productsService)

    @Singleton
    @Provides
    fun provideProductsRepository(productsRemoteDataSource: ProductsRemoteDataSource): ProductsRepository =
        ProductsRepositoryImpl(productsRemoteDataSource)
}