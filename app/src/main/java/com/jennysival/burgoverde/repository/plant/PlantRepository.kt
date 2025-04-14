package com.jennysival.burgoverde.repository.plant

import android.net.Uri
import com.jennysival.burgoverde.data.PlantModel

interface PlantRepository {
    suspend fun savePlant(plant: PlantModel, imageUri: Uri): Result<Unit>
    suspend fun syncPlantsFromFirestore(): Result<Unit>
    suspend fun getAllPlants(): List<PlantModel>
    suspend fun deletePlant(plant: PlantModel)
    suspend fun getCollectivePlantCount(): Result<Int>
    suspend fun getUserPlantCount(userId: String): Result<Int>
}