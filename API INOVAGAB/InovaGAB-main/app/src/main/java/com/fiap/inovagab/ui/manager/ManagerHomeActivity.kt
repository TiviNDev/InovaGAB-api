package com.fiap.inovagab.ui.manager

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fiap.inovagab.ui.ProfileActivity
import com.fiap.inovagab.ui.auth.LoginActivity
import com.fiap.inovagab.utils.Ui
import com.fiap.inovagab.viewmodel.AuthViewModel
import com.fiap.inovagab.viewmodel.IdeaViewModel
import com.fiap.inovagab.viewmodel.ProjectViewModel

class ManagerHomeActivity : AppCompatActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val ideaViewModel: IdeaViewModel by viewModels()
    private val projectViewModel: ProjectViewModel by viewModels()

    private var pendingCount = 0
    private var activeProjects = 0
    private var isRendering = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        render()

        ideaViewModel.ideas.observe(this) {
            pendingCount = it.size
            render()
        }

        projectViewModel.projects.observe(this) {
            activeProjects = it.count { p -> p.status != "concluido" }
            render()
        }

        ideaViewModel.carregarIdeiasPendentes()
        projectViewModel.carregarProjetos()
    }

    private fun render() {
        if (isRendering) return
        isRendering = true

        val root = Ui.root(this)
        root.addView(Ui.header(this, "InovaGAB", "Gestor • Avalie ideias e estruture projetos"))

        val content = Ui.scrollContent(this)
        content.addView(Ui.sectionTitle(this, "Indicadores"))

        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        row.addView(Ui.statCard(this, pendingCount.toString(), "Ideias pendentes", "📥").apply {
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                rightMargin = Ui.dp(this@ManagerHomeActivity, 6)
            }
        })
        row.addView(Ui.statCard(this, activeProjects.toString(), "Projetos ativos", "🚀").apply {
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                leftMargin = Ui.dp(this@ManagerHomeActivity, 6)
            }
        })
        content.addView(row)

        content.addView(Ui.sectionTitle(this, "Ações rápidas"))
        content.addView(Ui.actionCard(this, "Avaliar ideias pendentes", "Aprove ou rejeite sugestões enviadas pelos operadores.", pendingCount.toString()) {
            startActivity(Intent(this, ReviewIdeasActivity::class.java))
        })
        content.addView(Ui.actionCard(this, "Gerenciar projetos", "Acompanhe progresso, investimento e ROI.", onClick = {
            startActivity(Intent(this, ProjectsActivity::class.java))
        }))
        content.addView(Ui.actionCard(this, "Criar novo projeto", "Estruture um projeto a partir de uma ideia aprovada.", onClick = {
            startActivity(Intent(this, ProjectFormActivity::class.java))
        }))
        content.addView(Ui.secondaryButton(this, "Sair") {
            authViewModel.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        })

        root.addView(Ui.wrapInScroll(this, content))
        root.addView(Ui.bottomNav(this, listOf(
            Ui.NavItem("Home", true) {},
            Ui.NavItem("Ideias") { startActivity(Intent(this, ReviewIdeasActivity::class.java)) },
            Ui.NavItem("Projetos") { startActivity(Intent(this, ProjectsActivity::class.java)) },
            Ui.NavItem("Perfil") { startActivity(Intent(this, ProfileActivity::class.java)) }
        )))
        setContentView(root)
        isRendering = false
    }
}
