package com.fiap.inovagab.ui.leader

import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fiap.inovagab.model.Strategy
import com.fiap.inovagab.utils.Ui
import com.fiap.inovagab.utils.toast
import com.fiap.inovagab.viewmodel.StrategyViewModel

class StrategyFormActivity : AppCompatActivity() {
    private val viewModel: StrategyViewModel by viewModels()
    private lateinit var tituloInput: EditText
    private lateinit var descricaoInput: EditText
    private lateinit var statusSpinner: android.widget.Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = Ui.root(this)
        root.addView(Ui.header(this, "Nova Estratégia", "Defina uma orientação para inovação"))

        val content = Ui.scrollContent(this)

        tituloInput = Ui.input(this, "Ex: Redução de emissão de CO₂")
        descricaoInput = Ui.input(this, "Descreva a estratégia em detalhes", multiline = true)
        statusSpinner = Ui.spinner(this, listOf("Ativa", "Inativa"))

        content.addView(Ui.inputBox(this, "Título da estratégia", tituloInput))
        content.addView(Ui.inputBox(this, "Descrição detalhada", descricaoInput))
        content.addView(Ui.sectionTitle(this, "Status"))
        content.addView(statusSpinner)
        content.addView(Ui.primaryButton(this, "Salvar estratégia") { salvar() })
        content.addView(Ui.secondaryButton(this, "Cancelar") { finish() })

        root.addView(Ui.wrapInScroll(this, content))
        setContentView(root)
    }

    private fun salvar() {
        val titulo = tituloInput.text.toString().trim()
        val descricao = descricaoInput.text.toString().trim()
        val ativo = statusSpinner.selectedItem.toString() == "Ativa"

        if (titulo.isBlank() || descricao.isBlank()) {
            toast("Preencha título e descrição")
            return
        }

        val strategy = Strategy(
            titulo = titulo,
            descricao = descricao,
            ativo = ativo
        )

        viewModel.salvarEstrategia(strategy) {
            if (it) {
                toast("Estratégia salva")
                finish()
            } else {
                toast("Erro ao salvar estratégia")
            }
        }
    }
}
