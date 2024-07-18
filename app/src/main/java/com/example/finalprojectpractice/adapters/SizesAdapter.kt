package com.example.finalprojectpractice.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectpractice.databinding.SizeRvItemBinding

class SizesAdapter: RecyclerView.Adapter<SizesAdapter.SizesViewHolder>() {

	private var selectedPosition = -1

	private val diffCallBack = object: DiffUtil.ItemCallback<String>() {
		override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
			return oldItem == newItem
		}

		override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
			return oldItem == newItem
		}
	}

	val differ = AsyncListDiffer(this, diffCallBack)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizesViewHolder {
		return SizesViewHolder(
			SizeRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		)
	}

	override fun onBindViewHolder(holder: SizesViewHolder, position: Int) {
		val size = differ.currentList[position]
		holder.apply {
			tvSize.setText(size)

			if(position == selectedPosition) { //Size is selected
				ivSizeSelection.visibility = View.VISIBLE
			} else { //Size is not selected
				ivSizeSelection.visibility = View.INVISIBLE
			}

			cvColor.setOnClickListener {
				if(selectedPosition >= 0) {
					notifyItemChanged(selectedPosition)
				}
				selectedPosition = holder.adapterPosition
				notifyItemChanged(selectedPosition)
				onItemClick?.invoke(size)
			}
		}
	}

	override fun getItemCount(): Int {
		return differ.currentList.size
	}

	inner class SizesViewHolder(val binding: SizeRvItemBinding): RecyclerView.ViewHolder(binding.root) {
		val cvColor = binding.cvSize
		val tvSize = binding.tvProductSize
		val ivSizeSelection = binding.ivProductColorSelection
	}

	var onItemClick: ((String) -> Unit)? = null
}