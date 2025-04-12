package com.jennysival.burgoverde.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jennysival.burgoverde.data.room.PlantDatabase.Companion.PLANTS_TABLE_NAME

@Entity(tableName = PLANTS_TABLE_NAME)
data class PlantModel(
    @PrimaryKey val id: String,
    val name: String,
    val firebaseUrl: String,
    val localUri: String,
    val timestamp: Long = System.currentTimeMillis(),
    val author: String
)
