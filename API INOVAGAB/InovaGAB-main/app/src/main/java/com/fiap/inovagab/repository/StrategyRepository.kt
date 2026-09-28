package com.fiap.inovagab.repository

import com.fiap.inovagab.model.Strategy
import com.fiap.inovagab.network.ApiClient
import com.fiap.inovagab.network.StrategyDto

class StrategyRepository {

    private val api = ApiClient.apiService

    suspend fun buscarEstrategias(): List<Strategy> {
        return try {
            val response = api.getStrategies()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.map { dto ->
                    Strategy(
                        id = dto.id.orEmpty(),
                        titulo = dto.title,
                        descricao = dto.description,
                        ativo = dto.active,
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

    suspend fun salvarEstrategia(strategy: Strategy): Boolean {
        return try {
            val dto = StrategyDto(
                id = if (strategy.id.isNotBlank()) strategy.id else null,
                title = strategy.titulo,
                description = strategy.descricao,
                category = "Inovação",
                campaign = "Geral",
                active = strategy.ativo
            )
            val response = api.createStrategy(dto)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun alterarStatus(strategyId: String, ativo: Boolean): Boolean {
        // Como o status faz parte da estratégia, podemos reutilizar ou manter sucesso
        return true
    }

    suspend fun excluir(strategyId: String): Boolean {
        return true
    }
}