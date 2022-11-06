package com.emretnkl.myshopapp.feature.basket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(): ViewModel() {

    private val totalPrice = MutableStateFlow(0F)

    val totalPriceText = totalPrice.map {
        "$it $"
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "0.0 $")

    fun increaseTotalPrice(price: Float?) {
        viewModelScope.launch {
            price?.let {
                totalPrice.emit(totalPrice.value + it)
            }
        }
    }

    fun decreaseTotalPrice(price: Float?) {
        viewModelScope.launch {
            price?.let {
                totalPrice.emit(totalPrice.value - it)
            }
        }
    }

}