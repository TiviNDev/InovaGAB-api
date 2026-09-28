package com.fiap.inovagab.ui.operator

import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fiap.inovagab.model.Idea
import com.fiap.inovagab.repository.UserRepository
import com.fiap.inovagab.utils.Ui
import com.fiap.inovagab.utils.toast
import com.fiap.inovagab.viewmodel.AuthViewModel
import com.fiap.inovagab.viewmodel.IdeaViewModel

class CreateIdeaActivity : AppCompatActivity() {
    private val ideaViewModel: IdeaViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private val userRepository = UserRepository()

    private lateinit var tituloInput: EditText
    private lateinit var descricaoInput: EditText
    private lateinit var categoriaSpinner: android.widget.Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = Ui.root(this)
        root.addView(Ui.header(this, "Nova Ideia", "Registre uma melhoria operacional"))

        val content = Ui.scrollContent(this)

        tituloInput = Ui.input(this, "Ex: Reduzir tempo de abastecimento")
        descricaoInput = Ui.input(this, "Descreva o problema, oportunidade e impacto esperado", multiline = true)
        categoriaSpinner = Ui.spinner(this, listOf("Operação", "Manutenção", "Segurança", "Outros"))

        content.addView(Ui.inputBox(this, "Título da ideia", tituloInput))
        content.addView(Ui.inputBox(this, "Descrição detalhada", descricaoInput))
        content.addView(Ui.sectionTitle(this, "Categoria"))
        content.addView(categoriaSpinner)
        content.addView(Ui.secondaryButton(this, "Anexar foto/documento (opcional)") {
            toast("Anexos são opcionais nesta entrega")
        })
        content.addView(Ui.primaryButton(this, "Enviar ideia") { enviarIdeia() })
        content.addView(Ui.secondaryButton(this, "Cancelar") { finish() })

        root.addView(Ui.wrapInScroll(this, content))
        setContentView(root)
    }

    private fun enviarIdeia() {
        val uid = authViewModel.getCurrentUserId()
        val titulo = tituloInput.text.toString().trim()
        val descricao = descricaoInput.text.toString().trim()
        val categoria = categoriaSpinner.selectedItem.toString()

        if (uid == null || titulo.isBlank() || descricao.isBlank()) {
            toast("Preencha título e descrição")
            return
        }

        userRepository.buscarUsuario(uid) { user ->
            val idea = Idea(
                titulo = titulo,
                descricao = descricao,
                categoria = categoria,
                autorId = uid,
                autorNome = user?.nome ?: "Usuário"
            )

            ideaViewModel.cadastrarIdeia(idea) { success ->
                if (success) {
                    toast("Ideia enviada com sucesso")
                    finish()
                } else {
                    toast("Erro ao enviar ideia")
                }
            }
        }
    }
}
