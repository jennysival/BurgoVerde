package com.jennysival.burgoverde.usecase

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jennysival.burgoverde.data.UserModel
import com.jennysival.burgoverde.repository.AuthRepository
import com.jennysival.burgoverde.utils.helper.SharedPreferencesHelper
import com.jennysival.burgoverde.utils.viewstate.AuthViewState
import com.jennysival.burgoverde.utils.mapper.mapFirebaseExceptionToAuthError

class AuthUseCase(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val sharedPrefs: SharedPreferencesHelper,
    private val authRepository: AuthRepository = AuthRepository(
        auth = auth,
        db = db,
        sharedPrefs = sharedPrefs
    )
) {

    suspend fun registerUser(user: UserModel): AuthViewState<AuthResult> {
        return try {
            val result = authRepository.registerUser(userModel = user)
            result.fold(
                onSuccess = { authResult -> AuthViewState.Success(authResult) },
                onFailure = { exception ->
                    val authError = mapFirebaseExceptionToAuthError(exception)
                    AuthViewState.Error(authError)
                }
            )
        } catch (e: Exception) {
            val authError = mapFirebaseExceptionToAuthError(e)
            AuthViewState.Error(authError)
        }
    }

    suspend fun login(email: String, password: String): AuthViewState<AuthResult> {
        return try {
            val result = authRepository.login(email = email, password = password)
            result.fold(
                onSuccess = { authResult -> AuthViewState.Success(authResult) },
                onFailure = { exception ->
                    val authError = mapFirebaseExceptionToAuthError(exception)
                    AuthViewState.Error(authError)
                }
            )
        } catch (e: Exception) {
            val authError = mapFirebaseExceptionToAuthError(e)
            AuthViewState.Error(authError)
        }
    }

    suspend fun resetPassword(email: String): AuthViewState<Unit> {
        return try {
            val result = authRepository.resetPassword(email = email)
            result.fold(
                onSuccess = { resetResult -> AuthViewState.Success(resetResult) },
                onFailure = { exception ->
                    val resetError = mapFirebaseExceptionToAuthError(exception)
                    AuthViewState.Error(resetError)
                }
            )
        } catch (e: Exception) {
            val resetError = mapFirebaseExceptionToAuthError(e)
            AuthViewState.Error(resetError)
        }
    }

    fun checkUserSession(): Boolean {
        return authRepository.isUserLoggedIn()
    }

}
