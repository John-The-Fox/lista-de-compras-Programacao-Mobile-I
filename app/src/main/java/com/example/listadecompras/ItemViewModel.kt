package com.example.listadecompras

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class ItemViewModel(private val state: SavedStateHandle) : ViewModel() {

    private val ITEM_LIST_KEY = "item_list_key"

    init {
        if (!state.contains(ITEM_LIST_KEY)) {
            state[ITEM_LIST_KEY] = mutableListOf<Item>()
        }
    }

    fun getItems(): MutableList<Item> {
        return state[ITEM_LIST_KEY] ?: mutableListOf()
    }

    fun addItem(item: Item) {
        val currentItems = getItems()
        currentItems.add(item)
        currentItems.sortBy { it.name }
        state[ITEM_LIST_KEY] = currentItems
    }

    fun deleteItem(index: Int) {
        val currentItems = getItems()
        if (currentItems.isNotEmpty() && index in currentItems.indices) {
            currentItems.removeAt(index)
            state[ITEM_LIST_KEY] = currentItems
        }
    }

    fun updateItem(updatedItem: Item) {
        val currentItems = getItems()
        val index = currentItems.indexOfFirst { it.id == updatedItem.id }

        if (index != -1) {
            currentItems[index] = updatedItem
            currentItems.sortBy { it.name }
            state[ITEM_LIST_KEY] = currentItems
        }
    }

    // Para garantir que IDs únicos sejam atribuídos aos itens
    fun generateItemId(): Int {
        val currentItems = getItems()
        return if (currentItems.isEmpty()) 1 else currentItems.maxOf { it.id } + 1
    }
}
