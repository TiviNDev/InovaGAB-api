package com.fiap.inovagab.repository

import com.fiap.inovagab.model.User
import com.fiap.inovagab.network.ApiClient
import com.fiap.inovagab.network.LoginRequest
import com.fiap.inovagab.network.RegisterRequest

class UserRepository {

    private val api = ApiClient.apiService

    companion object {
        var loggedUser: User? = null
    }

    var currentUser: User?
        get() = loggedUser
        set(value) {
            loggedUser = value
        }

    val userLogado: User?
        get() = loggedUser

    suspend fun login(email: String, pass: String): User? {
        return try {
            val response = api.login(LoginRequest(email, pass))
            if (response.isSuccessful && response.body() != null) {
                val authData = response.body()!!
                ApiClient.authToken = authData.token

                val user = User(
                    id = authData.user.id,
                    nome = authData.user.name,
                    email = authData.user.email,
                    perfil = authData.user.role
                )
                loggedUser = user
                user
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun register(nome: String, email: String, pass: String, perfil: String): User? {
        return try {
            val response = api.register(RegisterRequest(nome, email, pass, perfil))
            if (response.isSuccessful && response.body() != null) {
                val authData = response.body()!!
                ApiClient.authToken = authData.token

                val user = User(
                    id = authData.user.id,
                    nome = authData.user.name,
                    email = authData.user.email,
                    perfil = authData.user.role
                )
                loggedUser = user
                user
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun buscarUsuario(uid: String, callback: (User?) -> Unit) {
        callback(loggedUser)
    }

    fun logout() {
        ApiClient.authToken = null
        loggedUser = null
    }
}