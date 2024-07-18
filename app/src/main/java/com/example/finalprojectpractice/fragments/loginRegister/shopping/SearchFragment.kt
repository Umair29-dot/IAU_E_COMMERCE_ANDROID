package com.example.finalprojectpractice.fragments.loginRegister.shopping

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalprojectpractice.R
import com.example.finalprojectpractice.adapters.SearchAdapter
import com.example.finalprojectpractice.databinding.FragmentSearchBinding
import com.example.finalprojectpractice.util.Resource
import com.example.finalprojectpractice.viewmodel.SearchViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment: Fragment() {

	private lateinit var binding: FragmentSearchBinding
	private val viewModel by viewModels<SearchViewModel>()
	private lateinit var searchAdapter: SearchAdapter

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentSearchBinding.inflate(inflater)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setUpSearchRecyclerView()

		observeLiveData()

		binding.svSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
			override fun onQueryTextSubmit(p0: String?): Boolean {
				p0?.let {
					viewModel.searchProductWithName(it.trim())
				}
				return true
			}

			override fun onQueryTextChange(p0: String?): Boolean {
				return false
			}
		})

		searchAdapter.onClick = {
			val bundle = Bundle().apply { putParcelable("product", it) }
			findNavController().navigate(R.id.action_searchFragment_to_productDetailFragment, bundle)
		}

	}

	private fun setUpSearchRecyclerView() {
		searchAdapter = SearchAdapter()
		binding.rvSearch.apply {
			layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.VERTICAL , false)
			adapter = searchAdapter
		}
	}

	private fun observeLiveData() {
		viewModel.searchProducts.observe(viewLifecycleOwner, Observer {
			when(it) {
				is Resource.Loading -> {
					binding.pbSearch.visibility = View.VISIBLE
				}

				is Resource.Success -> {
					binding.pbSearch.visibility = View.GONE
					it.data?.let { it1 ->
						searchAdapter.setData(it1)
					}
					Log.d("queryy", it.data.toString())
				}

				is Resource.Error -> {
					binding.pbSearch.visibility = View.GONE
					Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG)
				}

				else -> {}
			}
		})
	}


}