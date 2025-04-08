package com.jennysival.burgoverde.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.jennysival.burgoverde.data.UserModel
import com.jennysival.burgoverde.usecase.AuthUseCase
import com.jennysival.burgoverde.utils.viewstate.AuthViewState
import com.jennysival.burgoverde.utils.mapper.mapFirebaseExceptionToAuthError
import kotlinx.coroutines.launch

class AuthViewModel(private val useCase: AuthUseCase) : ViewModel() {

    private val _authState = MutableLiveData<AuthViewState<AuthResult>>()
    val authState: LiveData<AuthViewState<AuthResult>> = _authState

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    fun registerUser(user: UserModel) {
        _loadingState.value = true

        viewModelScope.launch {
            try {
                _authState.value = useCase.registerUser(user = user)
                _loadingState.value = false
            } catch (e: Exception) {
                val authError = mapFirebaseExceptionToAuthError(e)
                _authState.value = AuthViewState.Error(authError)
                _loadingState.value = false
            }
        }
    }

    fun login(email: String, password: String) {
        _loadingState.value = true

        viewModelScope.launch {
            try {
                _authState.value = useCase.login(email = email, password = password)
                _loadingState.value = false
            } catch (e: Exception) {
                val authError = mapFirebaseExceptionToAuthError(e)
                Log.e("FirebaseError", "Exception: ${e::class.java.simpleName}, message: ${e.message}, code: ${(e as? FirebaseAuthInvalidUserException)?.errorCode}")
                _authState.value = AuthViewState.Error(authError)
                _loadingState.value = false
            }
        }
    }

    fun isUserLoggedIn(): Boolean = useCase.checkUserSession()
}
