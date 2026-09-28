package com.fiap.inovagab.repository

import com.fiap.inovagab.model.Project
import com.fiap.inovagab.network.ApiClient
import com.fiap.inovagab.network.ProjectDto

class ProjectRepository {

    private val api = ApiClient.apiService

    suspend fun salvarProjeto(project: Project): Boolean {
        return try {
            val dto = ProjectDto(
                id = if (project.id.isNotBlank()) project.id else null,
                title = project.nome,
                description = project.descricao,
                strategyId = if (project.categoria.isNotBlank()) project.categoria else "estrategia_geral",
                ideaId = if (project.ideiaId.isNotBlank()) project.ideiaId else null,
                status = project.status,
                stage = "Progresso: ${project.progresso}%",
                investment = project.investimento,
                expectedReturn = project.retornoEsperado,
                deadline = "${project.prazoMeses} meses"
            )
            val response = api.createProject(dto)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun buscarProjetos(): List<Project> {
        return try {
            val response = api.getProjects()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.map { dto ->
                    Project(
                        id = dto.id.orEmpty(),
                        nome = dto.title,
                        descricao = dto.description,
                        ideiaId = dto.ideaId.orEmpty(),
                        categoria = dto.strategyId,
                        status = dto.status,
                        investimento = dto.investment,
                        retornoEsperado = dto.expectedReturn,
                        prazoMeses = parseDeadline(dto.deadline),
                        progresso = parseProgresso(dto.stage),
                        gestorId = "",
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

    suspend fun atualizarProgresso(projectId: String, progresso: Int): Boolean {
        return true
    }

    private fun parseDeadline(deadline: String?): Int {
        if (deadline == null) return 0
        val digits = deadline.filter { it.isDigit() }
        return digits.toIntOrNull() ?: 0
    }

    private fun parseProgresso(stage: String?): Int {
        if (stage == null) return 0
        val digits = stage.filter { it.isDigit() }
        return (digits.toIntOrNull() ?: 0).coerceIn(0, 100)
    }
}