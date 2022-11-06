package com.emretnkl.myshopapp.feature.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emretnkl.myshopapp.data.model.ProductsResponseItemDTO
import com.emretnkl.myshopapp.data.remote.utils.DataState
import com.emretnkl.myshopapp.domain.repository.ProductsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
):ViewModel() {

    private val _uiState = MutableStateFlow<ProductViewState>(ProductViewState.Success(mutableListOf()))
    val uiState: StateFlow<ProductViewState> = _uiState

    private val _uiEvent = MutableSharedFlow<ProductViewEvent>(replay = 0)
    val uiEvent: SharedFlow<ProductViewEvent> = _uiEvent


    init {
        getProducts()

    }


    private fun getProducts() {
        viewModelScope.launch {
            productsRepository.getAllProducts().collect {
                when(it) {
                    is DataState.Success -> {
                        _uiState.value = ProductViewState.Success(it.data.map {
                            ProductsResponseItemDTO(
                                id = it?.id,
                                title = it?.title,
                                price = it?.price,
                                description = it?.description,
                                category = it?.category,
                                image = it?.image,
                                rating = it?.rating,
                                isOnCart = false

                            )
                        }.toMutableList())
                    }
                    is DataState.Error -> {
                        _uiEvent.emit(ProductViewEvent.ShowError(it.error?.status_message))
                    }
                    is DataState.Loading -> {
                        _uiState.value = ProductViewState.Loading
                    }
                }
            }
        }
    }


}

sealed class ProductViewEvent {
    data class ShowError(val message: String?) : ProductViewEvent()
    object NavigateToDetail : ProductViewEvent()

}

sealed class ProductViewState {
    class Success(val products: MutableList<ProductsResponseItemDTO>?) : ProductViewState()
    object Loading : ProductViewState()
}
