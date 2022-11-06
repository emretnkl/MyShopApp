package com.emretnkl.myshopapp.feature.basket

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
import com.emretnkl.myshopapp.R
import com.emretnkl.myshopapp.data.model.BasketProduct
import com.emretnkl.myshopapp.databinding.FragmentBasketBinding
import com.emretnkl.myshopapp.feature.basket.adapter.BasketOnClickListener
import com.emretnkl.myshopapp.feature.basket.adapter.BasketProductAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BasketFragment : Fragment(), BasketOnClickListener {

    private lateinit var binding: FragmentBasketBinding
    private lateinit var navController: NavController
    private val firebaseAuth = Firebase.auth
    private val firestore = Firebase.firestore
    private lateinit var basketProductArrayList: ArrayList<BasketProduct>
    private lateinit var basketProductAdapter: BasketProductAdapter
    private val viewModel by viewModels<BasketViewModel>()

    private var userId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBasketBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth.currentUser?.uid?.let {
            userId = it
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        navController = findNavController()
        basketProductArrayList = arrayListOf()
        basketProductAdapter = BasketProductAdapter(basketProductArrayList).apply {
            setBasketOnClickListener(this@BasketFragment)
        }
        val userId = firebaseAuth.currentUser?.uid.toString()

        binding.rvBasketItems.adapter = basketProductAdapter

        eventChangeListener()



        binding.bttnPurchase.setOnClickListener {
            Toast.makeText(requireContext(),"Purchasing is succesfull. We will deliver the products to you as soon as possible.",Toast.LENGTH_LONG).show()
            firestore.collection("users").document(userId).collection("cart").document().delete()
            navController.navigate(R.id.action_basketFragment_to_productsFragment)
        }


    }

    private fun eventChangeListener() {
        val userId = firebaseAuth.currentUser?.uid.toString()

        firestore.collection("users").document(userId).collection("cart").
                addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {

                        if (error != null) {
                            Log.e("Firestore Error",error.message.toString())
                            return
                        }

                        if (value?.documentChanges == null) {
                            return
                        }

                        for (dc : DocumentChange in value.documentChanges) {
                            val item = dc.document.toObject(BasketProduct::class.java)
                            val price = item.price?.toFloat()

                            if (dc.type == DocumentChange.Type.ADDED || dc.type == DocumentChange.Type.MODIFIED) {
                                basketProductArrayList.add(dc.document.toObject(BasketProduct::class.java))
                                viewModel.increaseTotalPrice(price)
                            }

                            if (dc.type == DocumentChange.Type.REMOVED){
                                basketProductArrayList.remove(dc.document.toObject(BasketProduct::class.java))
                                viewModel.decreaseTotalPrice(price)
                            }
                        }

                        basketProductAdapter.notifyDataSetChanged()
                    }
                })


    }

    override fun onIncreaseButtonClicked(item: BasketProduct) {
        updateBasketCollection(item, true)
    }

    override fun onDecreaseButtonClicked(item: BasketProduct) {
        item.quantity?.let { quantity ->
            if (quantity == 1) {
                deleteItemFromCollection(item)
            } else {
                updateBasketCollection(item, false)
            }
        }
    }

    private fun updateBasketCollection(item: BasketProduct, shouldIncrease: Boolean) {
        item.id?.let { itemId ->
            firestore
                .collection("users")
                .document(userId)
                .collection("cart")
                .document(itemId)
                .set(
                    item.apply {
                        quantity = (quantity ?: 0) + getValueByShouldIncreaseLogic(shouldIncrease)
                    }
                )
                .addOnSuccessListener {
                    Toast.makeText(requireContext(),"Basket updated",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun deleteItemFromCollection(item: BasketProduct) {
        item.id?.let { itemId ->
            firestore
                .collection("users")
                .document(userId)
                .collection("cart")
                .document(itemId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(requireContext(),"item deleted",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun getValueByShouldIncreaseLogic(shouldIncrease: Boolean): Int {
        return if (shouldIncrease) 1 else -1
    }

}
