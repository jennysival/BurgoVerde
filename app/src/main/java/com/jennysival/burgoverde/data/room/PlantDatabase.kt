package com.jennysival.burgoverde.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jennysival.burgoverde.data.PlantModel

@Database(entities = [PlantModel::class], version = 1)
abstract class PlantDatabase : RoomDatabase() {

    abstract fun plantDao(): PlantDao

    companion object {
        @Volatile
        private var INSTANCE: PlantDatabase? = null

        const val PLANTS_TABLE_NAME = "plants_table"
        private const val PLANTS_DATABASE_NAME = "plant_db"


        fun getPlantDatabase(context: Context): PlantDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, PlantDatabase::class.java, PLANTS_DATABASE_NAME
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
