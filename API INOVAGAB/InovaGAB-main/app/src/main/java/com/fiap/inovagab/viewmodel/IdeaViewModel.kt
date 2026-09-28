package com.fiap.inovagab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiap.inovagab.model.Idea
import com.fiap.inovagab.repository.IdeaRepository
import com.fiap.inovagab.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IdeaViewModel : ViewModel() {

    private val repository = IdeaRepository()

    private val _ideas = MutableLiveData<List<Idea>>()
    val ideas: LiveData<List<Idea>> = _ideas

    fun cadastrarIdeia(idea: Idea, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                repository.cadastrarIdeia(idea)
            }
            if (success) {
                carregarTodasIdeias()
            }
            onResult(success)
        }
    }

    fun carregarMinhasIdeias(uid: String) {
        viewModelScope.launch {
            val lista = withContext(Dispatchers.IO) {
                repository.buscarMinhasIdeias(uid)
            }
            _ideas.value = lista
        }
    }

    fun carregarIdeiasPendentes() {
        viewModelScope.launch {
            val lista = withContext(Dispatchers.IO) {
                repository.buscarIdeiasPendentes()
            }
            _ideas.value = lista
        }
    }

    fun carregarIdeiasAprovadas() {
        viewModelScope.launch {
            val lista = withContext(Dispatchers.IO) {
                repository.buscarIdeiasAprovadas()
            }
            _ideas.value = lista
        }
    }

    fun carregarTodasIdeias() {
        viewModelScope.launch {
            val lista = withContext(Dispatchers.IO) {
                repository.buscarTodasIdeias()
            }
            _ideas.value = lista
        }
    }

    fun aprovarIdeia(ideaId: String, comentario: String = "", onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                repository.atualizarStatus(ideaId, Constants.STATUS_APPROVED, comentario)
            }
            if (success) {
                carregarTodasIdeias()
            }
            onResult(success)
        }
    }

    fun rejeitarIdeia(ideaId: String, comentario: String = "", onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                repository.atualizarStatus(ideaId, Constants.STATUS_REJECTED, comentario)
            }
            if (success) {
                carregarTodasIdeias()
            }
            onResult(success)
        }
    }
}