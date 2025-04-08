package com.jennysival.burgoverde.utils.mapper

import androidx.annotation.StringRes
import com.jennysival.burgoverde.R

sealed class AuthError(@StringRes val messageRes: Int) {
    object EmailAlreadyInUse : AuthError(R.string.burgoverde_duplicated_email)
    object WeakPassword : AuthError(R.string.burgoverde_weak_password)
    object WrongCredentials : AuthError(R.string.burgoverde_wrong_email_or_password)
    object Generic : AuthError(R.string.burgoverde_ops_text)
}
