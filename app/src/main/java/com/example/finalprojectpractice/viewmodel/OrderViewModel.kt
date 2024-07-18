package com.example.finalprojectpractice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpractice.data.order.Order
import com.example.finalprojectpractice.data.order.OrderProduct
import com.example.finalprojectpractice.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
	private val fireStore: FirebaseFirestore,
	private val auth: FirebaseAuth
): ViewModel() {

	private val _order = MutableStateFlow<Resource<Order>>(Resource.UnSpecified())
	val order = _order.asStateFlow()

	fun placeOrder(order: Order) {
		viewModelScope.launch {
			_order.emit(Resource.Loading())
		}
			//Batch and Transaction is used when u want to perform more than one task on firebase
			//if one thing fails, the whole block will fail
			//Batch is just used to write
			//Transaction is used to read and write
		fireStore.runBatch {
			//TODO: Create orders collections and add the orders
			//TODO: Delete products from the users-cart collection

			fireStore.collection("orders")
				.document()
				.set(order)

			fireStore.collection("user")
				.document(auth.uid!!)
				.collection("cart")
				.get()
				.addOnSuccessListener {
					it.documents.forEach {
						it.reference.delete()
					}
				}

		}
			.addOnSuccessListener {
				viewModelScope.launch {
					_order.emit(Resource.Success(order))
				}
			}
			.addOnFailureListener {
				viewModelScope.launch {
					_order.emit(Resource.Error(it.message.toString()))
				}
			}
	}

}