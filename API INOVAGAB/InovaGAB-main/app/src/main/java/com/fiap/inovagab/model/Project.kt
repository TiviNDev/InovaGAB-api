package com.fiap.inovagab.model

data class Project(
    val id: String = "",
    val nome: String = "",
    val descricao: String = "",
    val ideiaId: String = "",
    val categoria: String = "",
    val status: String = "planejamento",
    val investimento: Double = 0.0,
    val retornoEsperado: Double = 0.0,
    val prazoMeses: Int = 0,
    val progresso: Int = 0,
    val gestorId: String = "",
    val dataCriacao: String? = null
) {
    val roiPercentual: Double
        get() = if (investimento > 0) ((retornoEsperado - investimento) / investimento) * 100 else 0.0
}