package com.jennysival.burgoverde.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var name: String = "",
    var age: Int = 0,
    var email: String = "",
    var password: String = "",
): Parcelable