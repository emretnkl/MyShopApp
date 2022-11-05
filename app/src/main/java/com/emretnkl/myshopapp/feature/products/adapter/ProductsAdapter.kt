package com.emretnkl.myshopapp.feature.products.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emretnkl.myshopapp.data.model.ProductsResponseItem
import com.emretnkl.myshopapp.data.model.ProductsResponseItemDTO
import com.emretnkl.myshopapp.databinding.ItemProductListBinding

class ProductsAdapter(private val listener: OnProductClickListener) :
    ListAdapter<ProductsResponseItemDTO, ProductsAdapter.ProductViewHolder>(ProductDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder (
            ItemProductListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    class ProductViewHolder(private val binding: ItemProductListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductsResponseItemDTO, listener: OnProductClickListener) {
            binding.dataHolder = product

            binding.cardViewProduct.setOnClickListener {
                listener.onProductClick(product)
            }
            binding.listener = listener
            binding.executePendingBindings()

        }

    }

    class ProductDiffUtil : DiffUtil.ItemCallback<ProductsResponseItemDTO>(){
        override fun areItemsTheSame(
            oldItem: ProductsResponseItemDTO,
            newItem: ProductsResponseItemDTO
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProductsResponseItemDTO,
            newItem: ProductsResponseItemDTO
        ): Boolean {
            return oldItem == newItem
        }
    }

}

interface OnProductClickListener {
    fun onProductClick(product: ProductsResponseItemDTO)
}
