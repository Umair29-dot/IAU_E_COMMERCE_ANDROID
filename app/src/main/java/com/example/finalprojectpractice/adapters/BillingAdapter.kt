package com.example.finalprojectpractice.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalprojectpractice.data.Address
import com.example.finalprojectpractice.data.CartProduct
import com.example.finalprojectpractice.databinding.AddressRvItemBinding
import com.example.finalprojectpractice.databinding.BillingProductsRvItemBinding
import com.example.finalprojectpractice.helper.getProductPrice

class BillingAdapter: RecyclerView.Adapter<BillingAdapter.BillingViewHolder>() {

	private val diffCallBack = object: DiffUtil.ItemCallback<CartProduct>() {
		override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
			return oldItem.product == newItem.product
		}

		override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
			return oldItem == newItem
		}
	}

	val differ = AsyncListDiffer(this, diffCallBack)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingViewHolder {
		return BillingViewHolder(
			BillingProductsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		)
	}

	override fun onBindViewHolder(holder: BillingViewHolder, position: Int) {
		val billing = differ.currentList[position]
		holder.bind(billing)
	}

	override fun getItemCount(): Int {
		return differ.currentList.size
	}

	inner class BillingViewHolder(val binding: BillingProductsRvItemBinding): RecyclerView.ViewHolder(binding.root) {
		fun bind(cartProduct: CartProduct) {
			Glide.with(itemView).load(cartProduct.product.images[0]).into(binding.ivCartProductImage)
			binding.tvCartProductName.text = cartProduct.product.name
			binding.tvCartProductQuantity.text = cartProduct.quantity.toString()

			val priceAfterPercentage = cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)
			binding.tvCartProductPrice.text = "$ ${String.format("%.2f", priceAfterPercentage)}"

			binding.civCartProductColor.setBackgroundColor(Color.parseColor(cartProduct.selectedColor ?: "#00666666"))
			binding.civCartProductSize.text = cartProduct.selectedSize ?: "".also { binding.civCartProductSize.setBackgroundColor(Color.TRANSPARENT as Int)}
		}
	}

	var onClick: ((Address) -> Unit)? = null

}