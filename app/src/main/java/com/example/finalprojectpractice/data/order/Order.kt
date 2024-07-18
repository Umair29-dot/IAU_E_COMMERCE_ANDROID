package com.example.finalprojectpractice.data.order

import com.example.finalprojectpractice.data.Address
import com.example.finalprojectpractice.data.CartProduct

data class Order (
	val orderStatus: String,
	val totalPrice: Float,
	val products: List<OrderProduct>,
	val address: Address
		)