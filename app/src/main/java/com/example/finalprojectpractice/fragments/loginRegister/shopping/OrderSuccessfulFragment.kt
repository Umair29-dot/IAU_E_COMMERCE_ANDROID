package com.example.finalprojectpractice.fragments.loginRegister.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.finalprojectpractice.databinding.FragmentOrderSuccessfulBinding

class OrderSuccessfulFragment: Fragment() {

	private lateinit var binding: FragmentOrderSuccessfulBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentOrderSuccessfulBinding.inflate(inflater)
		return binding.root
	}

}