package com.jennysival.burgoverde.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class AuthRepository {
    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = Firebase.firestore

    fun registerUser(name: String, age: Int, email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid ?: ""
                saveUserData(userId = userId, name = name, age = age)
            } else {
                //erro ao registrar
            }
        }
    }

    fun logout(){
        auth.signOut()
    }

    fun login(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email,password)
    }

    fun getUserEmail(): String = auth.currentUser?.email.toString()

    fun saveUserData(userId: String, name: String, age: Int) {
        val user = hashMapOf(
            "name" to name,
            "age" to age,
            "email" to getUserEmail()
        )

        db.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener {
                //registo bem sucedido
            }
            .addOnFailureListener {
                //falha ao registrar
            }
    }
}