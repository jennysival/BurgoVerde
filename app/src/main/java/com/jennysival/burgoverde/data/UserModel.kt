package com.jennysival.burgoverde.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val name: String,
    val age: String,
    val email: String,
    val password: String,
    val confirmPassword: String
): Parcelable