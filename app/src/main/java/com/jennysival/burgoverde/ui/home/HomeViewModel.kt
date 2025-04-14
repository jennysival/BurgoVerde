package com.jennysival.burgoverde.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jennysival.burgoverde.data.PlantCount
import com.jennysival.burgoverde.usecase.UserUseCase
import com.jennysival.burgoverde.usecase.plant.PlantUseCase
import com.jennysival.burgoverde.utils.viewstate.PlantViewState
import com.jennysival.burgoverde.utils.viewstate.ProfileViewState
import kotlinx.coroutines.launch

class HomeViewModel(
    private val plantUseCase: PlantUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {

    private val _plantsCountState = MutableLiveData<PlantViewState<PlantCount>>()
    val plantsCountState: LiveData<PlantViewState<PlantCount>> = _plantsCountState

    private val _profilePhotoState = MutableLiveData<ProfileViewState<String>>()
    val profilePhotoState: LiveData<ProfileViewState<String>> = _profilePhotoState

    fun getPlantsCount(userId: String) {
        viewModelScope.launch {
            try {
                val plantsCount = plantUseCase.getPlantCount(userId)
                _plantsCountState.value = plantsCount
            } catch (e: Exception) {
                _plantsCountState.value = PlantViewState.Error(e)
            }
        }
    }

    fun getProfilePhoto() {
        viewModelScope.launch {
            val cachedUrl = userUseCase.getCachedProfileImageUrl()
            if(!cachedUrl.isNullOrEmpty()) {
                _profilePhotoState.value = ProfileViewState.Success(cachedUrl)
            } else {
                try {
                    _profilePhotoState.value = userUseCase.getProfileImage()
                } catch (e: Exception) {
                    _profilePhotoState.value = ProfileViewState.Error(e)
                }
            }
        }
    }
}
