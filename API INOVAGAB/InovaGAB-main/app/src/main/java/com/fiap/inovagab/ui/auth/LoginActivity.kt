package com.fiap.inovagab.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
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

class LoginActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = Ui.root(this)
        val content = Ui.scrollContent(this).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(Ui.dp(this@LoginActivity, 24), Ui.dp(this@LoginActivity, 80), Ui.dp(this@LoginActivity, 24), Ui.dp(this@LoginActivity, 24))
        }

        content.addView(Ui.text(this, "InovaGAB", 34, Ui.PRIMARY, true).apply { gravity = Gravity.CENTER })
        content.addView(Ui.text(this, "Plataforma de Inovação Corporativa", 16, Ui.TEXT_SECONDARY, true).apply {
            gravity = Gravity.CENTER
            setPadding(0, Ui.dp(this@LoginActivity, 8), 0, Ui.dp(this@LoginActivity, 32))
        })

        emailInput = Ui.input(this, "Digite seu email")
        passwordInput = Ui.input(this, "Digite sua senha", password = true)

        content.addView(Ui.inputBox(this, "Email", emailInput))
        content.addView(Ui.inputBox(this, "Senha", passwordInput))
        content.addView(Ui.primaryButton(this, "Entrar") { fazerLogin() })
        content.addView(Ui.text(this, "Criar nova conta", 15, Ui.SECONDARY, true).apply {
            gravity = Gravity.CENTER
            setPadding(0, Ui.dp(this@LoginActivity, 16), 0, 0)
            setOnClickListener { startActivity(Intent(this@LoginActivity, RegisterActivity::class.java)) }
        })

        root.addView(Ui.wrapInScroll(this, content))
        setContentView(root)
    }

    private fun fazerLogin() {
        val email = emailInput.text.toString().trim()
        val senha = passwordInput.text.toString().trim()

        if (email.isBlank() || senha.isBlank()) {
            toast("Preencha email e senha")
            return
        }

        viewModel.login(email, senha) { sucesso, perfil ->
            if (sucesso && perfil != null) {
                redirecionarPorPerfil(perfil)
            } else {
                toast("Email ou senha incorretos")
            }
        }
    }

    private fun redirecionarPorPerfil(perfil: String) {
        val target = when (perfil) {
            Constants.PROFILE_OPERATOR -> OperatorHomeActivity::class.java
            Constants.PROFILE_MANAGER -> ManagerHomeActivity::class.java
            Constants.PROFILE_LEADER -> LeaderHomeActivity::class.java
            else -> LoginActivity::class.java
        }

        startActivity(Intent(this, target))
        finish()
    }
}
