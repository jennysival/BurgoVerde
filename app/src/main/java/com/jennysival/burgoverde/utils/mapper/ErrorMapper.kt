package com.jennysival.burgoverde.utils.mapper

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

fun mapFirebaseExceptionToAuthError(throwable: Throwable): AuthError {
    return when (throwable) {
        is FirebaseAuthWeakPasswordException -> AuthError.WeakPassword
        is FirebaseAuthInvalidCredentialsException -> {
            if (throwable.message?.contains(FIREBASE_THROWABLE_MESSAGE) == true) {
                AuthError.WrongCredentials
            } else {
                AuthError.Generic
            }
        }
        is FirebaseAuthUserCollisionException -> AuthError.EmailAlreadyInUse
        else -> AuthError.Generic
    }
}

const val FIREBASE_THROWABLE_MESSAGE = "The supplied auth credential is incorrect"
