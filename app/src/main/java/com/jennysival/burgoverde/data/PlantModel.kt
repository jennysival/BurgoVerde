package com.jennysival.burgoverde.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jennysival.burgoverde.data.room.PlantDatabase.Companion.PLANTS_TABLE_NAME

@Entity(tableName = PLANTS_TABLE_NAME)
data class PlantModel(
    @PrimaryKey var id: String = "",
    var name: String = "",
    var firebaseUrl: String = "",
    var localUri: String = "",
    var timestamp: Long = System.currentTimeMillis(),
    var author: String = "",
    var userId: String? = null
)
