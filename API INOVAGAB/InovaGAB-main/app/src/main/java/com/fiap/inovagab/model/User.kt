package com.fiap.inovagab.model

data class User(
    val id: String = "",
    val nome: String = "",
    val email: String = "",
    val perfil: String = "",
    val dataCriacao: String? = null
)