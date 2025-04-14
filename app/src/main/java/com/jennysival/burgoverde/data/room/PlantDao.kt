package com.jennysival.burgoverde.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jennysival.burgoverde.data.PlantModel

@Dao
interface PlantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plantModel: PlantModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<PlantModel>)

    @Query("SELECT * FROM plants_table ORDER BY timestamp DESC")
    suspend fun getAllPlants(): List<PlantModel>

    @Delete
    suspend fun deletePlant(plantModel: PlantModel)

    @Query("DELETE FROM plants_table")
    suspend fun clearAllPlants()
}
