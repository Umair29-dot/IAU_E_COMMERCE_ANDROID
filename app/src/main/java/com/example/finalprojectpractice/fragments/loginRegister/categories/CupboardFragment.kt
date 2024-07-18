package com.example.finalprojectpractice.fragments.loginRegister.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.finalprojectpractice.R
import com.example.finalprojectpractice.data.Category
import com.example.finalprojectpractice.databinding.FragmentBaseCategoryBinding
import com.example.finalprojectpractice.util.Resource
import com.example.finalprojectpractice.viewmodel.ChairViewModel
import com.example.finalprojectpractice.viewmodel.CupboardViewModel
import com.example.finalprojectpractice.viewmodel.factory.ViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
@AndroidEntryPoint
class CupboardFragment: BaseCategoryFragment() {

	@Inject
	lateinit var firestore: FirebaseFirestore

	val viewModel by viewModels<CupboardViewModel> {
		ViewModelFactory(firestore, Category.Cupboard)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewModel.fetchOfferProducts()
		viewModel.fetchBestProducts()

		lifecycleScope.launchWhenStarted {
			//viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
			viewModel.offerProducts.collectLatest {
				when(it) {
					is Resource.Loading -> {
						showLoading()
					}

					is Resource.Success -> {
						hideLoading()
						offerProductAdapter.differ.submitList(it.data)
					}

					is Resource.Error -> {
						hideLoading()
						Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
					}

					else -> Unit
				}
			}
			//}
		}

		lifecycleScope.launchWhenStarted {
			//viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
			viewModel.bestProducts.collectLatest {
				when(it) {
					is Resource.Loading -> {
						showLoading()
					}

					is Resource.Success -> {
						hideLoading()
						it.data?.let { it1 -> bestProductsAdapter.setData(it1) }
					}

					is Resource.Error -> {
						hideLoading()
						Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
					}

					else -> Unit
				}
			}
		}
		//}

		offerProductAdapter.onClick = {
			val bundle = Bundle().apply { putParcelable("product", it) }
			findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment, bundle)
		}

		bestProductsAdapter.onClick = {
			val bundle = Bundle().apply { putParcelable("product", it) }
			findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment, bundle)
		}

	}

}



