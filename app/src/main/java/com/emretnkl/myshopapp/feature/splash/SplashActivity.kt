package com.emretnkl.myshopapp.feature.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.emretnkl.myshopapp.MainActivity
import com.emretnkl.myshopapp.R
import com.emretnkl.myshopapp.feature.onboarding.OnboardingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val viewModel by viewModels<SplashViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        navigateToMain()

        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.uiEvent.collect{
                    when(it) {
                        is SplashViewEvent.NavigateToOnboarding -> {
                            navigateToOnboarding()
                        }
                        is SplashViewEvent.NavigateToMain -> {
                            navigateToMain()
                        }
                        is SplashViewEvent.NavigateToLogin -> {

                        }
                    }
                }
            }
        }
    }

    private fun navigateToOnboarding() {
        lifecycleScope.launch {
            delay(2000)
            val intent = Intent(this@SplashActivity, OnboardingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun navigateToMain() {
        lifecycleScope.launch {
            delay(2000)
            val intent = Intent(this@SplashActivity, OnboardingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}