package com.emretnkl.myshopapp.feature.productDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emretnkl.myshopapp.data.model.ProductsResponseItemDTO
import com.emretnkl.myshopapp.data.remote.utils.DataState
import com.emretnkl.myshopapp.domain.repository.ProductsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val firebaseAuth: FirebaseAuth,
    private val firebase: FirebaseFirestore,
    private val state: SavedStateHandle
):ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailViewState>(ProductDetailViewState.Success(mutableListOf()))
    val uiState: StateFlow<ProductDetailViewState> = _uiState

    private val _uiEvent = MutableSharedFlow<ProductDetailViewEvent>(replay = 0)
    val uiEvent: SharedFlow<ProductDetailViewEvent> = _uiEvent

    init {
        getProducts()

    }


    private fun getProducts() {
        viewModelScope.launch {
            productsRepository.getAllProducts().collect {
                when(it) {
                    is DataState.Success -> {
                        _uiState.value = ProductDetailViewState.Success(it.data.map {
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
                        _uiEvent.emit(ProductDetailViewEvent.ShowError(it.error?.status_message))
                    }
                    is DataState.Loading -> {
                        _uiState.value = ProductDetailViewState.Loading
                    }
                }
            }
        }
    }


}

sealed class ProductDetailViewEvent {
    data class ShowError(val message: String?) : ProductDetailViewEvent()
    object NavigateToDetail : ProductDetailViewEvent()

}

sealed class ProductDetailViewState {
    class Success(val products: MutableList<ProductsResponseItemDTO>?) : ProductDetailViewState()
    object Loading : ProductDetailViewState()
}



