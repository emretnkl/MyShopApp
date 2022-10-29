package com.emretnkl.myshopapp.domain.repository.impl

import com.emretnkl.myshopapp.data.model.ProductsResponse
import com.emretnkl.myshopapp.data.model.ProductsResponseItem
import com.emretnkl.myshopapp.data.remote.source.ProductsRemoteDataSource
import com.emretnkl.myshopapp.data.remote.utils.DataState
import com.emretnkl.myshopapp.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(private val productsRemoteDataSource: ProductsRemoteDataSource) :
    ProductsRepository {
    override suspend fun getProductDetail(id: Int): Flow<DataState<ProductsResponseItem>> {
        return productsRemoteDataSource.getProductDetail(id)
    }

    override suspend fun getAllProducts(): Flow<DataState<ArrayList<ProductsResponseItem>>> {
        return productsRemoteDataSource.getAllProducts()
    }


}