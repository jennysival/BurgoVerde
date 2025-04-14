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
import com.jennysival.burgoverde.ui.profile.ProfileViewModel
import com.jennysival.burgoverde.usecase.UserUseCase
import com.jennysival.burgoverde.utils.helper.SharedPreferencesHelper

class ProfileViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val dao: PlantDao = PlantDatabase.getPlantDatabase(context).plantDao()
    private val auth = Firebase.auth
    private val storage = Firebase.storage
    private val db = Firebase.firestore
    private val sharedPrefs = SharedPreferencesHelper(context = context)
    private val repository = UserRepository(
        auth = auth,
        storage = storage,
        db = db,
        sharedPrefs = sharedPrefs,
        plantDao = dao
    )
    private val useCase = UserUseCase(userRepository = repository)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(useCase = useCase) as T
    }
}