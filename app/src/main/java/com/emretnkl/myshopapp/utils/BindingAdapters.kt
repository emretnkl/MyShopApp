package com.emretnkl.myshopapp.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("loadImage")
    fun loadImage(view: ImageView, image: String) {
        Glide.with(view.context)
            .load(image)
            .into(view)
    }
}