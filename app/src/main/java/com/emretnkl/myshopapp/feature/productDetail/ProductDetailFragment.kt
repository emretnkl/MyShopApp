package com.emretnkl.myshopapp.feature.productDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.emretnkl.myshopapp.databinding.FragmentProductDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private lateinit var binding : FragmentProductDetailBinding
    private val viewModel by viewModels<ProductDetailViewModel>()
    private lateinit var navController: NavController
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    var totalPrice : Float = 0F
    var quantity = 1


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
        firebaseAuth = Firebase.auth
        firestore = Firebase.firestore
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val userId = firebaseAuth.currentUser?.uid.toString()

        val productId = arguments?.getString("productId")?.toInt()
        val productTitle = arguments?.getString("productTitle")
        val productImage = arguments?.getString("productImage")
        val productCategory = arguments?.getString("productCategory")
        val productDescription = arguments?.getString("productDescription")
        val productPrice = arguments?.getString("productPrice")
        val productRating = arguments?.getString("productRating")


        binding.tvProductDetailTotalPrice.text = "$productPrice $"
        binding.tvProductPrice.text = "$productPrice $"
        binding.tvProductDescription.text = productDescription
        binding.tvProductName.text = productTitle
        Glide.with(view.context)
            .load(productImage)
            .into(binding.ivProductImage)

        binding.bttnIncreaseQuantity.setOnClickListener {
            quantity++
            if (productPrice != null) {
                totalPrice = quantity * productPrice.toFloat()
            }
            binding.tvProductQuantity.text = quantity.toString()
            binding.tvProductDetailTotalPrice.text = "$totalPrice $"
        }

        binding.bttnDecreaseQuantity.setOnClickListener {
            if (quantity > 1) {
                quantity--
            }
            if (productPrice != null) {
                totalPrice = quantity * productPrice.toFloat()
            }
            binding.tvProductQuantity.text = quantity.toString()
            binding.tvProductDetailTotalPrice.text = "$totalPrice$"
        }


        binding.bttnAddToBasket.setOnClickListener {
            Log.d("Emre : ", "ProductDetailFragment : " + quantity)
            val product = Product(productId.toString(),productImage,productTitle,productPrice,quantity)
            firestore.collection("users").document(userId).collection("cart").document(productId.toString()).set(product)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(),"Product has been added to cart.",Toast.LENGTH_SHORT).show()
                }
        }

    }

}
data class Product(
    val id: String? = null,
    val image: String? = null,
    val title: String? = null,
    val price: String? = null,
    val quantity: Int? = null
)


