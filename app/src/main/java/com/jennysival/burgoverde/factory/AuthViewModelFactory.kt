package com.jennysival.burgoverde.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.jennysival.burgoverde.ui.AuthViewModel
import com.jennysival.burgoverde.usecase.AuthUseCase
import com.jennysival.burgoverde.utils.helper.SharedPreferencesHelper

class AuthViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val sharedPrefs = SharedPreferencesHelper(context = context)
    private val useCase = AuthUseCase(auth = auth, db = db, sharedPrefs = sharedPrefs)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(useCase = useCase) as T
    }
}