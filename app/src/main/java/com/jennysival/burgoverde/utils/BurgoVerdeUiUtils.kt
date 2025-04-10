package com.jennysival.burgoverde.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun View.hideKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}

fun showSnackBar(@StringRes messageRes: Int, view: View, context: Context) {
    Snackbar.make(view, context.getString(messageRes), Snackbar.LENGTH_LONG).show()
}
