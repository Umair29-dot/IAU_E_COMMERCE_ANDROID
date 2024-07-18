package com.example.finalprojectpractice.data.order

data class OrderProduct (
	val id: String,
	val name: String,
	val image: String,
	val price: Float,
	val quantity: Int,
	val selectedColor: String? = null,
	val selectedSize: String? = null
		)