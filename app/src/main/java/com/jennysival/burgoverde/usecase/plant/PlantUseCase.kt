package com.jennysival.burgoverde.usecase.plant

import android.net.Uri
import com.jennysival.burgoverde.data.PlantModel
import com.jennysival.burgoverde.repository.plant.PlantRepository
import com.jennysival.burgoverde.utils.viewstate.PlantViewState

class PlantUseCase(
    private val repository: PlantRepository
) {

    suspend fun savePlant(plant: PlantModel, imageUri: Uri): PlantViewState<Unit> {
        return try {
            val result = repository.savePlant(plant, imageUri)
            result.fold(onSuccess = { uploadResult -> PlantViewState.Success(uploadResult) },
                onFailure = { exception -> PlantViewState.Error(exception) })
        } catch (e: Exception) {
            PlantViewState.Error(e)
        }
    }

    suspend fun getAllPlants(): PlantViewState<List<PlantModel>> {
        return try {
            val plantsList = repository.getAllPlants()
            if (plantsList.isEmpty()) {
                PlantViewState.Error(Exception())
            } else {
                PlantViewState.Success(plantsList)
            }
        } catch (e: Exception) {
            PlantViewState.Error(Exception(e.message))
        }
    }

    suspend fun syncPlants(): PlantViewState<Unit> {
        return try {
            val result = repository.syncPlantsFromFirestore()
            result.fold(onSuccess = { PlantViewState.Success(Unit) },
                onFailure = { PlantViewState.Error(it) })
        } catch (e: Exception) {
            PlantViewState.Error(e)
        }
    }

    suspend fun deletePlant(plant: PlantModel) {
        repository.deletePlant(plant)
    }

}
