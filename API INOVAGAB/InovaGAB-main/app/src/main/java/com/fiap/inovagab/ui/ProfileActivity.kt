package com.fiap.inovagab.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fiap.inovagab.repository.UserRepository
import com.fiap.inovagab.ui.auth.LoginActivity
import com.fiap.inovagab.utils.Ui
import com.fiap.inovagab.utils.toast
import com.fiap.inovagab.viewmodel.AuthViewModel

class ProfileActivity : AppCompatActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = Ui.root(this)
        root.addView(Ui.header(this, "Perfil", "Dados do usuário logado"))

        val content = Ui.scrollContent(this)

        val uid = authViewModel.getCurrentUserId()
        if (uid == null) {
            content.addView(Ui.actionCard(this, "Usuário não encontrado", "Faça login novamente."))
        } else {
            userRepository.buscarUsuario(uid) { user ->
                content.removeAllViews()
                if (user == null) {
                    content.addView(Ui.actionCard(this, "Usuário não encontrado", "Documento do usuário não existe no Firestore."))
                } else {
                    val card = Ui.card(this)
                    card.addView(Ui.text(this, user.nome, 20, Ui.TEXT_PRIMARY, true))
                    card.addView(Ui.text(this, user.email, 14, Ui.TEXT_SECONDARY))
                    card.addView(Ui.statusChip(this, user.perfil))
                    content.addView(card)
                }

                content.addView(Ui.secondaryButton(this, "Voltar") { finish() })
                content.addView(Ui.dangerButton(this, "Sair da conta") {
                    authViewModel.logout()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                })
            }
        }

        root.addView(Ui.wrapInScroll(this, content))
        setContentView(root)
    }
}
