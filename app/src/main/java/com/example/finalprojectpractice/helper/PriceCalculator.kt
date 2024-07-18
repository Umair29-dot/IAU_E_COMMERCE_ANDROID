package com.example.finalprojectpractice.helper

fun Float?.getProductPrice(price:Float): Float{
	//this --> Percentage
	if(this == null){
		return price
	}else{
		val remainingPricePercentage = 1f-this
		val priceAfterPercentage = remainingPricePercentage*price
		return price
	}
}