package com.example.finalprojectpractice.fragments.loginRegister.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.finalprojectpractice.adapters.HomeViewPagerAdapter
import com.example.finalprojectpractice.databinding.FragmentHomeBinding
import com.example.finalprojectpractice.fragments.loginRegister.categories.AccessoryFragment
import com.example.finalprojectpractice.fragments.loginRegister.categories.ChairFragment
import com.example.finalprojectpractice.fragments.loginRegister.categories.CupboardFragment
import com.example.finalprojectpractice.fragments.loginRegister.categories.FurnitureFragment
import com.example.finalprojectpractice.fragments.loginRegister.categories.MainCategoryFragment
import com.example.finalprojectpractice.fragments.loginRegister.categories.TableFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment: Fragment() {

	private lateinit var binding: FragmentHomeBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentHomeBinding.inflate(inflater)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val categoriesFragment = arrayListOf<Fragment>(
			MainCategoryFragment(),
			ChairFragment(),
			CupboardFragment(),
			TableFragment(),
			AccessoryFragment(),
			FurnitureFragment()
		)

		binding.viewPagerHome.isUserInputEnabled = false

		val viewPager2Adapter = HomeViewPagerAdapter(categoriesFragment, childFragmentManager, lifecycle)
		binding.viewPagerHome.adapter = viewPager2Adapter
		TabLayoutMediator(binding.tabLayout, binding.viewPagerHome) { tab, position ->
			when(position) {
				0 -> tab.text = "Main"
				1 -> tab.text = "Chair"
				2 -> tab.text = "Cupboard"
				3 -> tab.text = "Table"
				4 -> tab.text = "Accessory"
				5 -> tab.text = "Furniture"
			}
		}.attach()

	}

}