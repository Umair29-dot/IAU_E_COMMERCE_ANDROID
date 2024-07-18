package com.example.finalprojectpractice.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalprojectpractice.data.Category
import com.example.finalprojectpractice.viewmodel.AccessoryViewModel
import com.example.finalprojectpractice.viewmodel.ChairViewModel
import com.example.finalprojectpractice.viewmodel.CupboardViewModel
import com.example.finalprojectpractice.viewmodel.FurnitureViewModel
import com.example.finalprojectpractice.viewmodel.TableViewModel
import com.google.firebase.firestore.FirebaseFirestore

class ViewModelFactory(
	private val firestore: FirebaseFirestore,
    private val category: Category
	): ViewModelProvider.Factory {

	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(ChairViewModel::class.java)) {
			return ChairViewModel(firestore, category) as T
		}
		else if (modelClass.isAssignableFrom(CupboardViewModel::class.java)) {
			return CupboardViewModel(firestore, category) as T
		}
		else if (modelClass.isAssignableFrom(TableViewModel::class.java)) {
			return TableViewModel(firestore, category) as T
		}
		else if (modelClass.isAssignableFrom(AccessoryViewModel::class.java)) {
			return AccessoryViewModel(firestore, category) as T
		}
		else if (modelClass.isAssignableFrom(FurnitureViewModel::class.java)) {
			return FurnitureViewModel(firestore, category) as T
		}
		throw IllegalArgumentException("Unknown ViewModel class")
	}

}