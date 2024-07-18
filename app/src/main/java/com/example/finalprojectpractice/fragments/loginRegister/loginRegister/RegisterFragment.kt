package com.example.finalprojectpractice.fragments.loginRegister.loginRegister

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
import com.example.finalprojectpractice.data.User
import com.example.finalprojectpractice.databinding.FragmentRegisterBinding
import com.example.finalprojectpractice.util.RegisterValidation
import com.example.finalprojectpractice.util.Resource
import com.example.finalprojectpractice.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment: Fragment() {

	private lateinit var binding: FragmentRegisterBinding
	private val viewModel by viewModels<RegisterViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentRegisterBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.apply {
			btnRegister.setOnClickListener {
				val user = User(
					edRegisterFirstName.text.toString().trim(),
					edRegisterLastName.text.toString().trim(),
					edRegisterEmail.text.toString().trim()
				)
				val password = edRegisterPassword.text.toString()

				viewModel.createAccountWithEmailAndPassword(user, password)
			}

			binding.tvDoYouHaveAccount.setOnClickListener {
				findNavController().navigate(R.id.action_registerFragment_to_loginFragment23)
			}
		}

	lifecycleScope.launch {
		viewModel.register.collect {
			when(it) {
				is Resource.Loading -> {
					binding.pbRegister.visibility = View.VISIBLE
				}
				is Resource.Success -> {
					binding.pbRegister.visibility = View.GONE
					Toast.makeText(requireContext(), "User saved successfully", Toast.LENGTH_LONG).show()
				}
				is Resource.Error -> {
					binding.pbRegister.visibility = View.GONE
					Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG).show()
				}

				else -> Unit
			}
		}
	}

		lifecycleScope.launch {
			viewModel.validation.collect {
				if(it.email is RegisterValidation.Failed) {
					withContext(Dispatchers.Main) {
						binding.edRegisterEmail.apply {
							requestFocus()
							error = it.email.message
						}
					}
				}

				if(it.password is RegisterValidation.Failed) {
					withContext(Dispatchers.Main) {
						binding.edRegisterPassword.apply {
							requestFocus()
							error = it.password.message
						}
					}
				}
			}
		}

	}

}