package com.aynlss.jumboxproject.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.aynlss.jumboxproject.R
import com.aynlss.jumboxproject.common.gone
import com.aynlss.jumboxproject.common.visible
import com.aynlss.jumboxproject.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import com.aynlss.jumboxproject.common.viewBinding

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val binding by viewBinding(FragmentDetailBinding::bind)

    private val viewModel by viewModels<DetailViewModel>()

    private val args by navArgs<DetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProductDetail(args.id)

        observeData()

        with(binding) {

            btnAddToCart.setOnClickListener {
                viewModel.addToCart(args.id)
            }

            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }

            ivFav.setOnClickListener {
                viewModel.setFavoriteState(args.id)
            }
        }
    }

    private fun observeData() = with(binding) {
        viewModel.detailState.observe(viewLifecycleOwner) { state ->
            when (state) {
                DetailState.Loading -> progressBar.visible()

                is DetailState.SuccessState -> {

                    Glide.with(ivProduct).load(state.product.imageOne).into(ivProduct)
                    progressBar.gone()
                    ratingBar.visible()
                    ivProduct.visible()
                    ivFav.visible()
                    tvTitle.text = state.product.title
                    tvPrice.text = "${state.product.price}₺"
                    tvDescription.text = state.product.description
                    ratingBar.rating = state.product.rate.toFloat()
                    tvCategory.text = state.product.category

                    if (!state.product.saleState) {
                        tvSalePrice.gone()
                    } else {
                        tvSalePrice.text = "${state.product.salePrice}₺"
                    }

                    ivFav.setBackgroundResource(
                        if (state.product.isFav) {
                            R.drawable.ic_fav_selected
                        } else {
                            R.drawable.ic_fav_unselected
                        }
                    )
                }

                is DetailState.EmptyScreen -> {
                    progressBar.gone()
                    tvError.text = state.failMessage
                    tvError.visible()
                    ivError.visible()
                }

                is DetailState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }
}