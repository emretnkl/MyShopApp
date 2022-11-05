package com.emretnkl.myshopapp.feature.productDetail

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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.emretnkl.myshopapp.R
import com.emretnkl.myshopapp.databinding.FragmentProductDetailBinding
import com.emretnkl.myshopapp.feature.products.ProductViewEvent
import com.emretnkl.myshopapp.feature.products.ProductViewState
import com.emretnkl.myshopapp.feature.products.adapter.ProductsAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private lateinit var binding : FragmentProductDetailBinding
    private val viewModel by viewModels<ProductDetailViewModel>()
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val productId = arguments?.getString("productId")?.toInt()
        val productTitle = arguments?.getString("productTitle")
        val productImage = arguments?.getString("productImage")
        val productCategory = arguments?.getString("productCategory")
        val productDescription = arguments?.getString("productDescription")
        val productPrice = arguments?.getString("productPrice")
        val productRating = arguments?.getString("productRating")

        binding.tvProductPrice.text = productPrice + " $"
        binding.tvProductDescription.text = productDescription
        binding.tvProductName.text = productTitle
        Glide.with(view.context)
            .load(productImage)
            .into(binding.ivProductImage)


/*
            putString("productId", product.id.toString())
            putString("productImage",product.image.toString())
            putString("productTitle",product.title)
            putString("productCategory",product.category.toString())
            putString("productDescription",product.description.toString())
            putString("productPrice",product.price.toString())
            putString("productRating",product.rating.toString())


        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.uiState.collect{
                    when(it){
                        is ProductDetailViewState.Success -> {
                            it.products?.let {
                                binding.tvProductName.text = it.first().title
                                binding.tvProductDescription.text = it.first().description
                                binding.tvProductPrice.text = it.first().price.toString()

                            }

                        }
                        else -> {}
                    }
                    }

                    }
            launch {
                viewModel.uiEvent.collect{
                    when(it) {
                        is ProductDetailViewEvent.ShowError -> {
                            Snackbar.make(binding.root,it.message.toString(), Snackbar.LENGTH_SHORT).show()
                        }

                        else -> {}
                    }
                }
            }
        } */

    }

}


