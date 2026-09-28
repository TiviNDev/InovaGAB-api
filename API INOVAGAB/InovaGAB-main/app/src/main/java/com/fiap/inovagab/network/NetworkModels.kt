package com.fiap.inovagab.network

import com.google.gson.annotations.SerializedName

// Autenticação
data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: String
)

data class AuthResponse(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val role: String
)

// Estratégias
data class StrategyDto(
    val id: String? = null,
    val title: String,
    val description: String,
    val category: String,
    val campaign: String,
    val active: Boolean = true,
    val createdAt: String? = null
)

// Ideias
data class IdeaDto(
    val id: String? = null,
    val title: String,
    val description: String,
    val strategyId: String,
    val authorId: String? = null,
    val authorName: String? = null,
    val status: String = "Pendente",
    val scoreAi: Double? = null,
    val feedbackAi: String? = null,
    val createdAt: String? = null
)

data class ReviewIdeaRequest(
    val approved: Boolean,
    val feedback: String?
)

// Projetos
data class ProjectDto(
    val id: String? = null,
    val title: String,
    val description: String,
    val strategyId: String,
    val ideaId: String? = null,
    val status: String,
    val stage: String,
    val investment: Double,
    val expectedReturn: Double,
    val deadline: String? = null
)