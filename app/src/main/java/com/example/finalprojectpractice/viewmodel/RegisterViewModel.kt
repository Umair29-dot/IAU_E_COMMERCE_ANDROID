package com.example.finalprojectpractice.viewmodel

import androidx.lifecycle.ViewModel
import com.example.finalprojectpractice.data.User
import com.example.finalprojectpractice.util.Constants
import com.example.finalprojectpractice.util.RegisterFieldState
import com.example.finalprojectpractice.util.RegisterValidation
import com.example.finalprojectpractice.util.Resource
import com.example.finalprojectpractice.util.validateEmail
import com.example.finalprojectpractice.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
	private val firebaseAuth: FirebaseAuth,
	private val db: FirebaseFirestore
	): ViewModel(){

	private val _register = MutableStateFlow<Resource<User>>(Resource.UnSpecified())
	val register: Flow<Resource<User>> = _register.asStateFlow()

	private val _validation = Channel<RegisterFieldState>()
	val validation = _validation.receiveAsFlow()

	fun createAccountWithEmailAndPassword(user: User, password: String) {
		if(checkValidation(user, password)) {
			runBlocking {
				_register.emit(Resource.Loading())
			}
			firebaseAuth.createUserWithEmailAndPassword(user.email, password)
				.addOnSuccessListener {
					it.user?.let {
						saveUserInfo(it.uid, user)
					}
				}
				.addOnFailureListener {
					_register.value = Resource.Error(it.message.toString())
				}
		} else {
			val registerFieldState = RegisterFieldState(
				validateEmail(user.email), validatePassword(password)
			)
			runBlocking {
				_validation.send(registerFieldState)
			}
		}
	}

	private fun saveUserInfo(userUid: String, user: User) {
		db.collection(Constants.USER_COLLECTION)
			.document(userUid)
			.set(user)
			.addOnSuccessListener {
				_register.value = Resource.Success(user)
			}
			.addOnFailureListener {
				_register.value = Resource.Error(it.message.toString())
			}
	}

	private fun checkValidation(user: User, password: String): Boolean {
		val emailValidation = validateEmail(user.email)  //Inside util.ValidationCheck
		val passwordValidation = validatePassword(password) //Inside util.ValidationCheck

		return emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success
	}

}