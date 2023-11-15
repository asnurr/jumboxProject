package com.aynlss.jumboxproject.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.aynlss.jumboxproject.R
import com.aynlss.jumboxproject.common.strike
import com.aynlss.jumboxproject.data.model.response.ProductUI
import com.aynlss.jumboxproject.databinding.ItemSaleProductBinding


class SaleProductsAdapter(
    private val onProductClick: (Int) -> Unit,
    private val onFavClick: (ProductUI) -> Unit
) : ListAdapter<ProductUI, SaleProductsAdapter.ProductViewHolder>(SaleProductDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemSaleProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onProductClick,
            onFavClick
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) = holder.bind(getItem(position))

    class ProductViewHolder(
        private val binding: ItemSaleProductBinding,
        private val onProductClick: (Int) -> Unit,
        private val onFavClick: (ProductUI) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) {
            with(binding) {
                tvTitle.text = product.title
                tvPrice.text = "${product.price} "
                tvSalePrice.text = "${product.salePrice} "
                tvPrice.strike = true

                ivSaleFavorite.setBackgroundResource(
                    if (product.isFav) R.drawable.ic_fav_selected
                    else R.drawable.ic_fav_unselected
                )

                Glide.with(ivProduct).load(product.imageOne).into(ivProduct)

                root.setOnClickListener {
                    onProductClick(product.id)
                }

                ivSaleFavorite.setOnClickListener {
                    onFavClick(product)
                }
            }
        }
    }

    class SaleProductDiffUtilCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }
}