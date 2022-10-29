package com.emretnkl.myshopapp.data.remote.source.impl

import com.emretnkl.myshopapp.data.model.ProductsResponse
import com.emretnkl.myshopapp.data.model.ProductsResponseItem
import com.emretnkl.myshopapp.data.remote.api.ProductsService
import com.emretnkl.myshopapp.data.remote.source.ProductsRemoteDataSource
import com.emretnkl.myshopapp.data.remote.utils.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductsRemoteDataSourceImpl @Inject constructor(private val productsService: ProductsService) :
    BaseRemoteDataSource(),ProductsRemoteDataSource{
    override suspend fun getProductDetail(id: Int): Flow<DataState<ProductsResponseItem>> {
        return getResult { productsService.getProductDetail(id) }
    }

    override suspend fun getAllProducts(): Flow<DataState<ArrayList<ProductsResponseItem>>> {
        return getResult { productsService.getAllProducts() }
    }
}