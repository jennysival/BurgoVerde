package com.jennysival.burgoverde.ui.userRegister

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.jennysival.burgoverde.data.UserModel
import com.jennysival.burgoverde.usecase.AuthUseCase
import com.jennysival.burgoverde.utils.ViewState
import kotlinx.coroutines.launch

class UserRegisterViewModel(private val useCase: AuthUseCase) : ViewModel() {

    private val _registerState = MutableLiveData<ViewState<AuthResult>>()
    val registerState: LiveData<ViewState<AuthResult>> = _registerState

    fun registerUser(user: UserModel) {
        _registerState.value = ViewState.Loading(true)

        viewModelScope.launch {
            try {
                _registerState.value =
                    useCase.registerUser(user = user)
            } catch (e: Exception) {
                ViewState.Error(Throwable(e.message))
            } finally {
                _registerState.value = ViewState.Loading(false)
            }
        }
    }
}
