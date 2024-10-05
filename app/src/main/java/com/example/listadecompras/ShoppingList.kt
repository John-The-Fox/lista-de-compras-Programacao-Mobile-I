package com.example.listadecompras

data class ShoppingList(
    val id: Int,
    var title: String,
    val items: MutableList<Item> = mutableListOf(),
    var imageUri: String = ""
)