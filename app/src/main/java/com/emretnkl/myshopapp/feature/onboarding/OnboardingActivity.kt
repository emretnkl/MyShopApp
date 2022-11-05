package com.emretnkl.myshopapp.feature.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.emretnkl.myshopapp.feature.main.MainActivity
import com.emretnkl.myshopapp.R
import com.emretnkl.myshopapp.databinding.ActivityOnboardingBinding
import com.emretnkl.myshopapp.feature.onboarding.adapter.OnboardingAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    private val viewModel by viewModels<OnboardingViewModel>()
    private lateinit var binding : ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = OnboardingAdapter(this,prepareOnboardingItems())
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
        }.attach()

        initViews()
    }

    private fun initViews() {
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.isLastPage = position == 2
                if (position != 0) {
                    binding.btnPrev.visibility = VISIBLE
                } else {
                    binding.btnPrev.visibility = GONE
                }
            }
        })

        binding.btnSkip.setOnClickListener {
            if (binding.viewPager.currentItem == 2) {
                skipOnboarding()
            } else {
                binding.viewPager.setCurrentItem(binding.viewPager.currentItem.plus(1), true)
            }
        }

        binding.btnPrev.setOnClickListener {
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem.minus(1), true)
        }
    }

    private fun skipOnboarding() {
        viewModel.setOnboardingStatus()
        navigateToMain()

    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(MainActivity.KEY_NAVIGATE_HOME, false)
        startActivity(intent)
        finish()
    }

    private fun prepareOnboardingItems(): List<Int> {
        return listOf(
            R.layout.item_onboarding,
            R.layout.item_onboarding_two,
            R.layout.item_onboarding_three
        )

    }
}