package com.example.finalprojectpractice.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalprojectpractice.data.Product
import com.example.finalprojectpractice.databinding.BestDealsRvItemBinding
import com.example.finalprojectpractice.databinding.SearchRvItemBinding

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

	private var productList = ArrayList<Product>()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
		return SearchViewHolder(SearchRvItemBinding.inflate(LayoutInflater.from(parent.context)))
	}


	override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
		var data = productList[position]

		holder.apply {
			Glide.with(itemView).load(data.images[0]).into(ivProductImage)
			tvProductName.setText(data.name)
			tvProductOldPrice.setText("$ ${data.price}")
			tvProductDescription.setText("${data.description}")
			data.offerPercentage?.let {
				val remainingPricePercentage = 1f - it
				val priceAfterOffer = remainingPricePercentage * data.price
				tvProductNewPrice.text = "$ ${priceAfterOffer}"
				tvProductOldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
			}
		}

		holder.itemView.setOnClickListener {
			onClick?.invoke(data)
		}
	}

	override fun getItemCount(): Int {
		return productList.size
	}

	inner class SearchViewHolder(private val binding: SearchRvItemBinding): RecyclerView.ViewHolder(binding.root) {
		val ivProductImage = binding.ivProductImage
		val tvProductName = binding.tvProductName
		val tvProductNewPrice = binding.tvProductOfferPrice
		val tvProductOldPrice = binding.tvProductActualPrice
		val tvProductDescription = binding.tvProductDescription
	}

	fun setData(list: List<Product>) {
		productList.clear()
		productList.addAll(list)
		notifyDataSetChanged()
	}

	var onClick: ((Product) -> Unit)? = null

}