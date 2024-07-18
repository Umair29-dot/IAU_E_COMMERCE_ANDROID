package com.example.finalprojectpractice.util

import android.util.Patterns

fun validateEmail(email:String):RegisterValidation{
	return if(email.isEmpty()){
		RegisterValidation.Failed("Email can't be empty")
	} else if((!Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
		RegisterValidation.Failed("Wrong email format")
	} else  {
		RegisterValidation.Success
	}
}

fun validatePassword(password:String):RegisterValidation{
	return if(password.isEmpty()){
		RegisterValidation.Failed("Password can't be empty")
	} else if (password.length < 6) {
		RegisterValidation.Failed("Password should contain 6 character")
	} else {
		RegisterValidation.Success
	}
}