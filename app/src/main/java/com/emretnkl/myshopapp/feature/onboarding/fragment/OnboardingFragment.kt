package com.emretnkl.myshopapp.feature.onboarding.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emretnkl.myshopapp.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private var layout: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            layout = it.getInt("layout")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(layout,container,false)
    }

    companion object {

        @JvmStatic
        fun newInstance(layout : Int) =
            OnboardingFragment().apply {
                arguments = Bundle().apply {
                    putInt("layout",layout)
                }
            }
    }
}