package com.maka.launcher.data.repository

import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import com.maka.launcher.data.SupabaseClient
import com.maka.launcher.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepository {
    private val supabase = SupabaseClient.client

    suspend fun signUp(email: String, password: String): Result<User> = runCatching {
        val response = supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
        User(
            id = response.id,
            email = response.email ?: "",
            coinBalance = 0,
            settings = UserSettings()
        )
    }

    suspend fun signIn(email: String, password: String): Result<User> = runCatching {
        val response = supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
        User(
            id = response.id,
            email = response.email ?: "",
            coinBalance = 0,
            settings = UserSettings()
        )
    }

    suspend fun signOut() {
        supabase.auth.signOut()
    }

    fun getCurrentUser(): Flow<User?> {
        return supabase.auth.currentUserFlow.map { session ->
            session?.let {
                User(
                    id = it.id,
                    email = it.email ?: "",
                    coinBalance = 0,
                    settings = UserSettings()
                )
            }
        }
    }

    suspend fun resetPassword(email: String) {
        supabase.auth.resetPasswordForEmail(email)
    }

    suspend fun updatePassword(newPassword: String) {
        supabase.auth.modifyUser {
            password = newPassword
        }
    }
}