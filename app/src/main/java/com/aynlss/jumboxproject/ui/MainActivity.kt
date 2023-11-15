package com.aynlss.jumboxproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.aynlss.jumboxproject.R
import com.aynlss.jumboxproject.common.gone
import com.aynlss.jumboxproject.common.visible
import com.aynlss.jumboxproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import com.aynlss.jumboxproject.common.viewBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding (ActivityMainBinding::inflate)

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.logInFragment,
                R.id.signUpFragment,
                R.id.splashFragment,
                R.id.successFragment
                -> {
                    binding.bottomNavigation.gone()
                }

                else -> {
                    binding.bottomNavigation.visible()
                }
            }
        }
    }
}