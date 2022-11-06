package com.emretnkl.myshopapp.data.model

data class BasketProduct(
    val id: String? = null,
    val image: String? = null,
    val title: String? = null,
    val price: String? = null,
    var quantity: Int? = null
)
