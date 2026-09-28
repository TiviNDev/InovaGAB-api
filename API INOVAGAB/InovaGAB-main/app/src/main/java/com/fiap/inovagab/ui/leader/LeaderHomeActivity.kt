package com.fiap.inovagab.ui.leader

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fiap.inovagab.ui.ProfileActivity
import com.fiap.inovagab.ui.auth.LoginActivity
import com.fiap.inovagab.ui.manager.ProjectsActivity
import com.fiap.inovagab.utils.Constants
import com.fiap.inovagab.utils.Ui
import com.fiap.inovagab.utils.toBRL
import com.fiap.inovagab.viewmodel.AuthViewModel
import com.fiap.inovagab.viewmodel.IdeaViewModel
import com.fiap.inovagab.viewmodel.ProjectViewModel

class LeaderHomeActivity : AppCompatActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val ideaViewModel: IdeaViewModel by viewModels()
    private val projectViewModel: ProjectViewModel by viewModels()

    private var totalIdeas = 0
    private var approvedIdeas = 0
    private var activeProjects = 0
    private var totalRoi = 0.0
    private var isRendering = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        render()

        ideaViewModel.ideas.observe(this) {
            totalIdeas = it.size
            approvedIdeas = it.count { idea -> idea.status == Constants.STATUS_APPROVED }
            render()
        }

        projectViewModel.projects.observe(this) {
            activeProjects = it.count { p -> p.status != Constants.PROJECT_DONE }
            totalRoi = it.sumOf { p -> p.retornoEsperado - p.investimento }
            render()
        }

        ideaViewModel.carregarTodasIdeias()
        projectViewModel.carregarProjetos()
    }

    private fun render() {
        if (isRendering) return
        isRendering = true

        val root = Ui.root(this)
        root.addView(Ui.header(this, "InovaGAB", "Líder • Estratégia, inovação e resultado"))

        val content = Ui.scrollContent(this)
        content.addView(Ui.sectionTitle(this, "KPIs principais"))
        content.addView(Ui.statCard(this, totalIdeas.toString(), "Total de ideias cadastradas", "💡"))
        content.addView(Ui.statCard(this, approvedIdeas.toString(), "Ideias aprovadas", "✅"))
        content.addView(Ui.statCard(this, activeProjects.toString(), "Projetos ativos", "🚀"))
        content.addView(Ui.statCard(this, totalRoi.toBRL(), "ROI total estimado", "📈"))

        content.addView(Ui.sectionTitle(this, "Ações estratégicas"))
        content.addView(Ui.actionCard(this, "Ver Dashboard completo", "Indicadores, gráficos e projetos com maior retorno.", onClick = {
            startActivity(Intent(this, DashboardActivity::class.java))
        }))
        content.addView(Ui.actionCard(this, "Gerenciar estratégias", "Crie, edite, ative ou desative orientações estratégicas.", onClick = {
            startActivity(Intent(this, ManageStrategiesActivity::class.java))
        }))
        content.addView(Ui.actionCard(this, "Ver todos os projetos", "Acompanhe execução, progresso e ROI.", onClick = {
            startActivity(Intent(this, ProjectsActivity::class.java))
        }))
        content.addView(Ui.secondaryButton(this, "Sair") {
            authViewModel.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        })

        root.addView(Ui.wrapInScroll(this, content))
        root.addView(nav("Home"))
        setContentView(root)
        isRendering = false
    }

    private fun nav(active: String) = Ui.bottomNav(this, listOf(
        Ui.NavItem("Home", active == "Home") {},
        Ui.NavItem("Dashboard", active == "Dashboard") { startActivity(Intent(this, DashboardActivity::class.java)) },
        Ui.NavItem("Estratégias", active == "Estratégias") { startActivity(Intent(this, ManageStrategiesActivity::class.java)) },
        Ui.NavItem("Perfil", active == "Perfil") { startActivity(Intent(this, ProfileActivity::class.java)) }
    ))
}
