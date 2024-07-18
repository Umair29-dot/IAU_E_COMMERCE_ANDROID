package com.example.finalprojectpractice.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectpractice.R
import com.example.finalprojectpractice.data.Address
import com.example.finalprojectpractice.databinding.AddressRvItemBinding

class AddressAdapter: RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

	var selectedAddress = -1

	private val diffCallBack = object: DiffUtil.ItemCallback<Address>() {
		override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
			return oldItem.addressTitle == newItem.addressTitle && oldItem.fullName == newItem.fullName
		}

		override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
			return oldItem == newItem
		}
	}

	val differ = AsyncListDiffer(this, diffCallBack)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
		return AddressViewHolder(
			AddressRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		)
	}

	override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
		val address = differ.currentList[position]
		holder.bind(address, selectedAddress == position)

		holder.binding.tvAddress.setOnClickListener {
			if(selectedAddress >= 0) {
				notifyItemChanged(selectedAddress)
			}
			selectedAddress = holder.adapterPosition
			notifyItemChanged(selectedAddress)

			onClick?.invoke(address)
		}
	}

	override fun getItemCount(): Int {
		return differ.currentList.size
	}

	inner class AddressViewHolder(val binding: AddressRvItemBinding): RecyclerView.ViewHolder(binding.root) {
		fun bind(address: Address, isSelected: Boolean) {
			binding.apply {
				tvAddress.text = address.addressTitle
				if (isSelected) {
					//tvAddress.setBackgroundColor(Color.BLUE)
					tvAddress.background = ColorDrawable(ContextCompat.getColor(itemView.context, R.color.g_blue))
					tvAddress.setTextColor(ContextCompat.getColor(itemView.context, R.color.g_white))
				} else {
					tvAddress.background = ColorDrawable(itemView.context.resources.getColor(R.color.g_white))
					tvAddress.setTextColor(ContextCompat.getColor(itemView.context, R.color.g_black))
				}
			}
		}
	}

	var onClick: ((Address) -> Unit)? = null

}