package com.aynlss.jumboxproject.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.aynlss.jumboxproject.data.model.response.ProductUI
import com.aynlss.jumboxproject.databinding.ItemSearchProductBinding


class SearchAdapter(
    private val onProductClick: (Int) -> Unit
): ListAdapter<ProductUI, SearchAdapter.SearchViewHolder>(SearchDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemSearchProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onProductClick
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) = holder.bind(getItem(position))

    class SearchViewHolder(
        private val binding: ItemSearchProductBinding,
        private val onProductClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) {
            with(binding) {
                tvTitle.text = product.title

                if(!product.saleState) {
                    tvPrice.text = "${product.price} "
                } else {
                    tvPrice.text = "${product.salePrice} "
                }

                ratingBar.rating = (product.rate).toFloat()

                Glide.with(imgProduct).load(product.imageOne).into(imgProduct)

                root.setOnClickListener {
                    onProductClick(product.id)
                }
            }
        }
    }

    class SearchDiffUtilCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }
}