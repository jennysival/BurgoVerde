package com.jennysival.burgoverde.ui.plantRegister

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jennysival.burgoverde.data.PlantModel
import com.jennysival.burgoverde.usecase.plant.PlantUseCase
import com.jennysival.burgoverde.utils.helper.ImageCompressor.compressImage
import com.jennysival.burgoverde.utils.viewstate.PlantViewState
import kotlinx.coroutines.launch

class PlantViewModel(
    private val useCase: PlantUseCase
) : ViewModel() {

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _uploadState = MutableLiveData<PlantViewState<Unit>>()
    val uploadState: LiveData<PlantViewState<Unit>> = _uploadState

    private val _plantListState = MutableLiveData<PlantViewState<List<PlantModel>>>()
    val plantListState: LiveData<PlantViewState<List<PlantModel>>> = _plantListState

    fun savePlant(context: Context, plant: PlantModel, imageUri: Uri) {
        _loadingState.value = true
        viewModelScope.launch {
            try {
                val compressedUri = compressImage(context, imageUri)
                val result = useCase.savePlant(plant, compressedUri)
                _uploadState.value = result
            } catch (e: Exception) {
                _uploadState.value = PlantViewState.Error(e)
            } finally {
                _loadingState.value = false
            }
        }
    }

    fun getPlants() {
        viewModelScope.launch {
            try {
                val plantsList = useCase.getAllPlants()
                _plantListState.value = plantsList
            } catch (e: Exception) {
                _plantListState.value = PlantViewState.Error(e)
            }
        }
    }

    fun syncPlants() {
        viewModelScope.launch {
            useCase.syncPlants()
        }
    }

    fun deletePlant(plant: PlantModel) {
        viewModelScope.launch {
            try {
                useCase.deletePlant(plant)
                _plantListState.value = useCase.getAllPlants()
            } catch (e: Exception) {
                _plantListState.value = PlantViewState.Error(e)
            }
        }
    }

    fun clearUploadState() {
        _uploadState.value = PlantViewState.None
    }

    fun clearListState() {
        _plantListState.value = PlantViewState.None
    }
}
