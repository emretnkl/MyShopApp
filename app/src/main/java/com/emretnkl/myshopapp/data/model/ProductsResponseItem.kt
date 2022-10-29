package com.emretnkl.myshopapp.data.model


import com.google.gson.annotations.SerializedName

data class ProductsResponseItem(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("category")
    val category: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("rating")
    val rating: Rating?
)