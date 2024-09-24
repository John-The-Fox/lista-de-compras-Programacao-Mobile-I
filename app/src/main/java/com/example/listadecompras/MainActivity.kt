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

        // Simular credenciais válidas
        val validEmail = "usuario@email.com"
        val validPassword = "123456"

        // Botão de login
        loginButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else if (email != validEmail || password != validPassword) {
                Toast.makeText(this, "Email ou senha incorretos", Toast.LENGTH_SHORT).show()
            } else {
                // Simulação de login bem-sucedido
                Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()

                // Navegar para a tela principal (exemplo de navegação)
                val intent = Intent(this, ShoppingListActivity::class.java)
                startActivity(intent)
                finish() // Finaliza a MainActivity para não voltar ao login com o botão "voltar"
            }
        }

        // Botão de cadastro (navegar para tela de cadastro)
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}