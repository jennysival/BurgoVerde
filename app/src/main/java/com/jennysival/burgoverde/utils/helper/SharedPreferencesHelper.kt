package com.jennysival.burgoverde.utils.helper

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.applicationContext.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)

    fun saveUserName(name: String) {
        sharedPreferences.edit().putString(USER_NAME, name).apply()
    }

    fun getUserName(): String? {
        return sharedPreferences.getString(USER_NAME, null)
    }

    fun clearUserData() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        const val USER_PREFS = "user_prefs"
        const val USER_NAME = "user_name"
    }
}