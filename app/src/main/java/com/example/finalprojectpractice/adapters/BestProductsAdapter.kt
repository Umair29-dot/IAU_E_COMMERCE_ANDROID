package com.example.finalprojectpractice.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalprojectpractice.data.Product
import com.example.finalprojectpractice.databinding.BestDealsRvItemBinding
import com.example.finalprojectpractice.databinding.ProductRvItemBinding

class BestProductsAdapter: RecyclerView.Adapter<BestProductsAdapter.BestProductsViewHolder>() {

	private var productList = ArrayList<Product>()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductsViewHolder {
		return BestProductsViewHolder(ProductRvItemBinding.inflate(LayoutInflater.from(parent.context)))
	}


	override fun onBindViewHolder(holder: BestProductsViewHolder, position: Int) {
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
			/*ivFavorite.setOnClickListener{

			}*/
		}

		holder.itemView.setOnClickListener {
			onClick?.invoke(data)
		}
	}

	override fun getItemCount(): Int {
		return productList.size
	}

	inner class BestProductsViewHolder(private val binding: ProductRvItemBinding): RecyclerView.ViewHolder(binding.root) {
		val ivProductImage = binding.imgProduct
		val tvProductName = binding.tvName
		val tvProductNewPrice = binding.tvNewPrice
		val tvProductOldPrice = binding.tvPrice
		//val ivFavorite = binding.imgFavorite
	}

	fun setData(list: List<Product>) {
		productList.clear()
		productList.addAll(list)
		notifyDataSetChanged()
	}

	var onClick: ((Product) -> Unit)? = null

}