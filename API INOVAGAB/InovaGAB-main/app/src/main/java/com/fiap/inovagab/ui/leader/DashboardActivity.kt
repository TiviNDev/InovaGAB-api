package com.fiap.inovagab.ui.leader

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fiap.inovagab.model.Idea
import com.fiap.inovagab.model.Project
import com.fiap.inovagab.ui.ProfileActivity
import com.fiap.inovagab.utils.Constants
import com.fiap.inovagab.utils.Ui
import com.fiap.inovagab.utils.toBRL
import com.fiap.inovagab.viewmodel.IdeaViewModel
import com.fiap.inovagab.viewmodel.ProjectViewModel

class DashboardActivity : AppCompatActivity() {
    private val ideaViewModel: IdeaViewModel by viewModels()
    private val projectViewModel: ProjectViewModel by viewModels()

    private var ideas: List<Idea> = emptyList()
    private var projects: List<Project> = emptyList()
    private lateinit var content: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = Ui.root(this)
        root.addView(Ui.header(this, "Dashboard", "Visão executiva da inovação"))
        content = Ui.scrollContent(this)

        root.addView(Ui.wrapInScroll(this, content))
        root.addView(nav("Dashboard"))
        setContentView(root)

        ideaViewModel.ideas.observe(this) {
            ideas = it
            render()
        }
        projectViewModel.projects.observe(this) {
            projects = it
            render()
        }

        ideaViewModel.carregarTodasIdeias()
        projectViewModel.carregarProjetos()
    }

    private fun render() {
        content.removeAllViews()

        val approved = ideas.count { it.status == Constants.STATUS_APPROVED }
        val pending = ideas.count { it.status == Constants.STATUS_PENDING }
        val rejected = ideas.count { it.status == Constants.STATUS_REJECTED }
        val activeProjects = projects.count { it.status != Constants.PROJECT_DONE }
        val roi = projects.sumOf { it.retornoEsperado - it.investimento }
        val approvalRate = if (ideas.isNotEmpty()) (approved * 100 / ideas.size) else 0

        content.addView(Ui.sectionTitle(this, "Período"))
        val chips = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        chips.addView(Ui.chip(this, "Mês", false))
        chips.addView(Ui.chip(this, "Trimestre", true))
        chips.addView(Ui.chip(this, "Ano", false))
        content.addView(chips)

        content.addView(Ui.sectionTitle(this, "KPIs principais"))
        content.addView(Ui.statCard(this, ideas.size.toString(), "Total de ideias cadastradas", "💡"))
        content.addView(Ui.statCard(this, "$approvalRate%", "Taxa de aprovação", "✅"))
        content.addView(Ui.statCard(this, activeProjects.toString(), "Projetos em andamento", "🚀"))
        content.addView(Ui.statCard(this, roi.toBRL(), "ROI acumulado", "📈"))

        content.addView(Ui.sectionTitle(this, "Gráficos"))

        val statusCard = Ui.card(this)
        statusCard.addView(Ui.text(this, "Ideias por status", 17, Ui.TEXT_PRIMARY, true))
        statusCard.addView(Ui.text(this, "🟢 Aprovadas: $approved", 14, Ui.TEXT_SECONDARY))
        statusCard.addView(Ui.text(this, "🟡 Pendentes: $pending", 14, Ui.TEXT_SECONDARY))
        statusCard.addView(Ui.text(this, "🔴 Rejeitadas: $rejected", 14, Ui.TEXT_SECONDARY))
        content.addView(statusCard)

        val categoryCard = Ui.card(this)
        categoryCard.addView(Ui.text(this, "Projetos por categoria", 17, Ui.TEXT_PRIMARY, true))
        if (projects.isEmpty()) {
            categoryCard.addView(Ui.text(this, "Nenhum projeto cadastrado", 14, Ui.TEXT_SECONDARY))
        } else {
            projects.groupBy { it.categoria.ifBlank { "Sem categoria" } }.forEach { (cat, list) ->
                categoryCard.addView(Ui.text(this, "$cat: ${list.size}", 14, Ui.TEXT_SECONDARY))
            }
        }
        content.addView(categoryCard)

        val roiCard = Ui.card(this)
        roiCard.addView(Ui.text(this, "Evolução de ROI", 17, Ui.TEXT_PRIMARY, true))
        roiCard.addView(Ui.text(this, "Jan ▇▇", 14, Ui.TEXT_SECONDARY))
        roiCard.addView(Ui.text(this, "Fev ▇▇▇▇", 14, Ui.TEXT_SECONDARY))
        roiCard.addView(Ui.text(this, "Mar ▇▇▇▇▇▇", 14, Ui.TEXT_SECONDARY))
        roiCard.addView(Ui.text(this, "Atual ${roi.toBRL()}", 14, Ui.SUCCESS, true))
        content.addView(roiCard)

        content.addView(Ui.sectionTitle(this, "Top 5 projetos com maior ROI"))
        if (projects.isEmpty()) {
            content.addView(Ui.actionCard(this, "Nenhum projeto para exibir", "Crie projetos para visualizar o ranking de ROI."))
        } else {
            projects.sortedByDescending { it.roiPercentual }.take(5).forEach {
                val card = Ui.card(this)
                card.addView(Ui.text(this, it.nome, 16, Ui.TEXT_PRIMARY, true))
                card.addView(Ui.text(this, "Investimento: ${it.investimento.toBRL()}", 13, Ui.TEXT_SECONDARY))
                card.addView(Ui.text(this, "Retorno: ${it.retornoEsperado.toBRL()}", 13, Ui.TEXT_SECONDARY))
                card.addView(Ui.text(this, "ROI: ${"%.1f".format(it.roiPercentual)}%", 13, Ui.SUCCESS, true))
                content.addView(card)
            }
        }
    }

    private fun nav(active: String) = Ui.bottomNav(this, listOf(
        Ui.NavItem("Home", active == "Home") { startActivity(Intent(this, LeaderHomeActivity::class.java)) },
        Ui.NavItem("Dashboard", active == "Dashboard") {},
        Ui.NavItem("Estratégias", active == "Estratégias") { startActivity(Intent(this, ManageStrategiesActivity::class.java)) },
        Ui.NavItem("Perfil", active == "Perfil") { startActivity(Intent(this, ProfileActivity::class.java)) }
    ))
}
