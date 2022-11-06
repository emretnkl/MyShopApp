package com.emretnkl.myshopapp.feature.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emretnkl.myshopapp.data.model.ProductsResponseItem
import com.emretnkl.myshopapp.data.model.ProductsResponseItemDTO
import com.emretnkl.myshopapp.databinding.ItemSearchListBinding

class SearchAdapter(private val listener: OnSearchClickListener) :
    ListAdapter<ProductsResponseItemDTO, SearchAdapter.SearchViewHolder>(SearchDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder (
            ItemSearchListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    class SearchViewHolder(private val binding: ItemSearchListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(search: ProductsResponseItemDTO, listener: OnSearchClickListener) {
            binding.dataHolder = search

            binding.cardViewSearch.setOnClickListener {
                listener.onSearchClick(search)
            }
            binding.listener = listener
            binding.executePendingBindings()

        }

    }

    class SearchDiffUtil : DiffUtil.ItemCallback<ProductsResponseItemDTO>(){
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

interface OnSearchClickListener {
    fun onSearchClick(search: ProductsResponseItemDTO)
}
