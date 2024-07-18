package com.example.finalprojectpractice.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalprojectpractice.data.Product
import com.example.finalprojectpractice.databinding.CategoryOfferRvItemBinding

class CategoryOfferProductsAdapter: RecyclerView.Adapter<CategoryOfferProductsAdapter.OfferProductsViewHolder>() {

	private val diffCallBack = object : DiffUtil.ItemCallback<Product>(){
		override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
			return oldItem.id == newItem.id
		}

		override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
			return oldItem == newItem
		}
	}

	val differ = AsyncListDiffer(this,diffCallBack)

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): CategoryOfferProductsAdapter.OfferProductsViewHolder {
		var binding = CategoryOfferRvItemBinding.inflate(LayoutInflater.from(parent.context))
		return OfferProductsViewHolder(binding)
	}

	override fun onBindViewHolder(
		holder: CategoryOfferProductsAdapter.OfferProductsViewHolder,
		position: Int
	) {
		val product = differ.currentList[position]
		holder.bind(product)
		holder.itemView.setOnClickListener {
			onClick?.invoke(product)
		}
	}

	override fun getItemCount(): Int {
		return differ.currentList.size
	}

	var onClick: ((Product) -> Unit)? = null

	inner class OfferProductsViewHolder(private val binding: CategoryOfferRvItemBinding): RecyclerView.ViewHolder(binding.root){
		fun bind(product: Product){
			binding.apply {
				Glide.with(itemView).load(product.images[0]).into(imgProduct)
				product.offerPercentage?.let {
					val remainingPricePercentage = 1f-it
					val priceAfterPercentage = remainingPricePercentage*product.price
					tvNewPrice.text = "$ ${String.format("%.2f" , priceAfterPercentage)}"  //Two get just two digits after decimal
					tvPrice.paintFlags =  Paint.STRIKE_THRU_TEXT_FLAG  //To put line on text
				}
				if(product.offerPercentage == null){
					tvNewPrice.visibility = View.INVISIBLE
				}
				tvPrice.text = "$ ${product.price}"
				tvName.text = product.name
			}
		}
	}


}