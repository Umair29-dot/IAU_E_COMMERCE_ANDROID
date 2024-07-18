package com.example.finalprojectpractice.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpractice.data.User
import com.example.finalprojectpractice.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
	private val firestore: FirebaseFirestore,
	private val auth: FirebaseAuth,
	private val storage: StorageReference,
	private val app: Application
): AndroidViewModel(app) {

	private val _user = MutableStateFlow<Resource<User>>(Resource.UnSpecified())
	val user = _user.asStateFlow()

	private val _updateUser = MutableStateFlow<Resource<User>>(Resource.UnSpecified())
	val updateUser = _updateUser.asStateFlow()

	init {
		getUser()
	}

	fun getUser() {
		viewModelScope.launch {
			_user.emit(Resource.Loading())
		}
		firestore.collection("user").document(auth.uid!!)
			.get()
			.addOnSuccessListener {
				val user = it.toObject(User::class.java)
				user?.let {
					viewModelScope.launch {
						_user.emit(Resource.Success(it))
					}
				}
			}
			.addOnFailureListener {
				viewModelScope.launch {
					_user.emit(Resource.Error(it.message.toString()))
				}
			}
	}

	fun updateUser(user:User, imageUri: Uri?) {
		viewModelScope.launch {
			_updateUser.emit(Resource.Loading())
		}
		if(imageUri == null) {
			saveUserInformation(user, true) //if user don't change the image, we will retrieve old image
		} else {
			saveUserWithNewImage(user, imageUri)
		}
	}

	private fun saveUserWithNewImage(user: User, imageUri: Uri) {
		viewModelScope.launch {
			try {
				val imageBitmap = MediaStore.Images.Media.getBitmap(
					app.contentResolver, imageUri
				)
				val byteArrayOutputStream = ByteArrayOutputStream()
				imageBitmap.compress(Bitmap.CompressFormat.JPEG, 96, byteArrayOutputStream)
				val imageByteArray = byteArrayOutputStream.toByteArray()
				val imageDirectory = storage.child("profileImages/${auth.uid}/${UUID.randomUUID()}")
				val result = imageDirectory.putBytes(imageByteArray).await()
				val imageUri = result.storage.downloadUrl.await().toString()
				saveUserInformation(user.copy(imagePath = imageUri), false)
			} catch (e: Exception) {
				viewModelScope.launch {
					_user.emit(Resource.Error(e.message.toString()))
				}
			}
		}
	}

	private fun saveUserInformation(user: User, shouldRetrieveOldImage: Boolean) {
		firestore.runTransaction {transaction ->
			val documentReference = firestore.collection("user").document(auth.uid!!)
			if(shouldRetrieveOldImage) { //getting old image and setting again
				val currentUser = transaction.get(documentReference).toObject(User::class.java)
				val newUser = user.copy(imagePath = currentUser?.imagePath ?: "")
				transaction.set(documentReference, newUser)
			} else { //setting totally new data
				transaction.set(documentReference, user)
			}
		}
			.addOnSuccessListener {
				viewModelScope.launch {
					_updateUser.emit(Resource.Success(user))
				}
			}
			.addOnFailureListener {
				viewModelScope.launch {
					_user.emit(Resource.Error(it.message.toString()))
				}
			}
	}
}