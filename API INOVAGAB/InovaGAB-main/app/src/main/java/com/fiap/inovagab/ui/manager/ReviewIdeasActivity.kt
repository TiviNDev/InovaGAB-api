package com.fiap.inovagab.ui.manager

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fiap.inovagab.model.Idea
import com.fiap.inovagab.ui.ProfileActivity
import com.fiap.inovagab.utils.Ui
import com.fiap.inovagab.utils.toast
import com.fiap.inovagab.viewmodel.IdeaViewModel

class ReviewIdeasActivity : AppCompatActivity() {
    private val viewModel: IdeaViewModel by viewModels()
    private lateinit var content: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = Ui.root(this)
        root.addView(Ui.header(this, "Ideias Pendentes", "Avalie sugestões enviadas pelos operadores"))
        content = Ui.scrollContent(this)

        root.addView(Ui.wrapInScroll(this, content))
        root.addView(nav("Ideias"))
        setContentView(root)

        viewModel.ideas.observe(this) { renderIdeas(it) }
        viewModel.carregarIdeiasPendentes()
    }

    private fun renderIdeas(ideias: List<Idea>) {
        content.removeAllViews()

        val chips = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        chips.addView(Ui.chip(this, "Todas", true))
        chips.addView(Ui.chip(this, "Operação", false))
        chips.addView(Ui.chip(this, "Segurança", false))
        content.addView(chips)

        if (ideias.isEmpty()) {
            content.addView(Ui.actionCard(this, "Nenhuma ideia pendente", "Quando operadores enviarem ideias, elas aparecerão aqui."))
            return
        }

        ideias.forEach { idea ->
            val card = Ui.card(this)
            card.addView(Ui.statusChip(this, idea.status))
            card.addView(Ui.text(this, idea.titulo, 17, Ui.TEXT_PRIMARY, true))
            card.addView(Ui.text(this, "Autor: ${idea.autorNome}", 13, Ui.TEXT_SECONDARY))
            card.addView(Ui.text(this, "Categoria: ${idea.categoria}", 13, Ui.TEXT_SECONDARY))
            card.addView(Ui.text(this, idea.descricao, 14, Ui.TEXT_PRIMARY))
            card.addView(Ui.primaryButton(this, "Aprovar") {
                viewModel.aprovarIdeia(idea.id, "Ideia aprovada pelo gestor") {
                    toast(if (it) "Ideia aprovada" else "Erro ao aprovar")
                }
            })
            card.addView(Ui.dangerButton(this, "Rejeitar") {
                viewModel.rejeitarIdeia(idea.id, "Ideia rejeitada pelo gestor") {
                    toast(if (it) "Ideia rejeitada" else "Erro ao rejeitar")
                }
            })
            content.addView(card)
        }
    }

    private fun nav(active: String) = Ui.bottomNav(this, listOf(
        Ui.NavItem("Home", active == "Home") { startActivity(Intent(this, ManagerHomeActivity::class.java)) },
        Ui.NavItem("Ideias", active == "Ideias") {},
        Ui.NavItem("Projetos", active == "Projetos") { startActivity(Intent(this, ProjectsActivity::class.java)) },
        Ui.NavItem("Perfil", active == "Perfil") { startActivity(Intent(this, ProfileActivity::class.java)) }
    ))
}
