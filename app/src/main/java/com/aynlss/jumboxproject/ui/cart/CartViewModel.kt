package com.aynlss.jumboxproject.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aynlss.jumboxproject.common.Resource
import com.aynlss.jumboxproject.data.model.response.ProductUI
import com.aynlss.jumboxproject.data.repository.FirebaseRepository
import com.aynlss.jumboxproject.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _cartProductState = MutableLiveData<CartProductState>()
    val cartProductState: LiveData<CartProductState> get() = _cartProductState

    private val _totalPriceAmount = MutableLiveData(0.0)
    val totalPriceAmount: LiveData<Double> = _totalPriceAmount

    fun getCartProducts() = viewModelScope.launch {
        _cartProductState.value = CartProductState.Loading

        when(val result = productRepository.getCartProducts(firebaseRepository.getUserId())) {
            is Resource.Success -> {
                _cartProductState.value = CartProductState.SuccessState(result.data)
                _totalPriceAmount.value = result.data.sumOf { product ->
                    if (product.saleState) {
                        product.salePrice
                    } else {
                        product.price
                    }
                }
            }
            is Resource.Error -> {
                _cartProductState.value = CartProductState.ShowPopUp(result.errorMessage)
            }
            is Resource.Fail -> {
                _cartProductState.value = CartProductState.EmptyScreen(result.failMessage)
            }
        }
    }
    fun deleteFromCart(productId: Int) = viewModelScope.launch {
        productRepository.deleteFromCart(firebaseRepository.getUserId(), productId)
        getCartProducts()
        resetTotalAmount()
    }
    fun clearCart() = viewModelScope.launch {
        productRepository.clearCart(firebaseRepository.getUserId())
        getCartProducts()
        resetTotalAmount()
    }
    private fun resetTotalAmount() {
        _totalPriceAmount.value = 0.0
    }
}
sealed interface CartProductState {
    object  Loading : CartProductState
    data class SuccessState(val products: List<ProductUI>) : CartProductState
    data class EmptyScreen(val failMessage : String) : CartProductState
    data class ShowPopUp(val errorMessage : String) : CartProductState
}

