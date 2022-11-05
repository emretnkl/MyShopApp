package com.emretnkl.myshopapp.feature.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.emretnkl.myshopapp.R
import com.emretnkl.myshopapp.databinding.FragmentRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private val viewModel by viewModels<RegisterViewModel>()
    private lateinit var binding: FragmentRegisterBinding
    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.uiEvent.collect {
                    when (it) {
                        is RegisterViewEvent.NavigateToMain -> {
                            navController?.navigate(
                                resId = R.id.action_registerFragment_to_productsFragment,
                                null,
                                navOptions = NavOptions.Builder().setPopUpTo(0, true).build()
                            )
                            Snackbar.make(requireView(), "You are registered successfully.", Snackbar.LENGTH_SHORT).show()
                        }
                        is RegisterViewEvent.ShowError -> {
                            Snackbar.make(requireView(), it.error, Snackbar.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }
                }
            }
        }

        initViews()

        binding.tvRegisterToLogin.setOnClickListener {
            navController?.navigate(R.id.action_registerFragment_to_loginFragment)
        }

    }

    private fun initViews() {
        with(binding) {
            bttnRegister.setOnClickListener {
                viewModel.register(
                    etRegisterEmail.text.trim().toString(),
                    etRegisterPassword.text.trim().toString(),
                    etRegisterConfirmPassword.text.trim().toString(),
                    etRegisterUsername.text.trim().toString()
                )
            }
        }
    }
}