package com.jennysival.burgoverde.usecase

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jennysival.burgoverde.data.UserModel
import com.jennysival.burgoverde.repository.AuthRepository
import com.jennysival.burgoverde.utils.DEFAULT_USER_NAME
import com.jennysival.burgoverde.utils.SharedPreferencesHelper
import com.jennysival.burgoverde.utils.ViewState

class AuthUseCase(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val sharedPrefs: SharedPreferencesHelper,
    private val authRepository: AuthRepository = AuthRepository(auth = auth, db = db, sharedPrefs = sharedPrefs)
) {

    suspend fun registerUser(user: UserModel): ViewState<AuthResult> {
        return try {
            val result = authRepository.registerUser(userModel = user)
            result.fold(
                onSuccess = { authResult -> ViewState.Success(authResult) },
                onFailure = { exception -> ViewState.Error(exception) }
            )
        } catch (e: Exception) {
            ViewState.Error(e)
        }
    }

    suspend fun login(email: String, password: String): ViewState<AuthResult> {
        return try {
            val result = authRepository.login(email = email, password = password)
            result.fold(
                onSuccess = { authResult -> ViewState.Success(authResult) },
                onFailure = { exception -> ViewState.Error(exception) }
            )
        } catch (e: Exception) {
            ViewState.Error(e)
        }
    }

    fun checkUserSession(): Boolean {
        return authRepository.isUserLoggedIn()
    }

    fun logout() {
        authRepository.logout()
    }

    fun getUserName(): String {
        return authRepository.getUserName() ?: DEFAULT_USER_NAME
    }

}