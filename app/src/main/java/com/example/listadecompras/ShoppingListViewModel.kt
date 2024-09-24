package com.example.listadecompras

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class ShoppingListViewModel(private val state: SavedStateHandle) : ViewModel() {

    // Chave para salvar/restaurar dados no SavedStateHandle
    private val SHOPPING_LIST_KEY = "shopping_list_key"

    // Inicializando a lista de compras no SavedStateHandle
    init {
        if (!state.contains(SHOPPING_LIST_KEY)) {
            state[SHOPPING_LIST_KEY] = mutableListOf<ShoppingList>()
        }
    }

    // Função para obter a lista de compras
    fun getShoppingLists(): MutableList<ShoppingList> {
        return state[SHOPPING_LIST_KEY] ?: mutableListOf()
    }

    // Função para adicionar uma nova lista
    fun addShoppingList(shoppingList: ShoppingList) {
        val currentList = getShoppingLists()
        currentList.add(shoppingList)
        state[SHOPPING_LIST_KEY] = currentList
    }

    // Função para editar uma lista
    fun editShoppingList(index: Int, newShoppingList: ShoppingList) {
        val currentList = getShoppingLists()
        currentList[index] = newShoppingList
        state[SHOPPING_LIST_KEY] = currentList
    }

    // Função para remover uma lista
    fun deleteShoppingList(index: Int) {
        val currentList = getShoppingLists()
        currentList.removeAt(index)
        state[SHOPPING_LIST_KEY] = currentList
    }
}