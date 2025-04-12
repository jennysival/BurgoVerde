package com.jennysival.burgoverde.usecase

import android.net.Uri
import com.jennysival.burgoverde.repository.UserRepository
import com.jennysival.burgoverde.utils.DEFAULT_USER_NAME
import com.jennysival.burgoverde.utils.viewstate.ProfileViewState

class UserUseCase(
    private val userRepository: UserRepository
) {

    suspend fun uploadProfileImage(uri: Uri): ProfileViewState<String> {
        return try {
            val result = userRepository.uploadProfileImage(uri = uri)
            result.fold(
                onSuccess = { uploadResult -> ProfileViewState.Success(uploadResult) },
                onFailure = { exception -> ProfileViewState.Error(exception) }
            )
        } catch (e: Exception) {
            ProfileViewState.Error(e)
        }
    }

    suspend fun getProfileImage(): ProfileViewState<String> {
        return try {
            val result = userRepository.getProfileImage()
            result.fold(
                onSuccess = { imageResult -> ProfileViewState.Success(imageResult) },
                onFailure = { exception -> ProfileViewState.Error(exception) }
            )
        } catch (e: Exception) {
            ProfileViewState.Error(e)
        }
    }

    fun logout() {
        userRepository.logout()
    }

    fun getUserName(): String = userRepository.getUserName() ?: DEFAULT_USER_NAME

    fun getCachedProfileImageUrl(): String? = userRepository.getCachedProfileImageUrl()
}
