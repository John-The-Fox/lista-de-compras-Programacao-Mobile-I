package com.example.listadecompras

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.listadecompras.databinding.ActivityRegisterBinding


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa os componentes da interface
        val nameField = binding.etName
        val emailField = binding.etEmail
        val passwordField = binding.etPassword
        val confirmPasswordField = binding.etConfirmPassword
        val registerButton = binding.btnRegister

        // Botão de registrar
        registerButton.setOnClickListener {
            val name = nameField.text.toString()
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
            } else {
                val isRegistered = UserManager.registerUser(email, password, name)
                if (isRegistered) {
                    Toast.makeText(this, "Usuário registrado com sucesso", Toast.LENGTH_SHORT).show()
                    finish() // Volta para a tela de login
                } else {
                    Toast.makeText(this, "Email já cadastrado", Toast.LENGTH_SHORT).show()
                }
                finish() // Fecha a tela de cadastro e volta para o login
            }
        }
    }
}