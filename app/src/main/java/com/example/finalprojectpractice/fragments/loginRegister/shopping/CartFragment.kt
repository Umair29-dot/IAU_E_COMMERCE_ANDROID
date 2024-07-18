package com.example.finalprojectpractice.fragments.loginRegister.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectpractice.R
import com.example.finalprojectpractice.adapters.CartProductsAdapter
import com.example.finalprojectpractice.databinding.FragmentCartBinding
import com.example.finalprojectpractice.databinding.FragmentProfileBinding
import com.example.finalprojectpractice.util.FirebaseCommans
import com.example.finalprojectpractice.util.Resource
import com.example.finalprojectpractice.viewmodel.CartViewModel
import kotlinx.coroutines.flow.collectLatest

class CartFragment: Fragment() {

	private lateinit var binding: FragmentCartBinding
	private val cartAdapter by lazy {
		CartProductsAdapter()
	}
	private val viewModel by activityViewModels<CartViewModel>()
	var lastValue = ""
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentCartBinding.inflate(inflater)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		var totalPrice = 0f

		setUpCartRv()

		binding.btnCheckout.setOnClickListener {
			val action = CartFragmentDirections.actionCartFragmentToBillingFragment(totalPrice, cartAdapter.differ.currentList.toTypedArray())
			findNavController().navigate(action)
		}

		cartAdapter.onClick = {
			val b = Bundle().apply { putParcelable("product", it.product) }
			findNavController().navigate(R.id.action_cartFragment_to_productDetailFragment, b)
		}

		cartAdapter.onPlusClick = {
			viewModel.changeQuantity(it, FirebaseCommans.QuantityChanging.INCREASE)
		}

		cartAdapter.onMinusClick = {
			viewModel.changeQuantity(it, FirebaseCommans.QuantityChanging.DECREASE)
		}

		lifecycleScope.launchWhenStarted {
			viewModel.cartProducts.collectLatest {
				when(it) {
					is Resource.Loading -> {
						binding.progressBar.visibility = View.VISIBLE
					}
					is Resource.Success -> {
						binding.progressBar.visibility = View.GONE
						if(it.data!!.isEmpty()) {
							showEmptyCart()
						} else {
							hideEmptyCart()
							cartAdapter.differ.submitList(it.data)
						}
					}
					is Resource.Error -> {
						binding.progressBar.visibility = View.GONE
						Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
					}
					else -> Unit
				}
			}
		}

		lifecycleScope.launchWhenStarted {
			viewModel.productsPrice.collectLatest {
				it?.let {
					if (it.toString() == "kotlin.Unit") {
						binding.tvTotalprice.text = "$ "+lastValue
					} else {
						totalPrice = it as Float
						lastValue = it.toString()
						binding.tvTotalprice.text = "$ $it"
					}
				}
			}
		}

		lifecycleScope.launchWhenStarted {
			viewModel.deleteDialog.collectLatest {
				val alertDialog = AlertDialog.Builder(requireContext())
					.apply {
						setTitle("Delete item from cart")
						setMessage("Do you want to delete this item?")
						setNegativeButton("Cancle") { dialog, _ ->
							dialog.dismiss()
						}
						setPositiveButton("Yes") { dialog, _ ->
							viewModel.deleteProduct(it)
							dialog.dismiss()
						}
					}
				alertDialog.create()
				alertDialog.show()
			}
		}

	}

	private fun hideEmptyCart() {
		binding.apply {
			imgEmptyBoxTexture.visibility = View.GONE
			imgEmptyBox.visibility = View.GONE

			btnCheckout.visibility = View.VISIBLE
			linear.visibility = View.VISIBLE
			rvCart.visibility = View.VISIBLE
		}
	}

	private fun showEmptyCart() {
		binding.apply {
			imgEmptyBoxTexture.visibility = View.VISIBLE
			imgEmptyBox.visibility = View.VISIBLE

			btnCheckout.visibility = View.GONE
			linear.visibility = View.GONE
			rvCart.visibility = View.GONE
		}
	}

	private fun setUpCartRv() {
		binding.rvCart.apply {
			layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
			adapter = cartAdapter
		}
	}

}