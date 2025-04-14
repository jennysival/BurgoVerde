package com.jennysival.burgoverde.utils.helper

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.applicationContext.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)

    fun saveUserName(name: String) {
        sharedPreferences.edit().putString(USER_NAME, name).apply()
    }

    fun saveUserId(id: String) {
        sharedPreferences.edit().putString(USER_ID, id).apply()
    }

    fun getUserName(): String? {
        return sharedPreferences.getString(USER_NAME, null)
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(USER_ID, null)
    }


    fun saveProfileImageUrl(url: String) {
        sharedPreferences.edit().putString(PROFILE_IMAGE_URL, url).apply()
    }

    fun getProfileImageUrl(): String? {
        return sharedPreferences.getString(PROFILE_IMAGE_URL, null)
    }

    fun clearUserData() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        const val USER_PREFS = "user_prefs"
        const val USER_NAME = "user_name"
        const val USER_ID = "user_id"
        const val PROFILE_IMAGE_URL = "profile_image_url"
    }
}