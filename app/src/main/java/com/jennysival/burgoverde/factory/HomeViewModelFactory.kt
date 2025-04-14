package com.jennysival.burgoverde.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.jennysival.burgoverde.data.room.PlantDao
import com.jennysival.burgoverde.data.room.PlantDatabase
import com.jennysival.burgoverde.repository.UserRepository
import com.jennysival.burgoverde.repository.plant.PlantRepositoryImpl
import com.jennysival.burgoverde.ui.home.HomeViewModel
import com.jennysival.burgoverde.usecase.UserUseCase
import com.jennysival.burgoverde.usecase.plant.PlantUseCase
import com.jennysival.burgoverde.utils.helper.SharedPreferencesHelper

class HomeViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val dao: PlantDao = PlantDatabase.getPlantDatabase(context).plantDao()
    private val auth = Firebase.auth
    private val storage = Firebase.storage
    private val firestore = Firebase.firestore
    private val sharedPrefs = SharedPreferencesHelper(context)
    private val plantRepository = PlantRepositoryImpl(
        storage = storage,
        plantDao = dao,
        firestore = firestore,
        auth = auth
        )
    private val userRepository = UserRepository(
        auth = auth,
        storage = storage,
        db = firestore,
        sharedPrefs = sharedPrefs,
        plantDao = dao
    )
    private val plantUseCase = PlantUseCase(plantRepository = plantRepository)
    private val userUseCase = UserUseCase(userRepository = userRepository)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(plantUseCase = plantUseCase, userUseCase = userUseCase) as T
    }
}
