package com.emretnkl.myshopapp.feature.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.emretnkl.myshopapp.R
import com.emretnkl.myshopapp.data.model.ProductsResponseItemDTO
import com.emretnkl.myshopapp.databinding.FragmentSearchBinding
import com.emretnkl.myshopapp.feature.search.adapter.OnSearchClickListener
import com.emretnkl.myshopapp.feature.search.adapter.SearchAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), OnSearchClickListener {
    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var binding : FragmentSearchBinding
    private var navController: NavController? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.svSearchFragment.clearFocus()

        searchProduct()

/*
        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.uiState.collect {
                    when (it) {
                        is SearchViewState.Success -> {
                            binding.rvSearchFragment.adapter =
                                SearchAdapter(this@SearchFragment).apply {
                                    submitList(it.search)

                                }
                        }
                        is SearchViewState.Loading -> {

                        }
                    }
                }
            }
            launch {
                viewModel.uiEvent.collect {
                    when (it) {
                        is SearchViewEvent.ShowError -> {
                            Snackbar.make(
                                binding.root,
                                it.message.toString(),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        is SearchViewEvent.NavigateToDetail -> {

                        }
                    }
                }
            }
        }
*/
        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.uiState.collect {
                    when (it) {
                        is SearchViewState.Success -> {
                            if (it.filteredSearch.isEmpty().not()) {
                                initAdapter(it.filteredSearch)
                            } else {
                                initAdapter(it.search)
                            }
                        }
                        is SearchViewState.Loading -> {

                        }
                    }
                }
            }
            launch {
                viewModel.uiEvent.collect {
                    when (it) {
                        is SearchViewEvent.ShowError -> {
                            Snackbar.make(
                                binding.root,
                                it.message.toString(),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        is SearchViewEvent.NavigateToDetail -> {

                        }
                    }
                }
            }
        }
        binding.ivSearchFragmentBasketIcon.setOnClickListener {
            navController?.navigate(R.id.action_searchFragment_to_basketFragment)
        }


    }

    private fun initAdapter(search: MutableList<ProductsResponseItemDTO>) {
        binding.rvSearchFragment.adapter =
            SearchAdapter(this@SearchFragment).apply {
                submitList(search)
            }
    }

    private fun searchProduct() {
        binding.svSearchFragment.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (newText.length > 2) {
                        viewModel.searchMovie(newText)
                    } else {
                        viewModel.searchMovie("")
                    }
                }
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchMovie(it) }
                return false
            }
        })
    }

    override fun onSearchClick(search: ProductsResponseItemDTO) {
        navController?.navigate(R.id.action_searchFragment_to_productDetailFragment,Bundle().apply {
            putString("productId", search.id.toString())
            putString("productImage",search.image.toString())
            putString("productTitle",search.title)
            putString("productCategory",search.category.toString())
            putString("productDescription",search.description.toString())
            putString("productPrice",search.price.toString())
            putString("productRating",search.rating.toString())
        })
    }


}