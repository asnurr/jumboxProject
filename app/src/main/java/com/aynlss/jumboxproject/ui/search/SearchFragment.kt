package com.aynlss.jumboxproject.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.aynlss.jumboxproject.R
import com.aynlss.jumboxproject.common.gone
import com.aynlss.jumboxproject.common.visible
import com.aynlss.jumboxproject.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import com.aynlss.jumboxproject.common.viewBinding

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding by viewBinding (FragmentSearchBinding::bind)

    private val searchViewModel by viewModels<SearchViewModel>()

    private val searchAdapter = SearchAdapter(onProductClick = ::onProductClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()

        with(binding) {

            rvSearchProducts.adapter = searchAdapter

            with(searchViewModel) {
                searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText != null) {
                            if (newText.length >= 3) {
                                searchProduct(newText)
                                getProductsByCategory(newText)
                            } else {
                                searchAdapter.submitList(emptyList())
                            }
                        }
                        return false
                    }
                })
            }
        }
    }

    private fun observeData() = with(binding) {
        searchViewModel.searchState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SearchState.Loading -> progressBar.visible()

                is SearchState.SuccessState -> {
                    progressBar.gone()
                    ivError.gone()
                    tvError.gone()
                    searchAdapter.submitList(state.products)
                }

                is SearchState.EmptyScreen -> {
                    progressBar.gone()
                    rvSearchProducts.gone()
                    tvError.text = state.failMessage
                    tvError.visible()
                    ivError.visible()
                }

                is SearchState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }

    private fun onProductClick(id: Int){
        findNavController().navigate(SearchFragmentDirections.searchToDetail(id))
    }
}