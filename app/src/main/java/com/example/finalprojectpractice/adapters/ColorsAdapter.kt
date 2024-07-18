package com.example.finalprojectpractice.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectpractice.databinding.ColorRvItemBinding

class ColorsAdapter: RecyclerView.Adapter<ColorsAdapter.ColorsViewHolder>() {

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

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsViewHolder {
		return ColorsViewHolder(
			ColorRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		)
	}

	override fun onBindViewHolder(holder: ColorsViewHolder, position: Int) {
		val color = differ.currentList[position]
		holder.apply {

			//Log.d("colors11", color)
			ivColor.setBackgroundColor(Color.parseColor(color))

			if(position == selectedPosition) { //Color is selected
				ivColorSelection.visibility = View.VISIBLE
			} else { //Color is not selected
				ivColorSelection.visibility = View.INVISIBLE
			}

			cvColor.setOnClickListener {
				if(selectedPosition >= 0) {
					notifyItemChanged(selectedPosition)
				}
				selectedPosition = holder.adapterPosition
				notifyItemChanged(selectedPosition)
				onItemClick?.invoke(color)
			}
		}
	}

	override fun getItemCount(): Int {
		return differ.currentList.size
	}

	inner class ColorsViewHolder(val binding: ColorRvItemBinding): RecyclerView.ViewHolder(binding.root) {
		val cvColor = binding.cvColor
		val ivColor = binding.ivProductColor
		val ivColorSelection = binding.ivProductColorSelection
	}

	var onItemClick: ((String) -> Unit)? = null
}