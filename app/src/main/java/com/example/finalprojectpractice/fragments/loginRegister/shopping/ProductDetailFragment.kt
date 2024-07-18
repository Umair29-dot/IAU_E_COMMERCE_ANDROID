package com.example.finalprojectpractice.fragments.loginRegister.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalprojectpractice.R
import com.example.finalprojectpractice.activities.ShoppingActivity
import com.example.finalprojectpractice.adapters.ColorsAdapter
import com.example.finalprojectpractice.adapters.SizesAdapter
import com.example.finalprojectpractice.adapters.ViewPager2Images
import com.example.finalprojectpractice.data.CartProduct
import com.example.finalprojectpractice.databinding.FragmentProductDetailBinding
import com.example.finalprojectpractice.util.Resource
import com.example.finalprojectpractice.viewmodel.DetailViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailFragment: Fragment() {

	private val args by navArgs<ProductDetailFragmentArgs>()
	private lateinit var binding: FragmentProductDetailBinding
	private val viewPagerAdapter by lazy { ViewPager2Images() }
	private val sizesAdapter by lazy { SizesAdapter() }
	private val colorsAdapter by lazy { ColorsAdapter() }
	private var selectedColor: String? = null
	private var selectedSize: String? = null
	private val viewModel by viewModels<DetailViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentProductDetailBinding.inflate(inflater)

		val bottomNavigationView = (activity as ShoppingActivity).findViewById<BottomNavigationView>(
			R.id.bottomNavigation)

		bottomNavigationView.visibility = View.GONE

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val product = args.product

		setupSizesRV()
		setupColorsRV()
		setupViewPager()

		binding.apply {
			tvProductName.setText(product.name)
			tvProductPrice.setText(product.price.toString())
			tvProductDescription.setText(product.description)
		}

		sizesAdapter.onItemClick = {
			selectedSize = it
		}

		colorsAdapter.onItemClick = {
			selectedColor = it
		}

		binding.btnAddToCart.setOnClickListener {
			viewModel.addUpdateProductsInCart(CartProduct(product, 1, selectedColor, selectedSize))
		}

		binding.imgClose.setOnClickListener {
			findNavController().popBackStack()
		}

		viewPagerAdapter.differ.submitList(product.images)
		product.colors?.let { colorsAdapter.differ.submitList(it) }
		product.sizes?.let { sizesAdapter.differ.submitList(it) }

		lifecycleScope.launchWhenStarted {
			viewModel.addToCart.collectLatest {
				when(it) {
					is Resource.Loading -> {
						Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
					}
					is Resource.Success -> {
						Toast.makeText(requireContext(), "Added to cart successfully", Toast.LENGTH_SHORT).show()
					}
					is Resource.Error -> {
						Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
					}
					else -> Unit
				}
			}
		}

	}

	private fun setupViewPager() {
		binding.viewPagerProductImages.apply {
			adapter = viewPagerAdapter
		}
	}

	private fun setupColorsRV() {
		binding.rvColors.apply {
			adapter = colorsAdapter
			layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
		}
	}

	private fun setupSizesRV() {
		binding.rvSizes.apply {
			adapter = sizesAdapter
			layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		val bottomNavigationView = (activity as ShoppingActivity).findViewById<BottomNavigationView>(
			R.id.bottomNavigation)

		bottomNavigationView.visibility = View.VISIBLE
	}

}