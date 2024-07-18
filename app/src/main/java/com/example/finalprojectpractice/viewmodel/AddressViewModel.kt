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
class AddressViewModel @Inject constructor(
	private val firestore: FirebaseFirestore,
	private val auth: FirebaseAuth
): ViewModel() {

	private val _addNewAddress = MutableStateFlow<Resource<Address>>(Resource.UnSpecified())
	val addNewAddress = _addNewAddress.asStateFlow()

	fun addAddress(address: Address) {
		viewModelScope.launch {
			_addNewAddress.emit(Resource.Loading())
		}
		firestore.collection(Constants.USER_COLLECTION)
			.document(auth.uid!!)
			.collection(Constants.ADDRESS_COLLECTION)
			.document()
			.set(address)
			.addOnSuccessListener {
				viewModelScope.launch {
					_addNewAddress.emit(Resource.Success(address))
				}
			}
			.addOnFailureListener {
				viewModelScope.launch {
					_addNewAddress.emit(Resource.Error(it.message.toString()))
				}
			}
	}

}