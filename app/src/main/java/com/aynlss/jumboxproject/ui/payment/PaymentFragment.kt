package com.aynlss.jumboxproject.ui.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.aynlss.jumboxproject.R
import com.aynlss.jumboxproject.common.gone
import com.aynlss.jumboxproject.common.visible
import com.aynlss.jumboxproject.databinding.FragmentPaymentBinding
import dagger.hilt.android.AndroidEntryPoint
import com.aynlss.jumboxproject.common.viewBinding


@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private val binding by viewBinding (FragmentPaymentBinding::bind)

    private val viewModel by viewModels<PaymentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            btnBuyNow.setOnClickListener {
                viewModel.makePayment(
                    etCardFullname.text.toString(),
                    etCardNumber.text.toString(),
                    etMonth.text.toString(),
                    etYear.text.toString(),
                    etCvc.text.toString(),
                    etMail.text.toString()
                )
            }

            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.paymentState.observe(viewLifecycleOwner) {state ->
            when(state) {
                PaymentState.Loading -> progressBar.visible()

                is PaymentState.GoSuccess -> {
                    progressBar.gone()
                    findNavController().navigate(PaymentFragmentDirections.paymentToSuccess())
                }
                is PaymentState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }
}