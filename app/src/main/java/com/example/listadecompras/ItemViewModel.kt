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
        state[ITEM_LIST_KEY] = currentItems
    }

    fun deleteItem(index: Int) {
        val currentItems = getItems()
        currentItems.removeAt(index)
        state[ITEM_LIST_KEY] = currentItems
    }

    fun editItem(index: Int, newItem: Item) {
        val currentItems = getItems()
        currentItems[index] = newItem
        state[ITEM_LIST_KEY] = currentItems
    }
}