package com.example.finalprojectpractice.fragments.loginRegister.loginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.finalprojectpractice.R
import com.example.finalprojectpractice.databinding.FragmentAccountOptionBinding

class AccountOptionsFragment: Fragment() {

	private lateinit var binding: FragmentAccountOptionBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentAccountOptionBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.btnLoginAccountOption.setOnClickListener{
			findNavController().navigate(R.id.action_accountOptionsFragment_to_loginFragment2)
		}

		binding.btnRegisterAccountOption.setOnClickListener {
			findNavController().navigate(R.id.action_accountOptionsFragment_to_registerFragment2)
		}

	}

}