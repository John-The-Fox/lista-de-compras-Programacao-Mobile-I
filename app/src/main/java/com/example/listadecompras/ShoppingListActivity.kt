package com.example.listadecompras

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listadecompras.databinding.ActivityShoppingListBinding

class ShoppingListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShoppingListBinding
    private lateinit var shoppingListAdapter: ShoppingListAdapter
    private val filteredList = mutableListOf<ShoppingList>()
    // Inicializando o ViewModel com SavedStateHandle
    private val viewModel: ShoppingListViewModel by viewModels()

    companion object {
        const val ADD_LIST_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura o RecyclerView
        shoppingListAdapter = ShoppingListAdapter(filteredList)
        binding.recyclerViewShoppingLists.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewShoppingLists.adapter = shoppingListAdapter

        // Carrega as listas de compras do ViewModel
        filteredList.addAll(viewModel.getShoppingLists())
        shoppingListAdapter.notifyDataSetChanged()

        // Adiciona o listener de busca
        binding.etSearchList.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterShoppingLists(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Botão para adicionar nova lista
        binding.btnAddShoppingList.setOnClickListener {
            val intent = Intent(this, AddShoppingListActivity::class.java)
            startActivityForResult(intent, ADD_LIST_REQUEST_CODE)
        }
    }

    // Função para filtrar listas de compras
    private fun filterShoppingLists(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(shoppingList)
        } else {
            filteredList.addAll(shoppingList.filter {
                it.name.contains(query, ignoreCase = true)
            })
        }
        shoppingListAdapter.notifyDataSetChanged()
    }

    // Função para adicionar uma nova lista (exemplo de uso do ViewModel)
    private fun addNewShoppingList(shoppingList: ShoppingList) {
        viewModel.addShoppingList(shoppingList)
        filteredList.clear()
        filteredList.addAll(viewModel.getShoppingLists())
        shoppingListAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_LIST_REQUEST_CODE && resultCode == RESULT_OK) {
            val newListTitle = data?.getStringExtra("listTitle")
            newListTitle?.let {
                shoppingListAdapter.addShoppingList(it)
            }
        }
    }
}