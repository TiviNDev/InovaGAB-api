package com.fiap.inovagab.model

data class Idea(
    val id: String = "",
    val titulo: String = "",
    val descricao: String = "",
    val categoria: String = "",
    val status: String = "pendente",
    val autorId: String = "",
    val autorNome: String = "",
    val comentarioGestor: String = "",
    val dataCriacao: String? = null
)