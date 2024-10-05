package com.example.listadecompras

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecompras.ShoppingListActivity.Companion.ADD_LIST_REQUEST_CODE
import com.example.listadecompras.databinding.ActivityManageItemsBinding

class ManageItemsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageItemsBinding
    private lateinit var itemAdapter: ItemAdapter
    private val filteredItems = mutableListOf<Item>()

    private val viewModel: ItemViewModel by viewModels()

    companion object {
        const val ADD_ITEM_REQUEST_CODE = 1
        const val EDIT_ITEM_REQUEST_CODE = 2
    }

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
            val intent = Intent(this, AddItemActivity::class.java)
            startActivityForResult(intent, ADD_ITEM_REQUEST_CODE)
        }

        // Configurar o ItemTouchHelper para swipe de editar e excluir
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false // Não precisamos de suporte para mover itens
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = filteredItems[position]

                if (direction == ItemTouchHelper.RIGHT) {
                    // Editar item
                    editItem(item, position)
                    itemAdapter.notifyItemChanged(position)
                } else if (direction == ItemTouchHelper.LEFT) {
                    // Excluir item
                    deleteItem(position)
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recyclerViewItems)
    }

    override fun onResume() {
        super.onResume()
        filteredItems.clear()
        filteredItems.addAll(viewModel.getItems())
        itemAdapter.notifyDataSetChanged()
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
        val intent = Intent(this, AddItemActivity::class.java).apply {
            putExtra("itemId", item.id)
            putExtra("itemName", item.name)
            putExtra("itemQuantity", item.quantity)
            putExtra("itemUnit", item.unit)
            putExtra("itemCategory", item.category)
            // Adicionar outros campos conforme necessário
        }
        startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE)
    }

    // Função para excluir item
    private fun deleteItem(position: Int) {
        val item = filteredItems[position]
        viewModel.deleteItem(item.id) // Supondo que o ViewModel lide com a exclusão pelo ID do item
        filteredItems.removeAt(position)
        itemAdapter.notifyItemRemoved(position)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                EDIT_ITEM_REQUEST_CODE -> {
                    val updatedItem = data.getParcelableExtra<Item>("updatedItem")
                    updatedItem?.let {
                        viewModel.updateItem(it)
                        filteredItems.clear()
                        filteredItems.addAll(viewModel.getItems())
                        itemAdapter.notifyDataSetChanged()
                    }
                }
                ADD_ITEM_REQUEST_CODE -> {
                    val newItem = data.getParcelableExtra<Item>("newItem")
                    newItem?.let {
                        viewModel.addItem(it)
                        filteredItems.clear()
                        filteredItems.addAll(viewModel.getItems())
                        itemAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

}