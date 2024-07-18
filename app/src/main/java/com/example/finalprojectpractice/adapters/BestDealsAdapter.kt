package com.example.finalprojectpractice.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalprojectpractice.data.Product
import com.example.finalprojectpractice.databinding.BestDealsRvItemBinding

class BestDealsAdapter: RecyclerView.Adapter<BestDealsAdapter.BestDealsViewHolder>() {

	private var productList = ArrayList<Product>()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestDealsViewHolder {
		return BestDealsViewHolder(BestDealsRvItemBinding.inflate(LayoutInflater.from(parent.context)))
	}


	override fun onBindViewHolder(holder: BestDealsViewHolder, position: Int) {
		var data = productList[position]

		holder.apply {
			Glide.with(itemView).load(data.images[0]).into(ivProductImage)
			tvProductName.setText(data.name)
			tvProductOldPrice.setText("$ ${data.price}")
			data.offerPercentage?.let {
				val remainingPricePercentage = 1f - it
				val priceAfterOffer = remainingPricePercentage * data.price
				tvProductNewPrice.text = "$ ${priceAfterOffer}"
				tvProductOldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
			}
			if(data.offerPercentage == null) {
				tvProductNewPrice.visibility = View.INVISIBLE
			}
		}

		holder.itemView.setOnClickListener {
			onClick?.invoke(data)
		}
	}

	override fun getItemCount(): Int {
		return productList.size
	}

	inner class BestDealsViewHolder(private val binding: BestDealsRvItemBinding): RecyclerView.ViewHolder(binding.root) {
		val ivProductImage = binding.ivDealProductName
		val tvProductName = binding.tvDealProductName
		val tvProductNewPrice = binding.tvNewPrice
		val tvProductOldPrice = binding.tvOldPrice
		//val btnSeeProduct = binding.btnSeeProduct
	}

	fun setData(list: List<Product>) {
		productList.clear()
		productList.addAll(list)
		notifyDataSetChanged()
	}

	var onClick: ((Product) -> Unit)? = null

}