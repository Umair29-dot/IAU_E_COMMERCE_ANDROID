package com.example.finalprojectpractice.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.finalprojectpractice.databinding.ViewpagerImageItemBinding

 //It is used in ProductDetailFragment to show scrolling images //
class ViewPager2Images: RecyclerView.Adapter<ViewPager2Images.ViewPager2ImagesViewHolder>() {

	private val diffCallBack = object: DiffUtil.ItemCallback<String>() {
		override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
			return oldItem == newItem
		}

		override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
			return oldItem == newItem
		}
	}

	val differ = AsyncListDiffer(this, diffCallBack)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPager2ImagesViewHolder {
		return ViewPager2ImagesViewHolder(
			ViewpagerImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		)
	}

	override fun onBindViewHolder(holder: ViewPager2ImagesViewHolder, position: Int) {
		val image = differ.currentList[position]
		holder.apply {
			Glide.with(itemView).load(image).into(ivProductImage)
		}
	}

	override fun getItemCount(): Int {
		return differ.currentList.size
	}

	inner class ViewPager2ImagesViewHolder(val binding: ViewpagerImageItemBinding): ViewHolder(binding.root) {
		val ivProductImage = binding.imageProductDetails
	}

}