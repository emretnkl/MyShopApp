package com.emretnkl.myshopapp.feature.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.emretnkl.myshopapp.R
import com.emretnkl.myshopapp.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ProfileFragment() : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var navController:NavController ?= null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        firebaseAuth = Firebase.auth
        firestore = Firebase.firestore
        val userId = firebaseAuth.currentUser?.uid.toString()

        val docRef: DocumentReference = firestore.collection("users").document(userId)
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document: DocumentSnapshot? = task.getResult()
                if (document != null) {
                    binding.tvProfileUsername.text = document.getString("second")

                } else {
                    Log.d("LOGGER", "No such document")
                }
            } else {
                Log.d("LOGGER", "get failed with ", task.exception)
            }
        }

        binding.tvProfileEmail.text = firebaseAuth.currentUser?.email




        binding.bttnSignout.setOnClickListener {
            firebaseAuth.signOut()
            navController?.navigate(
                resId = R.id.action_profileFragment_to_login_graph,
                null,
                navOptions = NavOptions.Builder().setPopUpTo(0, true).build()
            )
        }

    }

}