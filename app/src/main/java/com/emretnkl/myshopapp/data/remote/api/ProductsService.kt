package com.emretnkl.myshopapp.data.remote.api

import com.emretnkl.myshopapp.data.model.ProductsResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductsService {

    @GET("products")
    suspend fun getAllProducts(): Response<ArrayList<ProductsResponseItem>>

    @GET("products/{id}")
    suspend fun getProductDetail(@Path("id") id: Int) : Response<ProductsResponseItem>
}