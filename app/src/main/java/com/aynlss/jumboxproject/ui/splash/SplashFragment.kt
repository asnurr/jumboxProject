package com.aynlss.jumboxproject.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aynlss.jumboxproject.R
import com.aynlss.jumboxproject.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import com.aynlss.jumboxproject.common.viewBinding

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val binding by viewBinding(FragmentSplashBinding::bind)

    private val viewModel by viewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
    }

    private fun observeData() {
        viewModel.splashState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SplashState.GoToSignIn -> {
                    findNavController().navigate(SplashFragmentDirections.splashToLogIn())
                }

                SplashState.GoToHome -> {
                    findNavController().navigate(SplashFragmentDirections.splashToMainGraph())
                }
            }
        }
    }
}