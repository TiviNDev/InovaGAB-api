package com.fiap.inovagab.model

data class Strategy(
    val id: String = "",
    val titulo: String = "",
    val descricao: String = "",
    val ativo: Boolean = true,
    val dataCriacao: String? = null
)