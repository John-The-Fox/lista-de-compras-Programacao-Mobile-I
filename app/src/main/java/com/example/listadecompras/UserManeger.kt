package com.example.listadecompras

object UserManager {
    private val users: MutableList<User> = mutableListOf()

    // Adiciona um novo usuário se o email não estiver em uso
    fun registerUser(email: String, password: String, name: String): Boolean {
        if (users.any { it.email == email }) {
            return false // Email já cadastrado
        }
        users.add(User(email, password, name))
        return true
    }

    // Autentica um usuário baseado em email e senha
    fun authenticate(email: String, password: String): User? {
        return users.find { it.email == email && it.password == password }
    }
}
