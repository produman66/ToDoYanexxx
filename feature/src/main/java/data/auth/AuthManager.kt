package data.auth

import android.content.Context


/**
 * Manager class for handling user authentication token storage and retrieval in SharedPreferences.
 */
class AuthManager(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("todo_prefs", Context.MODE_PRIVATE)

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getString("auth_token", "")?.isNotEmpty() ?: false
    }

    fun saveAuthToken(token: String) {
        with(sharedPreferences.edit()) {
            putString("auth_token", token)
            apply()
        }
    }

    fun getSavedToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    fun clearAuthToken() {
        with(sharedPreferences.edit()) {
            remove("auth_token")
            apply()
        }
    }
}