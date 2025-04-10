package com.jennysival.burgoverde.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jennysival.burgoverde.repository.AuthRepository.Companion.USERS_COLLECTION
import com.jennysival.burgoverde.repository.AuthRepository.Companion.USER_UID_ERROR
import com.jennysival.burgoverde.utils.helper.SharedPreferencesHelper
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val db: FirebaseFirestore,
    private val sharedPrefs: SharedPreferencesHelper
) {

    suspend fun uploadProfileImage(uri: Uri): Result<String> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception(USER_UID_ERROR)
            val ref = storage.reference.child("profile_images/$uid.jpg")

            ref.putFile(uri).await()
            val downloadUrl = ref.downloadUrl.await().toString()

            db.collection(USERS_COLLECTION).document(uid)
                .update(PROFILE_IMAGE_KEY, downloadUrl)
                .await()

            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProfileImage(): Result<String> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception(USER_UID_ERROR)
            val document = db.collection(USERS_COLLECTION).document(uid).get().await()
            val imageUrl = document.getString(PROFILE_IMAGE_KEY) ?: ""
            Result.success(imageUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUserName(): String? = sharedPrefs.getUserName()

    fun logout() {
        auth.signOut()
    }

    companion object {
        private const val PROFILE_IMAGE_KEY = "profileImageUrl"
    }
}
