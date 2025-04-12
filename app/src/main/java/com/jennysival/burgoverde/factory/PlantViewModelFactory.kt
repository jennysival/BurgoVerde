package com.jennysival.burgoverde.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.jennysival.burgoverde.data.room.PlantDao
import com.jennysival.burgoverde.data.room.PlantDatabase
import com.jennysival.burgoverde.repository.plant.PlantRepositoryImpl
import com.jennysival.burgoverde.ui.plantRegister.PlantViewModel
import com.jennysival.burgoverde.usecase.plant.PlantUseCase

class PlantViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val dao: PlantDao = PlantDatabase.getPlantDatabase(context).plantDao()
    private val storage = Firebase.storage
    private val firestore = Firebase.firestore
    private val repository = PlantRepositoryImpl(storage = storage, plantDao = dao, firestore = firestore)
    private val useCase = PlantUseCase(repository = repository)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlantViewModel(useCase = useCase) as T
    }
}