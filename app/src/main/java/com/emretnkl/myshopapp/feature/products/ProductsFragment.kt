package com.emretnkl.myshopapp.feature.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.emretnkl.myshopapp.R
import com.emretnkl.myshopapp.data.model.ProductsResponse
import com.emretnkl.myshopapp.data.model.ProductsResponseItem
import com.emretnkl.myshopapp.data.model.ProductsResponseItemDTO
import com.emretnkl.myshopapp.data.remote.utils.DataState
import com.emretnkl.myshopapp.databinding.FragmentProductsBinding
import com.emretnkl.myshopapp.feature.products.adapter.OnProductClickListener
import com.emretnkl.myshopapp.feature.products.adapter.ProductsAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ProductsFragment : Fragment(), OnProductClickListener {
    private val viewModel by viewModels<ProductsViewModel>()
    private lateinit var binding: FragmentProductsBinding
    private var navController: NavController? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.uiState.collect{
                    when(it){
                        is ProductViewState.Success -> {
                            binding.rvProductsFragment.adapter =
                                ProductsAdapter(this@ProductsFragment).apply {
                                    submitList(it.products)

                                }
                        }
                        is ProductViewState.Loading -> {

                        }
                    }
                }
            }
            launch {
                viewModel.uiEvent.collect{
                    when(it) {
                        is ProductViewEvent.ShowError -> {
                            Snackbar.make(binding.root,it.message.toString(),Snackbar.LENGTH_SHORT).show()
                        }
                        is ProductViewEvent.NavigateToDetail -> {

                        }
                    }
                }
            }
        }

    }

    override fun onProductClick(product: ProductsResponseItemDTO) {
        navController?.navigate(R.id.action_productsFragment_to_productDetailFragment,Bundle().apply {
            putString("productId", product.id.toString())
            putString("productImage",product.image.toString())
            putString("productTitle",product.title)
            putString("productCategory",product.category.toString())
            putString("productDescription",product.description.toString())
            putString("productPrice",product.price.toString())
            putString("productRating",product.rating.toString())

        })
    }

    /*
    override fun onProductClick(productsResponseItemDTO: ProductsResponseItemDTO) {
        viewModel.onProductClick()

    }*/


}