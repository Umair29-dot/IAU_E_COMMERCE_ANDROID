package com.example.finalprojectpractice.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalprojectpractice.data.Product
import com.example.finalprojectpractice.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
	private val firestore: FirebaseFirestore
): ViewModel() {

	var searchProducts: MutableLiveData<Resource<List<Product>>> = MutableLiveData()

	fun searchProductWithName(productName: String) {
		searchProducts.value = Resource.Loading()

		firestore.collection("Products")
			.whereEqualTo("name", productName)
			.get()
			.addOnSuccessListener {
				val productList = it.toObjects(Product::class.java)
				searchProducts.value = Resource.Success(productList)
			}
			.addOnFailureListener {
				searchProducts.value = Resource.Error(it.message.toString())
			}
	}

}