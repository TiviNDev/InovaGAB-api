package com.fiap.inovagab.ui.leader

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fiap.inovagab.model.Strategy
import com.fiap.inovagab.ui.ProfileActivity
import com.fiap.inovagab.utils.Ui
import com.fiap.inovagab.utils.toBrazilianDate
import com.fiap.inovagab.utils.toast
import com.fiap.inovagab.viewmodel.StrategyViewModel

class ManageStrategiesActivity : AppCompatActivity() {
    private val viewModel: StrategyViewModel by viewModels()
    private lateinit var content: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = Ui.root(this)
        root.addView(Ui.header(this, "Estratégias", "Gerencie orientações estratégicas"))
        content = Ui.scrollContent(this)

        root.addView(Ui.wrapInScroll(this, content))
        root.addView(nav("Estratégias"))
        setContentView(root)

        viewModel.strategies.observe(this) { renderStrategies(it) }
        viewModel.carregarEstrategias()
    }

    private fun renderStrategies(strategies: List<Strategy>) {
        content.removeAllViews()

        val chips = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        chips.addView(Ui.chip(this, "Ativas", true))
        chips.addView(Ui.chip(this, "Inativas", false))
        content.addView(chips)

        content.addView(Ui.primaryButton(this, "+ Criar nova estratégia") {
            startActivity(Intent(this, StrategyFormActivity::class.java))
        })

        if (strategies.isEmpty()) {
            content.addView(Ui.actionCard(this, "Nenhuma estratégia cadastrada", "Crie a primeira estratégia para orientar as ideias."))
            return
        }

        strategies.forEach { strategy ->
            val card = Ui.card(this)
            card.addView(Ui.statusChip(this, if (strategy.ativo) "Ativa" else "Inativa"))
            card.addView(Ui.text(this, strategy.titulo, 17, Ui.TEXT_PRIMARY, true))
            card.addView(Ui.text(this, strategy.descricao, 14, Ui.TEXT_SECONDARY))
            card.addView(Ui.text(this, "Criada em: ${strategy.dataCriacao.toBrazilianDate()}", 12, Ui.TEXT_SECONDARY))
            card.addView(Ui.secondaryButton(this, if (strategy.ativo) "Desativar" else "Ativar") {
                viewModel.alterarStatus(strategy.id, !strategy.ativo) {
                    toast(if (it) "Status alterado" else "Erro ao alterar status")
                }
            })
            card.addView(Ui.dangerButton(this, "Excluir") {
                viewModel.excluir(strategy.id) {
                    toast(if (it) "Estratégia excluída" else "Erro ao excluir")
                }
            })
            content.addView(card)
        }
    }

    private fun nav(active: String) = Ui.bottomNav(this, listOf(
        Ui.NavItem("Home", active == "Home") { startActivity(Intent(this, LeaderHomeActivity::class.java)) },
        Ui.NavItem("Dashboard", active == "Dashboard") { startActivity(Intent(this, DashboardActivity::class.java)) },
        Ui.NavItem("Estratégias", active == "Estratégias") {},
        Ui.NavItem("Perfil", active == "Perfil") { startActivity(Intent(this, ProfileActivity::class.java)) }
    ))
}
