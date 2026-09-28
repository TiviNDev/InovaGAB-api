package com.fiap.inovagab.ui.manager

import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fiap.inovagab.model.Idea
import com.fiap.inovagab.model.Project
import com.fiap.inovagab.utils.Constants
import com.fiap.inovagab.utils.Ui
import com.fiap.inovagab.utils.toast
import com.fiap.inovagab.viewmodel.AuthViewModel
import com.fiap.inovagab.viewmodel.IdeaViewModel
import com.fiap.inovagab.viewmodel.ProjectViewModel

class ProjectFormActivity : AppCompatActivity() {
    private val projectViewModel: ProjectViewModel by viewModels()
    private val ideaViewModel: IdeaViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var ideaSpinner: android.widget.Spinner
    private lateinit var nomeInput: EditText
    private lateinit var descricaoInput: EditText
    private lateinit var investimentoInput: EditText
    private lateinit var retornoInput: EditText
    private lateinit var prazoInput: EditText
    private lateinit var progressoInput: EditText
    private lateinit var statusSpinner: android.widget.Spinner

    private var approvedIdeas: List<Idea> = emptyList()
    private var renderedWithIdeas = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        render(emptyList())

        ideaViewModel.ideas.observe(this) {
            approvedIdeas = it
            if (!renderedWithIdeas) {
                renderedWithIdeas = true
                render(it)
            }
        }

        ideaViewModel.carregarIdeiasAprovadas()
    }

    private fun render(ideas: List<Idea>) {
        val root = Ui.root(this)
        root.addView(Ui.header(this, "Novo Projeto", "Transforme uma ideia aprovada em projeto"))

        val content = Ui.scrollContent(this)

        val ideaTitles = if (ideas.isEmpty()) listOf("Nenhuma ideia aprovada disponível") else ideas.map { it.titulo }
        ideaSpinner = Ui.spinner(this, ideaTitles)

        nomeInput = Ui.input(this, "Ex: Projeto de Otimização de Rotas")
        descricaoInput = Ui.input(this, "Descreva o projeto", multiline = true)
        investimentoInput = Ui.input(this, "Ex: 50000", number = true)
        retornoInput = Ui.input(this, "Ex: 120000", number = true)
        prazoInput = Ui.input(this, "Ex: 6", number = true)
        progressoInput = Ui.input(this, "Ex: 30", number = true)
        statusSpinner = Ui.spinner(this, listOf("Planejamento", "Em execução", "Concluído"))

        content.addView(Ui.sectionTitle(this, "Ideia aprovada de origem"))
        content.addView(ideaSpinner)
        content.addView(Ui.inputBox(this, "Nome do projeto", nomeInput))
        content.addView(Ui.inputBox(this, "Descrição", descricaoInput))
        content.addView(Ui.inputBox(this, "Investimento inicial (R$)", investimentoInput))
        content.addView(Ui.inputBox(this, "Retorno esperado (R$)", retornoInput))
        content.addView(Ui.inputBox(this, "Prazo em meses", prazoInput))
        content.addView(Ui.inputBox(this, "Progresso atual 0-100", progressoInput))
        content.addView(Ui.sectionTitle(this, "Status"))
        content.addView(statusSpinner)
        content.addView(Ui.primaryButton(this, "Salvar projeto") { salvarProjeto() })
        content.addView(Ui.secondaryButton(this, "Cancelar") { finish() })

        root.addView(Ui.wrapInScroll(this, content))
        setContentView(root)
    }

    private fun salvarProjeto() {
        if (approvedIdeas.isEmpty()) {
            toast("Aprove pelo menos uma ideia antes de criar projeto")
            return
        }

        val selectedIdea = approvedIdeas[ideaSpinner.selectedItemPosition]
        val nome = nomeInput.text.toString().trim()
        val descricao = descricaoInput.text.toString().trim()
        val investimento = investimentoInput.text.toString().replace(",", ".").toDoubleOrNull() ?: 0.0
        val retorno = retornoInput.text.toString().replace(",", ".").toDoubleOrNull() ?: 0.0
        val prazo = prazoInput.text.toString().toIntOrNull() ?: 0
        val progresso = progressoInput.text.toString().toIntOrNull() ?: 0
        val status = when (statusSpinner.selectedItem.toString()) {
            "Em execução" -> Constants.PROJECT_EXECUTION
            "Concluído" -> Constants.PROJECT_DONE
            else -> Constants.PROJECT_PLANNING
        }

        if (nome.isBlank() || descricao.isBlank()) {
            toast("Preencha nome e descrição")
            return
        }

        val project = Project(
            nome = nome,
            descricao = descricao,
            ideiaId = selectedIdea.id,
            categoria = selectedIdea.categoria,
            status = status,
            investimento = investimento,
            retornoEsperado = retorno,
            prazoMeses = prazo,
            progresso = progresso.coerceIn(0, 100),
            gestorId = authViewModel.getCurrentUserId().orEmpty()
        )

        projectViewModel.salvarProjeto(project) {
            if (it) {
                toast("Projeto salvo com sucesso")
                finish()
            } else {
                toast("Erro ao salvar projeto")
            }
        }
    }
}
