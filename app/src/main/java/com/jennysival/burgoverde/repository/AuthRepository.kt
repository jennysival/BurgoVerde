package com.jennysival.burgoverde.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jennysival.burgoverde.data.UserModel
import com.jennysival.burgoverde.utils.SharedPreferencesHelper
import com.jennysival.burgoverde.utils.USER_UID_ERROR
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val sharedPrefs: SharedPreferencesHelper
) {

    suspend fun registerUser(userModel: UserModel): Result<AuthResult> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(userModel.email, userModel.password).await()
            val userId = authResult.user?.uid ?: throw Exception(USER_UID_ERROR)

            saveUserData(userId = userId, userModel = userModel)
            sharedPrefs.saveUserName(name = userModel.name)
            Result.success(authResult)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<AuthResult> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()

            auth.currentUser?.uid?.let { userId ->
                val name = getUserNameFromFirestore(userId = userId)
                if (name != null) {
                    sharedPrefs.saveUserName(name = name)
                }
            }
            Result.success(authResult)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getUserEmail(): String = auth.currentUser?.email.toString()

    fun getUserName(): String? = sharedPrefs.getUserName()


    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    private suspend fun saveUserData(userId: String, userModel: UserModel): Result<Unit> {
        return try {
            val userData = hashMapOf(NAME_KEY to userModel.name, AGE_KEY to userModel.age, EMAIL_KEY to getUserEmail())
            db.collection(USERS_COLLECTION).document(userId).set(userData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun getUserNameFromFirestore(userId: String): String? {
        return try {
            val document = db.collection(USERS_COLLECTION).document(userId).get().await()
            document.getString(NAME_KEY)
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        const val NAME_KEY = "name"
        const val AGE_KEY = "age"
        const val EMAIL_KEY = "email"
        const val USERS_COLLECTION = "users"
    }
}
