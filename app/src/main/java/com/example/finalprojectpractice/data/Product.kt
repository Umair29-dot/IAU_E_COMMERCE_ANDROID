package com.example.finalprojectpractice.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String,
    val name: String,
    val category: String,
    val price: Float,
    val offerPercentage: Float? = null,
    val description: String? = null,
    val colors: List<String>? = null,
    val sizes: List<String>? = null,
    val images: List<String>
): Parcelable {
    constructor(): this("0" , "" , "" , 0f , images = emptyList())  // while dealing with firebase , we have to make secondary constructor
}