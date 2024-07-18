package com.example.finalprojectpractice.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpractice.data.Product
import com.example.finalprojectpractice.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(private val firestore: FirebaseFirestore) :
	ViewModel() {

	private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.UnSpecified())
	val specialProducts: StateFlow<Resource<List<Product>>> = _specialProducts

	private val _bestDealsProducts =
		MutableStateFlow<Resource<List<Product>>>(Resource.UnSpecified())
	val bestDeals: StateFlow<Resource<List<Product>>> = _bestDealsProducts

	private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.UnSpecified())
	val bestProdcuts: StateFlow<Resource<List<Product>>> = _bestProducts

	fun fetchSpecialProducts() {
		viewModelScope.launch {
			_specialProducts.emit(Resource.Loading())
		}

		firestore.collection("Products")
			.whereEqualTo("category", "Special Products")
			.get()
			.addOnSuccessListener {
				val specialProductsList = it.toObjects(Product::class.java)
				viewModelScope.launch {
					_specialProducts.emit(Resource.Success(specialProductsList))
				}
			}
			.addOnFailureListener {
				viewModelScope.launch {
					_specialProducts.emit(Resource.Error(it.message.toString()))
				}
			}
	}

	fun fetchBestDeals() {
		viewModelScope.launch {
			_bestDealsProducts.emit(Resource.Loading())
		}

		firestore.collection("Products")
			.whereEqualTo("category", "Best Deals")
			.get()
			.addOnSuccessListener {
				val bestDealsProductList =
					it.toObjects(Product::class.java)  //When we use .toObject then we have to make constructor in our class (Here ,Product class)
				viewModelScope.launch {
					_bestDealsProducts.emit(Resource.Success(bestDealsProductList))
				}
			}
			.addOnFailureListener {
				viewModelScope.launch {
					_bestDealsProducts.emit(Resource.Error(it.message.toString()))
				}
			}
	}

	fun fetchBestProducts() {
		viewModelScope.launch {
			_bestProducts.emit(Resource.Loading())
		}

		firestore.collection("Products")
			.whereEqualTo("category", "Best Products")
			.get()
			.addOnSuccessListener {
				val bestProductList =
					it.toObjects(Product::class.java)  //When we use .toObject then we have to make constructor in our class (Here ,Product class)
				viewModelScope.launch {
					_bestProducts.emit(Resource.Success(bestProductList))
				}
			}
			.addOnFailureListener {
				viewModelScope.launch {
					_bestProducts.emit(Resource.Error(it.message.toString()))
				}
			}
	}

}