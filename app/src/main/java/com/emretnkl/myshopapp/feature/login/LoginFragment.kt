package com.emretnkl.myshopapp.feature.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.emretnkl.myshopapp.R
import com.emretnkl.myshopapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: FragmentLoginBinding
    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        binding.bttnToProducts.setOnClickListener {
            navController!!.navigate(R.id.action_loginFragment_to_productsFragment)
        }

        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.uiEvent.collect {
                    when(it) {
                        is LoginViewEvent.NavigateToMain -> {
                            navController?.navigate(
                                R.id.action_loginFragment_to_productsFragment,
                                null,
                                NavOptions.Builder().setPopUpTo(0,true).build()
                            )
                            Toast.makeText(requireContext(), "Login is successfull", Toast.LENGTH_SHORT).show()
                        }
                        is LoginViewEvent.ShowError -> {
                            Toast.makeText(requireContext(), "Login is failed" , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.bttnLogin.setOnClickListener {
            viewModel.login(
                binding.etLoginEmail.text.trim().toString(),
                binding.etLoginPassword.text.trim().toString()
            )
        }

        binding.tvLoginToRegister.setOnClickListener {
            navController?.navigate(R.id.action_loginFragment_to_registerFragment)
        }

    }

}