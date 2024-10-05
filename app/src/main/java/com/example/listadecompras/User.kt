package com.example.listadecompras

data class User(
    val email: String,
    val password: String,
    val name: String,
    val shoppingLists: MutableList<ShoppingList> = mutableListOf()
)