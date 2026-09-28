package com.fiap.inovagab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiap.inovagab.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel : ViewModel() {

    private val userRepository = UserRepository()

    fun login(email: String, senha: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                userRepository.login(email, senha)
            }
            onResult(user != null, user?.perfil)
        }
    }

    fun cadastrar(nome: String, email: String, senha: String, perfil: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                userRepository.register(nome, email, senha, perfil)
            }
            onResult(user != null, user?.perfil)
        }
    }

    fun getCurrentUserId(): String? = userRepository.currentUser?.id

    fun logout() {
        userRepository.logout()
    }
}