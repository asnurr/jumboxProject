package com.aynlss.jumboxproject.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.aynlss.jumboxproject.data.model.response.ProductUI
import com.aynlss.jumboxproject.databinding.ItemCartProductBinding


class CartAdapter(
    private val onProductClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : ListAdapter<ProductUI, CartAdapter.CartViewHolder>(ProductDiffUtilCallBack())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onProductClick,
            onDeleteClick
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) = holder.bind(getItem(position))

    class CartViewHolder(
        private val binding: ItemCartProductBinding,
        private val onProductClick: (Int) -> Unit,
        private val onDeleteClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) {
            with(binding) {
                tvCartTitle.text = product.title
                tvCartPrice.text = if(product.saleState) {
                    "${product.salePrice} ₺"
                } else "${product.price} ₺"

                Glide.with(ivCartProduct).load(product.imageOne).into(ivCartProduct)

                root.setOnClickListener {
                    onProductClick(product.id)
                }

                imgDelete.setOnClickListener {
                    onDeleteClick(product.id)
                }
            }
        }
    }
}

class ProductDiffUtilCallBack : DiffUtil.ItemCallback<ProductUI>() {
    override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
        return oldItem == newItem
    }
}
