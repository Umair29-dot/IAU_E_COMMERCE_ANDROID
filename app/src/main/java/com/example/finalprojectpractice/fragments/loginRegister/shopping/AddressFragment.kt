package com.example.finalprojectpractice.fragments.loginRegister.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.finalprojectpractice.data.Address
import com.example.finalprojectpractice.databinding.FragmentAddressBinding
import com.example.finalprojectpractice.util.Resource
import com.example.finalprojectpractice.viewmodel.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AddressFragment: Fragment() {

	private lateinit var binding: FragmentAddressBinding
	private val viewModel by viewModels<AddressViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentAddressBinding.inflate(inflater)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		lifecycleScope.launchWhenStarted {
			viewModel.addNewAddress.collectLatest {
				when(it) {
					is Resource.Loading -> {
						binding.progressbarAddress.visibility = View.VISIBLE
					}
					is Resource.Success -> {
						binding.progressbarAddress.visibility = View.GONE
						Toast.makeText(requireContext(), "Address added successfully", Toast.LENGTH_SHORT).show()
						findNavController().navigateUp()  //To return back to the previous fragment
					}
					is Resource.Error -> {
						binding.progressbarAddress.visibility = View.GONE
						Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
					}

					else -> Unit
				}
			}
		}

		binding.apply {
			btnSave.setOnClickListener {
				val addressTitle = edAddressTitle.text.toString()
				val fullName = edFullName.text.toString()
				val state = edState.text.toString()
				val street = edStreet.text.toString()
				val phone = edPhone.text.toString()
				val city = edCity.text.toString()

				val address = Address(addressTitle, fullName, street, phone, city, state)
				viewModel.addAddress(address)
			}
		}

	}

}