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
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectpractice.R
import com.example.finalprojectpractice.adapters.AddressAdapter
import com.example.finalprojectpractice.adapters.BillingAdapter
import com.example.finalprojectpractice.data.Address
import com.example.finalprojectpractice.data.CartProduct
import com.example.finalprojectpractice.data.order.Order
import com.example.finalprojectpractice.data.order.OrderProduct
import com.example.finalprojectpractice.data.order.OrderStatus
import com.example.finalprojectpractice.databinding.FragmentBillingBinding
import com.example.finalprojectpractice.util.Resource
import com.example.finalprojectpractice.viewmodel.BillingViewModel
import com.example.finalprojectpractice.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BillingFragment: Fragment() {

	private lateinit var binding: FragmentBillingBinding
	private val billingViewModel by viewModels<BillingViewModel>()
	private val orderViewModel by viewModels<OrderViewModel>()
	private val addressAdapter by lazy {
		AddressAdapter()
	}
	private val billingAdapter by lazy {
		BillingAdapter()
	}
	private val args by navArgs<BillingFragmentArgs>()
	private var products = emptyList<CartProduct>()
	private var totalPrice = 0f
	private var selectedAddress: Address? = null
	private var orderProducts = mutableListOf<OrderProduct>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		products = args.products.toList()
		totalPrice = args.totalPrice

	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentBillingBinding.inflate(inflater)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.imgAddAddress.setOnClickListener {
			findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
		}

		binding.btnPlaceOrder.setOnClickListener {
			if (selectedAddress == null) {
				Toast.makeText(requireContext(), "Please select an address", Toast.LENGTH_SHORT).show()
			} else {

				for(cartProduct in products) {
					var orderProduct = OrderProduct(
						id = cartProduct.product.id,
						name = cartProduct.product.name,
						price = cartProduct.product.price,
						quantity = cartProduct.quantity,
						selectedColor = cartProduct.selectedColor,
						selectedSize = cartProduct.selectedSize,
						image = cartProduct.product.images[0]
					)
					orderProducts.add(orderProduct)
				}

				val order = Order(
					OrderStatus.Ordered.status,
					totalPrice,
					orderProducts,
					selectedAddress!!
				)

				orderViewModel.placeOrder(order)
			}
		}

		lifecycleScope.launchWhenStarted {
			billingViewModel.address.collectLatest {
				when(it) {
					is Resource.Loading -> {
						binding.progressbarAddresses.visibility = View.VISIBLE
					}
					is Resource.Success -> {
						binding.progressbarAddresses.visibility = View.GONE
						addressAdapter.differ.submitList(it.data)
					}
					is Resource.Error -> {
						binding.progressbarAddresses.visibility = View.GONE
						Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
					}
					else -> Unit
				}
			}
		}

		lifecycleScope.launchWhenStarted {
			orderViewModel.order.collectLatest {
				when(it) {
					is Resource.Loading -> {
						binding.progressbarAddresses.visibility = View.VISIBLE
					}
					is Resource.Success -> {
						binding.progressbarAddresses.visibility = View.GONE
						findNavController().navigateUp()
						findNavController().navigate(R.id.action_cartFragment_to_orderSuccessfulFragment)
					}
					is Resource.Error -> {
						binding.progressbarAddresses.visibility = View.GONE
						Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
					}
					else -> Unit
				}
			}
		}

		setUpAddressRv()
		setUpBillingRv()

		billingAdapter.differ.submitList(products)
		binding.tvTotalprice.text = "$ $totalPrice"
		addressAdapter.onClick = {
			selectedAddress = it
		}

	}

	private fun setUpBillingRv() {
		binding.rvProducts.apply {
			layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
			adapter = billingAdapter
		}
	}

	private fun setUpAddressRv() {
		binding.rvAdresses.apply {
			layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
			adapter = addressAdapter
		}
	}

}