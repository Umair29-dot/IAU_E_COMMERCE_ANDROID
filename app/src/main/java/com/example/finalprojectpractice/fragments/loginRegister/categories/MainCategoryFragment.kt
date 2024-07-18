package com.example.finalprojectpractice.fragments.loginRegister.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalprojectpractice.R
import com.example.finalprojectpractice.adapters.BestDealsAdapter
import com.example.finalprojectpractice.adapters.BestProductsAdapter
import com.example.finalprojectpractice.adapters.SpecialProductsAdapter
import com.example.finalprojectpractice.databinding.FragmentMainCategoryBinding
import com.example.finalprojectpractice.util.Resource
import com.example.finalprojectpractice.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainCategoryFragment: Fragment() {

	private lateinit var binding: FragmentMainCategoryBinding
	private lateinit var specialProductsAdapter: SpecialProductsAdapter
	private lateinit var bestDealsAdapter: BestDealsAdapter
	private lateinit var bestProductsAdapter: BestProductsAdapter
	//private val viewModel by viewModels<MainCategoryViewModel>()
	private lateinit var viewModel: MainCategoryViewModel

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentMainCategoryBinding.inflate(inflater)
		viewModel = ViewModelProvider(this).get(MainCategoryViewModel::class.java)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setUpSpecialProductsRecyclerView()
		setUpBestDealsRecyclerView()
		setUpBestProductsRecyclerView()

		viewModel.fetchSpecialProducts()
		viewModel.fetchBestDeals()
		viewModel.fetchBestProducts()

		specialProductsAdapter.onClick = {
			val bundle = Bundle().apply { putParcelable("product", it) }
			findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment, bundle)
		}

		bestDealsAdapter.onClick = {
			val bundle = Bundle().apply { putParcelable("product", it) }
			findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment, bundle)
		}

		bestProductsAdapter.onClick = {
			val bundle = Bundle().apply { putParcelable("product", it) }
			findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment, bundle)
		}

		//Special Products Listener
		lifecycleScope.launch{
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
				viewModel.specialProducts.collectLatest {
					when(it){
						is Resource.Loading -> {
							showLoading()
						}
						is Resource.Success -> {
							hideLoading()
							it.data?.let { it1 ->
								specialProductsAdapter.setData(it1)
							}
						}
						is Resource.Error -> {
							hideLoading()
							Toast.makeText(requireContext() , it.message.toString() , Toast.LENGTH_SHORT).show()
						}
						else -> Unit
					}
				}
			}
		}

		//Best Deals Listener
		lifecycleScope.launch{
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
				viewModel.bestDeals.collectLatest {
					when(it){
						is Resource.Loading -> {
							showLoading()
						}
						is Resource.Success -> {
							hideLoading()
							it.data?.let { it1 ->
								bestDealsAdapter.setData(it1)
							}
						}
						is Resource.Error -> {
							hideLoading()
							Toast.makeText(requireContext() , it.message.toString() , Toast.LENGTH_SHORT).show()
						}
						else -> Unit
					}
				}
			}
		}

		//Best Products Listener
		lifecycleScope.launch{
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
				viewModel.bestProdcuts.collectLatest {
					when(it){
						is Resource.Loading -> {
							showLoading()
						}
						is Resource.Success -> {
							hideLoading()
							it.data?.let { it1 ->
								bestProductsAdapter.setData(it1)
							}
						}
						is Resource.Error -> {
							hideLoading()
							Toast.makeText(requireContext() , it.message.toString() , Toast.LENGTH_SHORT).show()
						}
						else -> Unit
					}
				}
			}
		}

	}

	private fun hideLoading() {
		binding.mainCategoryProgressBar.visibility = View.INVISIBLE
	}

	private fun showLoading() {
		binding.mainCategoryProgressBar.visibility = View.VISIBLE
	}

	private fun setUpSpecialProductsRecyclerView() {
		specialProductsAdapter = SpecialProductsAdapter()
		binding.rvSpecialProducts.apply {
			layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL , false)
			adapter = specialProductsAdapter
		}
	}

	private fun setUpBestDealsRecyclerView() {
		bestDealsAdapter = BestDealsAdapter()
		binding.rvBestDeals.apply {
			layoutManager =  LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL , false)
			adapter = bestDealsAdapter
		}
	}

	private fun setUpBestProductsRecyclerView() {
		bestProductsAdapter = BestProductsAdapter()
		binding.rvBestProducts.apply {
			layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
			adapter = bestProductsAdapter
		}
	}

}