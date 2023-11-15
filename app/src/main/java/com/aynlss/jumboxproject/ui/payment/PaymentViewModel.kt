package com.aynlss.jumboxproject.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aynlss.jumboxproject.data.repository.FirebaseRepository
import com.aynlss.jumboxproject.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _paymentState = MutableLiveData<PaymentState>()
    val paymentState: LiveData<PaymentState> get() = _paymentState

    fun makePayment(fullName: String, cardNumber: String, month: String, year: String, cvc: String, mail: String) = viewModelScope.launch {
        _paymentState.value = PaymentState.Loading

        val errorMessage = when {
            fullName.isEmpty() -> "Name cannot be left blank!"
            cardNumber.isEmpty() || cardNumber.length != 16 -> "Invalid card number!"
            month.isEmpty() -> "Month cannot be left blank!"
            year.isEmpty() || year.length < 4 -> "Invalid year!"
            cvc.isEmpty() || cvc.length < 3 -> "Invalid CVC!"
            mail.isEmpty() -> "Mail cannot be left blank!"
            else -> null
        }

        if (errorMessage != null) {
            _paymentState.value = PaymentState.ShowPopUp(errorMessage)
        } else {
            _paymentState.value = PaymentState.GoSuccess
            productRepository.clearCart(firebaseRepository.getUserId())
        }
    }
}

sealed interface PaymentState {
    object  Loading : PaymentState
    object GoSuccess : PaymentState
    data class ShowPopUp(val errorMessage: String) : PaymentState
}