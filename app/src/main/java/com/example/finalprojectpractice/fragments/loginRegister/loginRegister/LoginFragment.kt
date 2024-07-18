package com.example.finalprojectpractice.fragments.loginRegister.loginRegister

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.finalprojectpractice.R
import com.example.finalprojectpractice.activities.ShoppingActivity
import com.example.finalprojectpractice.databinding.FragmentLoginBinding
import com.example.finalprojectpractice.util.Resource
import com.example.finalprojectpractice.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment: Fragment() {

	private lateinit var binding: FragmentLoginBinding
	private val viewModel by viewModels<LoginViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentLoginBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.apply {
			btnLogin.setOnClickListener {
				val email = edLoginEmail.text.toString().trim()
				val password = edLoginPassword.text.toString()
				viewModel.login(email, password)
			}
		}

		binding.tvDontHaveAccount.setOnClickListener{
			findNavController().navigate(R.id.action_loginFragment2_to_registerFragment2)
		}

		lifecycleScope.launch {
			viewModel.login.collect {
				when(it){
					is Resource.Loading ->{
						Log.d("Testing", "Loading")
					}
					is Resource.Success ->{
						val intent = Intent(requireContext(), ShoppingActivity::class.java)
						startActivity(intent)
						requireActivity().finish()

					}
					is Resource.Error -> {
						Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
					}
					else -> Unit
				}
			}
		}

	}

}