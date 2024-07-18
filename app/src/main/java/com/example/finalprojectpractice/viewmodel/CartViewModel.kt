package com.example.finalprojectpractice.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpractice.data.CartProduct
import com.example.finalprojectpractice.helper.getProductPrice
import com.example.finalprojectpractice.util.Constants
import com.example.finalprojectpractice.util.FirebaseCommans
import com.example.finalprojectpractice.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
	private val firestore: FirebaseFirestore,
	private val auth: FirebaseAuth,
	private val firebaseCommon: FirebaseCommans
): ViewModel(){

	private val _cartProduct = MutableStateFlow<Resource<List<CartProduct>>>(Resource.UnSpecified())
	val cartProducts = _cartProduct.asStateFlow()

	private val _deleteDialog = MutableSharedFlow<CartProduct>()
	val deleteDialog = _deleteDialog.asSharedFlow()

	val productsPrice = cartProducts.map {
		when(it) {
			is Resource.Success -> {
				calculatePrice(it.data!!)
			}
			else -> Unit
		}
	}

	private var cartProductDocuments = emptyList<DocumentSnapshot>()

	init {
		getCartProducts()
	}

	private fun getCartProducts(){
		viewModelScope.launch {
			_cartProduct.emit(Resource.Loading())
		}
		firestore.collection(Constants.USER_COLLECTION)// User Collection
			.document(auth.uid!!)// User Document
			.collection(Constants.CART_COLLECTION) // Cart Collection
			.addSnapshotListener{ value, error ->  //it will give us live data
				if(error != null || value == null){
					viewModelScope.launch {
						_cartProduct.emit(Resource.Error(error?.message.toString()))
					}
				}else{
					cartProductDocuments = value.documents
					val cartProducts = value.toObjects(CartProduct::class.java)
					viewModelScope.launch {
						_cartProduct.emit(Resource.Success(cartProducts))
					}
				}
			}
	}

	fun deleteProduct(cartProduct: CartProduct) {
		val index = cartProducts.value.data?.indexOf(cartProduct)
		if(index != null && index != -1) {
			val documentId = cartProductDocuments[index].id
			firestore.collection(Constants.USER_COLLECTION)
				.document(auth.uid!!)
				.collection(Constants.CART_COLLECTION)
				.document(documentId)
				.delete()
		}
	}

	fun changeQuantity(cartProduct: CartProduct, quantityChanging: FirebaseCommans.QuantityChanging) {
		val index = cartProducts.value.data?.indexOf(cartProduct) //it will give us of the index of cartProduct from mutableList
		if(index != null && index != -1){
			val documentId = cartProductDocuments[index].id
			when(quantityChanging){
				FirebaseCommans.QuantityChanging.INCREASE -> {
					viewModelScope.launch { _cartProduct.emit(Resource.Loading()) }
					increaseQuantity(documentId)
				}
				FirebaseCommans.QuantityChanging.DECREASE -> {
					if (cartProduct.quantity == 1) {
						viewModelScope.launch { _deleteDialog.emit(cartProduct) }
						return
					}
					viewModelScope.launch { _cartProduct.emit(Resource.Loading()) }
					decreaseQuantity(documentId)
				}
			}
		}
	}

	private fun decreaseQuantity(documentId: String) {
		firebaseCommon.decreaseQuantity(documentId){ result, exception ->
			if(exception != null){
				viewModelScope.launch {
					_cartProduct.emit(Resource.Error(exception.message.toString()))
				}
			}
		}
	}

	private fun increaseQuantity(documentId: String) {
		firebaseCommon.increaseQuantity(documentId){ result, exception ->
			if(exception != null){
				viewModelScope.launch {
					_cartProduct.emit(Resource.Error(exception.message.toString()))
				}
			}
		}
	}

	private fun calculatePrice(data: List<CartProduct>): Float {
		return data.sumByDouble { cartProduct ->
			(cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)*cartProduct.quantity).toDouble()
		}.toFloat()
	}

}