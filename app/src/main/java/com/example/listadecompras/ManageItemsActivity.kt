package com.example.listadecompras

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listadecompras.databinding.ActivityManageItemsBinding

class ManageItemsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageItemsBinding
    private lateinit var itemAdapter: ItemAdapter
    private val filteredItems = mutableListOf<Item>()

    private val viewModel: ItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura o RecyclerView
        itemAdapter = ItemAdapter(filteredItems, this::editItem, this::deleteItem)
        binding.recyclerViewItems.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewItems.adapter = itemAdapter

        // Carrega os itens do ViewModel
        filteredItems.addAll(viewModel.getItems())
        itemAdapter.notifyDataSetChanged()

        // Adiciona o listener de busca
        binding.etSearchItem.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterItems(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Botão para adicionar um novo item
        binding.btnAddNewItem.setOnClickListener {
            // Adicionar item de exemplo
            val newItem = Item("Novo Item", 1, "un", "Categoria")
            viewModel.addItem(newItem)
            filteredItems.clear()
            filteredItems.addAll(viewModel.getItems())
            itemAdapter.notifyDataSetChanged()
        }
    }

    // Função para filtrar os itens
    private fun filterItems(query: String) {
        val allItems = viewModel.getItems()
        filteredItems.clear()
        if (query.isEmpty()) {
            filteredItems.addAll(allItems)
        } else {
            filteredItems.addAll(allItems.filter {
                it.name.contains(query, ignoreCase = true)
            })
        }
        itemAdapter.notifyDataSetChanged()
    }

    // Função para editar item
    private fun editItem(item: Item, position: Int) {
        // Lógica de edição
    }

    // Função para excluir item
    private fun deleteItem(position: Int) {
        viewModel.deleteItem(position)
        filteredItems.clear()
        filteredItems.addAll(viewModel.getItems())
        itemAdapter.notifyDataSetChanged()
    }
}
