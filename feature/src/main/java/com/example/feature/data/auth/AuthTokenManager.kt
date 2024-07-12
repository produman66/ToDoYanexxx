package com.example.feature.data.auth

import android.content.Context


/**
 * Manager class for handling authentication token storage in SharedPreferences.
 */
class AuthTokenManager(val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("todo_prefs", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        with(sharedPreferences.edit()) {
            putString("auth_token", token)
            apply()
        }
    }


    fun getAuthToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }


    fun clearAuthToken() {
        with(sharedPreferences.edit()) {
            remove("auth_token")
            apply()
        }
    }
}