package com.emretnkl.myshopapp.feature.onboarding.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.emretnkl.myshopapp.feature.onboarding.fragment.OnboardingFragment

class OnboardingAdapter(
    fragmentActivity: FragmentActivity,
    private val layouts: List<Int>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return layouts.size
    }

    override fun createFragment(position: Int): Fragment {
        return OnboardingFragment.newInstance(layouts[position])
    }

}