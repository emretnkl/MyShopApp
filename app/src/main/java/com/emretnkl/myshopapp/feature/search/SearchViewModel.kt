package com.emretnkl.myshopapp.feature.search

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
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
): ViewModel() {

    private val _uiState = MutableStateFlow<SearchViewState>(SearchViewState.Success(mutableListOf(), mutableListOf()))
    val uiState: StateFlow<SearchViewState> = _uiState

    private val _uiEvent = MutableSharedFlow<SearchViewEvent>(replay = 0)
    val uiEvent: SharedFlow<SearchViewEvent> = _uiEvent

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            productsRepository.getAllProducts().collect {
                when(it) {

                    is DataState.Success -> {

                        val list = it.data.map {
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
                        }
                            _uiState.value = SearchViewState.Success(list.toMutableList(), mutableListOf())
                    }
                    is DataState.Error -> {
                        _uiEvent.emit(SearchViewEvent.ShowError(it.error?.status_message))
                    }
                    is DataState.Loading -> {
                        _uiState.value = SearchViewState.Loading
                    }
                }
            }
        }
    }

    fun searchMovie(query: String) {
        viewModelScope.launch {
            val updateQuery = query.lowercase(Locale.getDefault())

            val currentData = (_uiState.value as SearchViewState.Success).search
            if (updateQuery != "") {
                currentData.let {
                    val filteredList = it.filter {
                        it.title?.lowercase(Locale.getDefault())?.contains(updateQuery) ?: false
                    }
                    _uiState.value =
                        SearchViewState.Success(currentData, filteredList.toMutableList())
                }
            } else {
                _uiState.value =
                    SearchViewState.Success(currentData, mutableListOf())

          }
       }
    }
}


sealed class SearchViewEvent {
    data class ShowError(val message: String?) : SearchViewEvent()
    object NavigateToDetail : SearchViewEvent()

}

sealed class SearchViewState {
    data class Success(
        val search: MutableList<ProductsResponseItemDTO>,
        val filteredSearch: MutableList<ProductsResponseItemDTO>
    ) : SearchViewState()
    object Loading : SearchViewState()
}