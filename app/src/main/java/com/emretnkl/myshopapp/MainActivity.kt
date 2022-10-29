package com.emretnkl.myshopapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.emretnkl.myshopapp.data.remote.api.ProductsService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}