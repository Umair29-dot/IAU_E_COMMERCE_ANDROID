package com.example.finalprojectpractice.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalprojectpractice.data.Product
import com.example.finalprojectpractice.databinding.SpecialRvItemBinding

class SpecialProductsAdapter: RecyclerView.Adapter<SpecialProductsAdapter.SpecialProductsViewHolder>() {

	private var productList = arrayListOf<Product>()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductsViewHolder {
		return SpecialProductsViewHolder(
			SpecialRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		)
	}

	override fun onBindViewHolder(holder: SpecialProductsViewHolder, position: Int) {
		val data = productList[position]
		holder.apply{
			Glide.with(itemView).load(data.images[0]).into(ivImage)
			tvProductName.text = data.name
			tvProductPrice.text = data.price.toString()
		}

		holder.itemView.setOnClickListener {
			onClick?.invoke(data)
		}
	}

	override fun getItemCount(): Int {
		return productList.size
	}

	inner class SpecialProductsViewHolder(private val binding: SpecialRvItemBinding): RecyclerView.ViewHolder(binding.root){
		val ivImage = binding.imageSpecialRvItem
		val tvProductName = binding.tvSpecialProductName
		val tvProductPrice = binding.tvSpecialProductPrice
	}

	fun setData(list: List<Product>) {
		productList.clear()
		productList.addAll(list)
		notifyDataSetChanged()
	}

	var onClick: ((Product) -> Unit)? = null
}