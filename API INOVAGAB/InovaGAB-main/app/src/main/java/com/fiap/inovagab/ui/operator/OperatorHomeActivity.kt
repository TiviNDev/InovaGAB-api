package com.fiap.inovagab.ui.operator

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fiap.inovagab.ui.ProfileActivity
import com.fiap.inovagab.ui.auth.LoginActivity
import com.fiap.inovagab.utils.Ui
import com.fiap.inovagab.viewmodel.AuthViewModel

class OperatorHomeActivity : AppCompatActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = Ui.root(this)
        root.addView(Ui.header(this, "InovaGAB", "Operador • Capture ideias do dia a dia"))

        val content = Ui.scrollContent(this)
        content.addView(Ui.sectionTitle(this, "Estratégias da Empresa"))
        content.addView(Ui.actionCard(this, "Eficiência Operacional", "Reduzir desperdícios, otimizar rotas e melhorar produtividade.", "ATIVA"))
        content.addView(Ui.actionCard(this, "Segurança e Qualidade", "Priorizar melhorias que reduzam riscos e aumentem a qualidade.", "ATIVA"))

        content.addView(Ui.sectionTitle(this, "Ações rápidas"))
        content.addView(Ui.actionCard(this, "Cadastrar nova ideia", "Registre uma oportunidade de melhoria.", onClick = {
            startActivity(Intent(this, CreateIdeaActivity::class.java))
        }))
        content.addView(Ui.actionCard(this, "Ver minhas ideias", "Acompanhe ideias pendentes, aprovadas e rejeitadas.", onClick = {
            startActivity(Intent(this, MyIdeasActivity::class.java))
        }))
        content.addView(Ui.secondaryButton(this, "Sair") {
            authViewModel.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        })

        root.addView(Ui.wrapInScroll(this, content))
        root.addView(Ui.bottomNav(this, listOf(
            Ui.NavItem("Home", true) {},
            Ui.NavItem("Ideias") { startActivity(Intent(this, MyIdeasActivity::class.java)) },
            Ui.NavItem("Perfil") { startActivity(Intent(this, ProfileActivity::class.java)) }
        )))
        setContentView(root)
    }
}
