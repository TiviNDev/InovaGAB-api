package com.fiap.inovagab.repository

import com.fiap.inovagab.model.Idea
import com.fiap.inovagab.network.ApiClient
import com.fiap.inovagab.network.IdeaDto
import com.fiap.inovagab.network.ReviewIdeaRequest
import com.fiap.inovagab.utils.Constants

class IdeaRepository {

    private val api = ApiClient.apiService

    suspend fun cadastrarIdeia(idea: Idea): Boolean {
        return try {
            val dto = IdeaDto(
                id = null,
                title = idea.titulo,
                description = idea.descricao,
                strategyId = if (idea.categoria.isNotBlank()) idea.categoria else "estrategia_geral",
                authorId = idea.autorId,
                authorName = idea.autorNome,
                status = Constants.STATUS_PENDING
            )
            val response = api.createIdea(dto)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun buscarTodasIdeias(): List<Idea> {
        return try {
            val response = api.getIdeas()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.map { dto ->
                    Idea(
                        id = dto.id.orEmpty(),
                        titulo = dto.title,
                        descricao = dto.description,
                        categoria = dto.strategyId,
                        status = mapStatus(dto.status),
                        autorId = dto.authorId.orEmpty(),
                        autorNome = dto.authorName.orEmpty(),
                        comentarioGestor = dto.feedbackAi ?: "",
                        dataCriacao = null
                    )
                }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun buscarMinhasIdeias(uid: String): List<Idea> {
        return buscarTodasIdeias().filter { it.autorId == uid }
    }

    suspend fun buscarIdeiasPendentes(): List<Idea> {
        return buscarTodasIdeias().filter { it.status == Constants.STATUS_PENDING }
    }

    suspend fun buscarIdeiasAprovadas(): List<Idea> {
        return buscarTodasIdeias().filter { it.status == Constants.STATUS_APPROVED }
    }

    suspend fun atualizarStatus(ideaId: String, status: String, comentario: String = ""): Boolean {
        return try {
            val isApproved = (status == Constants.STATUS_APPROVED)
            val request = ReviewIdeaRequest(
                approved = isApproved,
                feedback = comentario
            )
            val response = api.reviewIdea(ideaId, request)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    private fun mapStatus(status: String): String {
        return when (status.lowercase()) {
            "aprovada", "approved" -> Constants.STATUS_APPROVED
            "rejeitada", "reprovada", "rejected" -> Constants.STATUS_REJECTED
            else -> Constants.STATUS_PENDING
        }
    }
}