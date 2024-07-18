package com.example.finalprojectpractice.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.finalprojectpractice.R
import com.example.finalprojectpractice.databinding.ActivityShoppingBinding
import com.example.finalprojectpractice.util.Constants
import com.example.finalprojectpractice.util.Resource
import com.example.finalprojectpractice.viewmodel.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

	val binding by lazy {
		ActivityShoppingBinding.inflate(layoutInflater)
	}

	val viewModel by viewModels<CartViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		FirebaseMessaging.getInstance().subscribeToTopic(Constants.TOPIC) //For Notifications

		val navController = findNavController(R.id.shoppingHostFragment)
		binding.bottomNavigation.setupWithNavController(navController)

		//Creating Cart Fragment badge
		lifecycleScope.launchWhenStarted {
			viewModel.cartProducts.collectLatest {
				when(it) {
					is Resource.Success -> {
						val count = it.data?.size ?: 0
						val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
						bottomNavigation.getOrCreateBadge(R.id.cartFragment) //This id is inside the menu
							.apply {
								number = count
								backgroundColor = Color.BLUE
							}
					}

					else -> {}
				}
			}
		}

	}
}