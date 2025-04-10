package com.jennysival.burgoverde.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jennysival.burgoverde.usecase.UserUseCase
import com.jennysival.burgoverde.utils.viewstate.ProfileViewState
import kotlinx.coroutines.launch

class ProfileViewModel(private val useCase: UserUseCase) : ViewModel() {

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _uploadState = MutableLiveData<ProfileViewState<String>>()
    val uploadState: LiveData<ProfileViewState<String>> = _uploadState

    private val _imageState = MutableLiveData<ProfileViewState<String>>()
    val imageState: LiveData<ProfileViewState<String>> = _imageState

    fun uploadProfileImage(uri: Uri) {
        _loadingState.value = true

        viewModelScope.launch {
            try {
                _uploadState.value = useCase.uploadProfileImage(uri = uri)
                _loadingState.value = false
            } catch (e: Exception) {
                _uploadState.value = ProfileViewState.Error(e)
                _loadingState.value = false
            }
        }
    }

    fun getProfileImage() {
        _loadingState.value = true

        viewModelScope.launch {
            try {
                _imageState.value = useCase.getProfileImage()
                _loadingState.value = false
            } catch (e: Exception) {
                _imageState.value = ProfileViewState.Error(e)
                _loadingState.value = false
            }
        }
    }

    fun logout() {
        useCase.logout()
    }

    fun getUserName(): String {
        return useCase.getUserName()
    }
}
