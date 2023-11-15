package com.aynlss.jumboxproject.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.aynlss.jumboxproject.R
import com.aynlss.jumboxproject.common.gone
import com.aynlss.jumboxproject.common.visible
import com.aynlss.jumboxproject.data.model.response.ProductUI
import com.aynlss.jumboxproject.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import com.aynlss.jumboxproject.common.viewBinding


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding (FragmentHomeBinding::bind)

    private val viewModel by viewModels<HomeViewModel>()

    private val productAdapter = ProductsAdapter(onProductClick = ::onProductClick, onFavClick = ::onFavClick)

    private val saleProductsAdapter = SaleProductsAdapter(onProductClick = ::onProductClick, onFavClick = ::onFavClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProducts()
        viewModel.getSaleProducts()

        observeData()

        with(binding) {

            rvProducts.adapter = productAdapter
            rvSaleProducts.adapter = saleProductsAdapter

            ivLogOut.setOnClickListener {
                viewModel.logOut()
            }
        }
    }

    private fun observeData() = with(binding) {
        viewModel.homeState.observe(viewLifecycleOwner) {state ->
            when (state) {
                HomeState.Loading -> progressBar.visible()

                is HomeState.SuccessState -> {
                    progressBar.gone()
                    productAdapter.submitList(state.products)
                }

                is HomeState.EmptyScreen -> {
                    progressBar.gone()
                    rvProducts.gone()
                    tvError.text = state.failMessage
                    tvError.visible()
                    ivError.visible()
                }

                is HomeState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }

                HomeState.GoToSignIn -> {
                    findNavController().navigate(R.id.homeToMain)
                }
            }
        }

        viewModel.saleState.observe(viewLifecycleOwner) { it ->
            when (it) {
                SaleState.Loading -> progressBar.visible()

                is SaleState.SuccessState -> {
                    progressBar.gone()
                    saleProductsAdapter.submitList(it.products)
                }

                is SaleState.EmptyScreen -> {
                    progressBar.gone()
                    rvSaleProducts.gone()
                    tvError.text = it.failMessage
                    tvError.visible()
                    ivError.visible()
                }

                is SaleState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), it.errorMessage, 1000).show()
                }
            }
        }
    }

    private fun onProductClick(id: Int){
        findNavController().navigate(HomeFragmentDirections.homeToDetail(id))
    }

    private fun onFavClick(product: ProductUI) {
        viewModel.setFavoriteState(product)
    }
}