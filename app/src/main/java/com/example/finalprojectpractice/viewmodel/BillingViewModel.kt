package com.example.finalprojectpractice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpractice.data.Address
import com.example.finalprojectpractice.util.Constants
import com.example.finalprojectpractice.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
	private val firestore: FirebaseFirestore,
	private val auth: FirebaseAuth
): ViewModel() {

	private val _address = MutableStateFlow<Resource<List<Address>>>(Resource.UnSpecified())
	val address = _address.asStateFlow()

	init {
		getUserAddresses()
	}

	fun getUserAddresses() {
		viewModelScope.launch { _address.emit(Resource.Loading()) }
		firestore.collection(Constants.USER_COLLECTION).document(auth.uid!!)
			.collection(Constants.ADDRESS_COLLECTION)
			.addSnapshotListener { value, error ->
				if(error != null) {
					viewModelScope.launch {
						_address.emit(Resource.Error(error.message.toString()))
					}
					return@addSnapshotListener
				}

				val addresses = value?.toObjects(Address::class.java)
				viewModelScope.launch {
					_address.emit(Resource.Success(addresses!!))
				}
			}
	}

}