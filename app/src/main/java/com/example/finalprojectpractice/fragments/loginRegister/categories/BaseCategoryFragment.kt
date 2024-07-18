package com.example.finalprojectpractice.fragments.loginRegister.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalprojectpractice.adapters.BestProductsAdapter
import com.example.finalprojectpractice.adapters.CategoryOfferProductsAdapter
import com.example.finalprojectpractice.databinding.FragmentBaseCategoryBinding

open class BaseCategoryFragment: Fragment() {

	private lateinit var binding: FragmentBaseCategoryBinding
	protected val offerProductAdapter: CategoryOfferProductsAdapter by lazy { CategoryOfferProductsAdapter() }
	protected val bestProductsAdapter: BestProductsAdapter by lazy { BestProductsAdapter() }

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentBaseCategoryBinding.inflate(inflater)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		bestProductsRecyclerView()
		setOfferRecyclerView()

	}

	private fun bestProductsRecyclerView() {
		binding.rvBestProducts.apply {
			layoutManager = GridLayoutManager(requireContext() , 2 , GridLayoutManager.VERTICAL , false)
			adapter = bestProductsAdapter
		}
	}

	private fun setOfferRecyclerView() {
		binding.rvOfferProducts.apply {
			layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL , false )
			adapter = offerProductAdapter
		}
	}

	protected fun showLoading() {
		binding.ProgressBar.visibility = View.VISIBLE
	}

	protected fun hideLoading() {
		binding.ProgressBar.visibility = View.INVISIBLE
	}

}



