package com.example.finalprojectpractice.data

data class User(
	val firstName:String,
	val lastName:String,
	val email:String,
	var imagePath:String = ""
){
	constructor():this("","","","")  // while dealing with firebase, we have to make secondary constructor
}

