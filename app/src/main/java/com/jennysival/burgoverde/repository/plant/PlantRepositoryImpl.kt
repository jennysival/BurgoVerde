package com.jennysival.burgoverde.repository.plant

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jennysival.burgoverde.data.PlantModel
import com.jennysival.burgoverde.data.room.PlantDao
import com.jennysival.burgoverde.utils.IMAGE_FORMAT_SUFFIX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PlantRepositoryImpl(
    private val storage: FirebaseStorage,
    private val firestore: FirebaseFirestore,
    private val plantDao: PlantDao,
) : PlantRepository {

    override suspend fun savePlant(plant: PlantModel, imageUri: Uri): Result<Unit> {
        return try {
            val imageRef = storage.reference.child("$PLANTS_COLLECTION/${plant.id}$IMAGE_FORMAT_SUFFIX")

            val downloadUrl = withContext(Dispatchers.IO) {
                uploadImageAndGetDownloadUrl(imageRef, imageUri)
            }

            val updatedPlant = plant.copy(firebaseUrl = downloadUrl.toString())
            plantDao.insertPlant(updatedPlant)
            savePlantToFirestore(updatedPlant)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun uploadImageAndGetDownloadUrl(
        imageRef: StorageReference,
        imageUri: Uri
    ): Uri = suspendCoroutine { continuation ->
        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        continuation.resume(uri)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }

    private suspend fun savePlantToFirestore(plant: PlantModel) = withContext(Dispatchers.IO) {
        firestore.collection(PLANTS_COLLECTION)
            .document(plant.id)
            .set(plant)
    }

    override suspend fun syncPlantsFromFirestore(): Result<Unit> {
        return try {
            val firestorePlants = withContext(Dispatchers.IO) {
                firestore.collection(PLANTS_COLLECTION).get().await().toObjects(PlantModel::class.java)
            }

            val localPlants = plantDao.getAllPlants()
            val localIds = localPlants.map { it.id }

            val newPlants = firestorePlants.filter { it.id !in localIds }

            if (newPlants.isNotEmpty()) {
                plantDao.insertAll(newPlants)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun getAllPlants(): List<PlantModel> = plantDao.getAllPlants()

    override suspend fun deletePlant(plant: PlantModel) {
        plantDao.deletePlant(plant)

        firestore.collection(PLANTS_COLLECTION).document(plant.id).delete()

        val storageRef = storage.reference.child("$PLANTS_COLLECTION/${plant.id}$IMAGE_FORMAT_SUFFIX")

        try {
            storageRef.delete().await()
        } catch (e: Exception) {
            // do nothing
        }
    }

    companion object {
        private const val PLANTS_COLLECTION = "plants"
    }
}
