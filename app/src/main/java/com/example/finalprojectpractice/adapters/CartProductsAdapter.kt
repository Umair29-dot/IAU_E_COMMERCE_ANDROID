package com.example.finalprojectpractice.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalprojectpractice.data.CartProduct
import com.example.finalprojectpractice.databinding.CartProductItemBinding
import com.example.finalprojectpractice.helper.getProductPrice

class CartProductsAdapter : RecyclerView.Adapter<CartProductsAdapter.CartProductsViewHolder>() {

	private val diffCallBack = object : DiffUtil.ItemCallback<CartProduct>(){
		override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
			return oldItem.product.id == newItem.product.id
		}

		override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
			return oldItem == newItem
		}
	}

	val differ = AsyncListDiffer(this,diffCallBack)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductsViewHolder {
		var binding = CartProductItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
		return CartProductsViewHolder(binding)
	}

	override fun onBindViewHolder(holder: CartProductsViewHolder, position: Int) {
		val cartProduct = differ.currentList[position]
		holder.bind(cartProduct)

		holder.itemView.setOnClickListener {
			onClick?.invoke(cartProduct)
		}

		holder.binding.ivPlus.setOnClickListener {
			onPlusClick?.invoke(cartProduct)
		}

		holder.binding.ivMinus.setOnClickListener {
			onMinusClick?.invoke(cartProduct)
		}
	}

	override fun getItemCount(): Int {
		return differ.currentList.size
	}

	var onClick: ((CartProduct) -> Unit)? = null
	var onPlusClick: ((CartProduct) -> Unit)? = null
	var onMinusClick: ((CartProduct) -> Unit)? = null

	inner class CartProductsViewHolder(val binding:CartProductItemBinding):RecyclerView.ViewHolder(binding.root){
		fun bind(cartProduct: CartProduct){
			binding.apply {
				Glide.with(itemView).load(cartProduct.product.images[0]).into(ivCartProductImage)
				tvCartProductName.text = cartProduct.product.name
				tvCartProductQuantity.text = cartProduct.quantity.toString()

				val priceAfterPercentage = cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)
				tvCartProductPrice.text = "$ ${String.format("%.2f", priceAfterPercentage)}"

				civCartProductColor.setBackgroundColor(Color.parseColor(cartProduct.selectedColor ?: "#00666666"))
				civCartProductSize.text = cartProduct.selectedSize ?: "".also { civCartProductSize.setBackgroundColor(Color.TRANSPARENT as Int)}
			}
		}
	}
}