package com.fiap.inovagab.ui.operator

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fiap.inovagab.model.Idea
import com.fiap.inovagab.ui.ProfileActivity
import com.fiap.inovagab.utils.Ui
import com.fiap.inovagab.utils.toBrazilianDate
import com.fiap.inovagab.viewmodel.AuthViewModel
import com.fiap.inovagab.viewmodel.IdeaViewModel

class MyIdeasActivity : AppCompatActivity() {
    private val ideaViewModel: IdeaViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var content: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = Ui.root(this)
        root.addView(Ui.header(this, "Minhas Ideias", "Acompanhe o status das suas sugestões"))

        content = Ui.scrollContent(this)
        root.addView(Ui.wrapInScroll(this, content))
        root.addView(Ui.bottomNav(this, listOf(
            Ui.NavItem("Home") { startActivity(Intent(this, OperatorHomeActivity::class.java)) },
            Ui.NavItem("Ideias", true) {},
            Ui.NavItem("Perfil") { startActivity(Intent(this, ProfileActivity::class.java)) }
        )))
        setContentView(root)

        ideaViewModel.ideas.observe(this) { ideias -> renderIdeas(ideias) }
        authViewModel.getCurrentUserId()?.let { ideaViewModel.carregarMinhasIdeias(it) }
    }

    private fun renderIdeas(ideias: List<Idea>) {
        content.removeAllViews()

        val chips = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        chips.addView(Ui.chip(this, "Todas", true))
        chips.addView(Ui.chip(this, "Pendentes", false))
        chips.addView(Ui.chip(this, "Aprovadas", false))
        chips.addView(Ui.chip(this, "Rejeitadas", false))
        content.addView(chips)

        if (ideias.isEmpty()) {
            content.addView(Ui.actionCard(this, "Você ainda não cadastrou ideias", "Use a tela de Nova Ideia para enviar sua primeira sugestão."))
            return
        }

        ideias.forEach { idea ->
            val card = Ui.card(this)
            card.addView(Ui.statusChip(this, idea.status))
            card.addView(Ui.text(this, idea.titulo, 17, Ui.TEXT_PRIMARY, true))
            card.addView(Ui.text(this, idea.descricao, 14, Ui.TEXT_SECONDARY))
            card.addView(Ui.text(this, "Categoria: ${idea.categoria}", 12, Ui.TEXT_SECONDARY))
            card.addView(Ui.text(this, "Criada em: ${idea.dataCriacao.toBrazilianDate()}", 12, Ui.TEXT_SECONDARY))
            if (idea.comentarioGestor.isNotBlank()) {
                card.addView(Ui.text(this, "Comentário: ${idea.comentarioGestor}", 12, Ui.TEXT_PRIMARY, true))
            }
            content.addView(card)
        }
    }
}
