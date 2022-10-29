package com.emretnkl.myshopapp.data.remote.source

import com.emretnkl.myshopapp.data.model.ProductsResponse
import com.emretnkl.myshopapp.data.model.ProductsResponseItem
import com.emretnkl.myshopapp.data.remote.utils.DataState
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ProductsRemoteDataSource {

    suspend fun getProductDetail(id : Int): Flow<DataState<ProductsResponseItem>>
    suspend fun getAllProducts(): Flow<DataState<ArrayList<ProductsResponseItem>>>
}