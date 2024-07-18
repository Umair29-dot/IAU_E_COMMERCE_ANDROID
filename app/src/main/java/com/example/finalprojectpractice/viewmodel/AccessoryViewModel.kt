package com.example.finalprojectpractice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpractice.data.Category
import com.example.finalprojectpractice.data.Product
import com.example.finalprojectpractice.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccessoryViewModel(private val firestore: FirebaseFirestore, private val category: Category): ViewModel() {

	private val _offerProducts = MutableStateFlow<Resource<List<Product>>>(Resource.UnSpecified())
	val offerProducts = _offerProducts.asStateFlow()

	private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.UnSpecified())
	val bestProducts = _bestProducts.asStateFlow()

	fun fetchOfferProducts() {
		viewModelScope.launch {
			_offerProducts.emit(Resource.Loading())

			firestore.collection("Products")
				.whereEqualTo("category", category.category)
				.whereNotEqualTo("offerPercentage", null)
				.get()
				.addOnSuccessListener {
					val products = it.toObjects(Product::class.java)
					viewModelScope.launch {
						_offerProducts.emit(Resource.Success(products))
					}
				}
				.addOnFailureListener {
					viewModelScope.launch {
						_offerProducts.emit(Resource.Error(it.message.toString()))
					}
				}
		}
	}

	fun fetchBestProducts() {
		viewModelScope.launch {
			_bestProducts.emit(Resource.Loading())

			firestore.collection("Products")
				.whereEqualTo("category", category.category)
				.whereEqualTo("offerPercentage", null)
				.get()
				.addOnSuccessListener {
					val products = it.toObjects(Product::class.java)
					viewModelScope.launch {
						_bestProducts.emit(Resource.Success(products))
					}
				}
				.addOnFailureListener {
					viewModelScope.launch {
						_bestProducts.emit(Resource.Error(it.message.toString()))
					}
				}
		}
	}

}