package com.fiap.inovagab.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fiap.inovagab.ui.leader.LeaderHomeActivity
import com.fiap.inovagab.ui.manager.ManagerHomeActivity
import com.fiap.inovagab.ui.operator.OperatorHomeActivity
import com.fiap.inovagab.utils.Constants
import com.fiap.inovagab.utils.Ui
import com.fiap.inovagab.utils.toast
import com.fiap.inovagab.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var nomeInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var senhaInput: EditText
    private lateinit var perfilSpinner: android.widget.Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = Ui.root(this)
        root.addView(Ui.header(this, "Criar conta", "Selecione seu perfil de acesso"))

        val content = Ui.scrollContent(this)

        nomeInput = Ui.input(this, "Digite seu nome")
        emailInput = Ui.input(this, "Digite seu email")
        senhaInput = Ui.input(this, "Digite sua senha", password = true)
        perfilSpinner = Ui.spinner(this, listOf("Operador", "Gestor", "Líder"))

        content.addView(Ui.inputBox(this, "Nome completo", nomeInput))
        content.addView(Ui.inputBox(this, "Email", emailInput))
        content.addView(Ui.inputBox(this, "Senha", senhaInput))
        content.addView(Ui.sectionTitle(this, "Perfil"))
        content.addView(perfilSpinner)
        content.addView(Ui.primaryButton(this, "Criar conta") { cadastrar() })
        content.addView(Ui.secondaryButton(this, "Já tenho conta") { finish() })

        root.addView(Ui.wrapInScroll(this, content))
        setContentView(root)
    }

    private fun cadastrar() {
        val nome = nomeInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val senha = senhaInput.text.toString().trim()
        val perfil = when (perfilSpinner.selectedItem.toString()) {
            "Gestor" -> Constants.PROFILE_MANAGER
            "Líder" -> Constants.PROFILE_LEADER
            else -> Constants.PROFILE_OPERATOR
        }

        if (nome.isBlank() || email.isBlank() || senha.length < 6) {
            toast("Preencha os dados. A senha deve ter pelo menos 6 caracteres.")
            return
        }

        viewModel.cadastrar(nome, email, senha, perfil) { sucesso, perfilCriado ->
            if (sucesso && perfilCriado != null) {
                val target = when (perfilCriado) {
                    Constants.PROFILE_MANAGER -> ManagerHomeActivity::class.java
                    Constants.PROFILE_LEADER -> LeaderHomeActivity::class.java
                    else -> OperatorHomeActivity::class.java
                }
                startActivity(Intent(this, target))
                finish()
            } else {
                toast("Erro ao criar conta")
            }
        }
    }
}
