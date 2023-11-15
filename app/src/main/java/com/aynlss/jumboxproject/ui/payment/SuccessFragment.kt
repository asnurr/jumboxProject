package com.aynlss.jumboxproject.ui.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.aynlss.jumboxproject.R
import com.aynlss.jumboxproject.databinding.FragmentSuccessBinding
import com.aynlss.jumboxproject.common.viewBinding


class SuccessFragment : Fragment(R.layout.fragment_success) {

    private val binding by viewBinding (FragmentSuccessBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGoHome.setOnClickListener {
            findNavController().navigate(SuccessFragmentDirections.successToHome())
        }
    }
}