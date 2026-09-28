package com.fiap.inovagab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiap.inovagab.model.Strategy
import com.fiap.inovagab.repository.StrategyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StrategyViewModel : ViewModel() {

    private val repository = StrategyRepository()

    private val _strategies = MutableLiveData<List<Strategy>>()
    val strategies: LiveData<List<Strategy>> = _strategies

    fun carregarEstrategias() {
        viewModelScope.launch {
            val lista = withContext(Dispatchers.IO) {
                repository.buscarEstrategias()
            }
            _strategies.value = lista
        }
    }

    fun salvarEstrategia(strategy: Strategy, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                repository.salvarEstrategia(strategy)
            }
            if (success) {
                carregarEstrategias()
            }
            onResult(success)
        }
    }

    fun alterarStatus(strategyId: String, ativo: Boolean, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                repository.alterarStatus(strategyId, ativo)
            }
            if (success) {
                carregarEstrategias()
            }
            onResult(success)
        }
    }

    fun excluir(strategyId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                repository.excluir(strategyId)
            }
            if (success) {
                carregarEstrategias()
            }
            onResult(success)
        }
    }
}