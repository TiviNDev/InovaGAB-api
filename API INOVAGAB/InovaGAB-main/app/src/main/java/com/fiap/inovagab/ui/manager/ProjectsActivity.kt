package com.fiap.inovagab.ui.manager

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fiap.inovagab.model.Project
import com.fiap.inovagab.ui.ProfileActivity
import com.fiap.inovagab.utils.Ui
import com.fiap.inovagab.utils.toBRL
import com.fiap.inovagab.viewmodel.ProjectViewModel

class ProjectsActivity : AppCompatActivity() {
    private val viewModel: ProjectViewModel by viewModels()
    private lateinit var content: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = Ui.root(this)
        root.addView(Ui.header(this, "Projetos", "Acompanhe execução, investimento e ROI"))
        content = Ui.scrollContent(this)
        root.addView(Ui.wrapInScroll(this, content))
        root.addView(Ui.bottomNav(this, listOf(
            Ui.NavItem("Home") { startActivity(Intent(this, ManagerHomeActivity::class.java)) },
            Ui.NavItem("Ideias") { startActivity(Intent(this, ReviewIdeasActivity::class.java)) },
            Ui.NavItem("Projetos", true) {},
            Ui.NavItem("Perfil") { startActivity(Intent(this, ProfileActivity::class.java)) }
        )))
        setContentView(root)

        viewModel.projects.observe(this) { renderProjects(it) }
        viewModel.carregarProjetos()
    }

    private fun renderProjects(projects: List<Project>) {
        content.removeAllViews()

        val chips = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        chips.addView(Ui.chip(this, "Todos", true))
        chips.addView(Ui.chip(this, "Planejamento", false))
        chips.addView(Ui.chip(this, "Em execução", false))
        chips.addView(Ui.chip(this, "Concluídos", false))
        content.addView(chips)

        content.addView(Ui.primaryButton(this, "+ Criar novo projeto") {
            startActivity(Intent(this, ProjectFormActivity::class.java))
        })

        if (projects.isEmpty()) {
            content.addView(Ui.actionCard(this, "Nenhum projeto cadastrado", "Crie um projeto a partir de uma ideia aprovada."))
            return
        }

        projects.forEach { project ->
            val card = Ui.card(this)
            card.addView(Ui.statusChip(this, project.status))
            card.addView(Ui.text(this, project.nome, 17, Ui.TEXT_PRIMARY, true))
            card.addView(Ui.text(this, project.descricao, 14, Ui.TEXT_SECONDARY))
            card.addView(Ui.progress(this, project.progresso))
            card.addView(Ui.text(this, "Progresso: ${project.progresso}% • Prazo: ${project.prazoMeses} meses", 13, Ui.TEXT_SECONDARY))
            card.addView(Ui.text(this, "Investimento: ${project.investimento.toBRL()}", 13, Ui.TEXT_SECONDARY))
            card.addView(Ui.text(this, "Retorno esperado: ${project.retornoEsperado.toBRL()}", 13, Ui.SUCCESS, true))
            card.addView(Ui.text(this, "ROI: ${"%.1f".format(project.roiPercentual)}%", 13, Ui.PRIMARY, true))
            content.addView(card)
        }
    }
}
