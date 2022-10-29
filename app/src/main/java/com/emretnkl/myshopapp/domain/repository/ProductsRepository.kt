package com.emretnkl.myshopapp.domain.repository

import com.emretnkl.myshopapp.data.model.ProductsResponse
import com.emretnkl.myshopapp.data.model.ProductsResponseItem
import com.emretnkl.myshopapp.data.remote.utils.DataState
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ProductsRepository {
    suspend fun getProductDetail(id : Int): Flow<DataState<ProductsResponseItem>>
    suspend fun getAllProducts(): Flow<DataState<ArrayList<ProductsResponseItem>>>
}