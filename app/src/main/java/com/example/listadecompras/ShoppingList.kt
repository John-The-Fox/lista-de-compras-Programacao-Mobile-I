package com.example.listadecompras

data class ShoppingList(
    val id: Int,
    var title: String,
    val items: MutableList<ShoppingItem> = mutableListOf(),
    var imageUri: String = ""
)

data class ShoppingItem(
    val id: Int,
    val name: String,
    val quantity: Int,
    val unit: String,
    val category: String,
    var isChecked: Boolean = false
)