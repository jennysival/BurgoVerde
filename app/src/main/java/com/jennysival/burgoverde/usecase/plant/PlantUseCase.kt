package com.jennysival.burgoverde.usecase.plant

import android.net.Uri
import com.jennysival.burgoverde.data.PlantCount
import com.jennysival.burgoverde.data.PlantModel
import com.jennysival.burgoverde.repository.plant.PlantRepository
import com.jennysival.burgoverde.utils.viewstate.PlantViewState

class PlantUseCase(
    private val plantRepository: PlantRepository
) {

    suspend fun savePlant(plant: PlantModel, imageUri: Uri): PlantViewState<Unit> {
        return try {
            val result = plantRepository.savePlant(plant, imageUri)
            result.fold(onSuccess = { uploadResult -> PlantViewState.Success(uploadResult) },
                onFailure = { exception -> PlantViewState.Error(exception) })
        } catch (e: Exception) {
            PlantViewState.Error(e)
        }
    }

    suspend fun getAllPlants(): PlantViewState<List<PlantModel>> {
        return try {
            val plantsList = plantRepository.getAllPlants()
            PlantViewState.Success(plantsList)
        } catch (e: Exception) {
            PlantViewState.Error(Exception(e.message))
        }
    }

    suspend fun syncPlants(): PlantViewState<Unit> {
        return try {
            val result = plantRepository.syncPlantsFromFirestore()
            result.fold(
                onSuccess = { PlantViewState.Success(Unit) },
                onFailure = { exception ->
                    PlantViewState.Error(exception)
                }
            )
        } catch (e: Exception) {
            PlantViewState.Error(e)
        }
    }

    suspend fun deletePlant(plant: PlantModel) {
        plantRepository.deletePlant(plant)
    }

    suspend fun getPlantCount(userId: String): PlantViewState<PlantCount> {
        return try {
            val totalCount = plantRepository.getCollectivePlantCount()
            val userCount = plantRepository.getUserPlantCount(userId)

            if (totalCount.isSuccess && userCount.isSuccess) {
                PlantViewState.Success(
                    PlantCount(
                        total = totalCount.getOrDefault(0),
                        user = userCount.getOrDefault(0)
                    )
                )
            } else {
                val exception = totalCount.exceptionOrNull() ?: userCount.exceptionOrNull()
                PlantViewState.Error(exception ?: Exception())
            }
        } catch (e: Exception) {
            PlantViewState.Error(e)
        }
    }

}
