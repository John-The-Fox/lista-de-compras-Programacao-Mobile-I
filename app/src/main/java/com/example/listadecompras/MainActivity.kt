package com.example.listadecompras

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listadecompras.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa os componentes da interface usando o ViewBinding
        val emailField = binding.etEmail
        val passwordField = binding.etPassword
        val loginButton = binding.btnLogin
        val registerButton = binding.btnRegister

        // credenciais válidas para teste
        val validEmail = "adm"
        val validPassword = "adm"

        // Botão de login
        loginButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else if (email == validEmail && password == validPassword) {
                Toast.makeText(this, "Bem vindo adm", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ShoppingListActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val user = UserManager.authenticate(email, password)
                if (user != null) {
                    // Armazena o usuário logado em uma variável global ou no SharedPreferences
                    val intent = Intent(this, ShoppingListActivity::class.java)// Navegar para a tela de listas
                    intent.putExtra("USER_EMAIL", user.email)
                    startActivity(intent)
                    finish() // Finaliza a MainActivity para não voltar ao login com o botão "voltar"
                } else {
                    Toast.makeText(this, "Email ou senha inválidos", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Botão de cadastro (navegar para tela de cadastro)
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}