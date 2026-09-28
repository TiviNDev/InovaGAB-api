package com.fiap.inovagab

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fiap.inovagab.network.ApiClient
import com.fiap.inovagab.repository.UserRepository
import com.fiap.inovagab.ui.auth.LoginActivity
import com.fiap.inovagab.ui.leader.LeaderHomeActivity
import com.fiap.inovagab.ui.manager.ManagerHomeActivity
import com.fiap.inovagab.ui.operator.OperatorHomeActivity
import com.fiap.inovagab.utils.Constants

class MainActivity : AppCompatActivity() {

    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUser = userRepository.currentUser
        val token = ApiClient.authToken

        if (token.isNullOrBlank() || currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val target = when (currentUser.perfil) {
            Constants.PROFILE_OPERATOR -> OperatorHomeActivity::class.java
            Constants.PROFILE_MANAGER -> ManagerHomeActivity::class.java
            Constants.PROFILE_LEADER -> LeaderHomeActivity::class.java
            else -> LoginActivity::class.java
        }

        startActivity(Intent(this, target))
        finish()
    }
}