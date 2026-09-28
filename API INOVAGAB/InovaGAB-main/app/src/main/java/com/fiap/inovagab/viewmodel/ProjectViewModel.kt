package com.fiap.inovagab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiap.inovagab.model.Project
import com.fiap.inovagab.repository.ProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectViewModel : ViewModel() {

    private val repository = ProjectRepository()

    private val _projects = MutableLiveData<List<Project>>()
    val projects: LiveData<List<Project>> = _projects

    fun carregarProjetos() {
        viewModelScope.launch {
            val lista = withContext(Dispatchers.IO) {
                repository.buscarProjetos()
            }
            _projects.value = lista
        }
    }

    fun salvarProjeto(project: Project, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                repository.salvarProjeto(project)
            }
            if (success) {
                carregarProjetos()
            }
            onResult(success)
        }
    }

    fun atualizarProgresso(projectId: String, progresso: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                repository.atualizarProgresso(projectId, progresso)
            }
            if (success) {
                carregarProjetos()
            }
            onResult(success)
        }
    }
}