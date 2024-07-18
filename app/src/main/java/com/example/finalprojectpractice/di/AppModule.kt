package com.example.finalprojectpractice.di

import com.example.finalprojectpractice.util.FirebaseCommans
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)    //It specify the life of dependencies that are inside this Module
object AppModule {

	@Provides
	@Singleton  //Just create one-time instance
	fun provideFirebaseAuth() = FirebaseAuth.getInstance()

	@Provides
	@Singleton
	fun provideFirebaseFirestore() = Firebase.firestore

	@Provides
	@Singleton
	fun provideFirebaseCommon(
		firebaseAuth: FirebaseAuth,
		firestore: FirebaseFirestore
	) = FirebaseCommans(firestore, firebaseAuth)

	@Provides
	@Singleton
	fun provideStorgae() = FirebaseStorage.getInstance().reference

}