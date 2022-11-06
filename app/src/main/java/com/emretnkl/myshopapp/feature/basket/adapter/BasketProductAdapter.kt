package com.emretnkl.myshopapp.feature.basket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emretnkl.myshopapp.R
import com.emretnkl.myshopapp.data.model.BasketProduct

class BasketProductAdapter(private val basketProductList : ArrayList<BasketProduct>) : RecyclerView.Adapter<BasketProductAdapter.BasketProductViewHolder>() {

    var listener: BasketOnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_basket_product,parent,false)

        return BasketProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BasketProductViewHolder, position: Int) {

        val basketProduct : BasketProduct = basketProductList[position]

        holder.title.text = basketProduct.title
        holder.price.text = basketProduct.price
        holder.quantity.text = basketProduct.quantity.toString()
        Glide.with(holder.itemView.context)
            .load(basketProduct.image)
            .into(holder.image)

        holder.increaseButton.setOnClickListener {
            listener?.onIncreaseButtonClicked(basketProduct)
        }

        holder.decreaseButton.setOnClickListener {
            listener?.onDecreaseButtonClicked(basketProduct)
        }
    }

    override fun getItemCount(): Int {
        return basketProductList.size
    }

    class BasketProductViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        //id image title price quantity

        val image : ImageView = itemView.findViewById(R.id.ivBasketItemImage)
        val title : TextView = itemView.findViewById(R.id.tvBasketItemTitle)
        val price : TextView = itemView.findViewById(R.id.tvBasketItemPrice)
        val quantity : TextView = itemView.findViewById(R.id.tvBasketItemQuantity)
        val increaseButton: TextView = itemView.findViewById(R.id.bttnIncreaseBasketQuantity)
        val decreaseButton: TextView = itemView.findViewById(R.id.bttnDecreaseBasketQuantity)
    }

    fun setBasketOnClickListener(listener: BasketOnClickListener){
        this.listener = listener
    }

}
interface BasketOnClickListener {

    fun onIncreaseButtonClicked(item: BasketProduct)

    fun onDecreaseButtonClicked(item: BasketProduct)

}