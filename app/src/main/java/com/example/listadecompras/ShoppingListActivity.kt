package com.example.listadecompras

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecompras.databinding.ActivityShoppingListBinding

class ShoppingListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShoppingListBinding
    private lateinit var shoppingListAdapter: ShoppingListAdapter
    private val originalList = mutableListOf<ShoppingList>() // Lista original
    private val filteredList = mutableListOf<ShoppingList>() // Lista filtrada
    private val viewModel: ShoppingListViewModel by viewModels()

    companion object {
        const val ADD_LIST_REQUEST_CODE = 1
        const val EDIT_LIST_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura o RecyclerView
        shoppingListAdapter = ShoppingListAdapter(
            filteredList,
            onItemClick = { selectedList -> openListDetails(selectedList) },
            onItemEdit = { selectedList -> editList(selectedList) },
            onItemDelete = { selectedList -> deleteList(selectedList) }
        )
        binding.recyclerViewShoppingLists.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewShoppingLists.adapter = shoppingListAdapter

        // Carrega as listas de compras do ViewModel
        originalList.addAll(viewModel.getShoppingLists())
        filteredList.addAll(originalList)
        shoppingListAdapter.notifyDataSetChanged()

        // Listener para o campo de busca
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

        // Configurar o ItemTouchHelper para swipe de editar e excluir
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val shoppingList = filteredList[position]

                if (direction == ItemTouchHelper.RIGHT) {
                    // Editar lista
                    editList(shoppingList)
                    shoppingListAdapter.notifyItemChanged(position)
                } else if (direction == ItemTouchHelper.LEFT) {
                    // Excluir lista
                    deleteList(shoppingList)
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recyclerViewShoppingLists)
    }

    private fun filterShoppingLists(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(originalList)
        } else {
            filteredList.addAll(originalList.filter { it.title.contains(query, ignoreCase = true) })
        }
        shoppingListAdapter.notifyDataSetChanged()
    }

    private fun openListDetails(list: ShoppingList) {
        val intent = Intent(this, ManageItemsActivity::class.java)
        intent.putExtra("listId", list.id)
        startActivity(intent)
    }

    private fun editList(list: ShoppingList) {
        val intent = Intent(this, AddShoppingListActivity::class.java).apply {
            putExtra("listId", list.id)
            putExtra("listTitle", list.title)
            putExtra("imageUri", list.imageUri)
        }
        startActivityForResult(intent, EDIT_LIST_REQUEST_CODE)
    }

    private fun deleteList(list: ShoppingList) {
        originalList.remove(list)
        filteredList.remove(list)
        shoppingListAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                ADD_LIST_REQUEST_CODE -> {
                    val newListTitle = data.getStringExtra("listTitle")
                    var newImageUri = data.getStringExtra("imageUri")
                    if (newImageUri == "null") {
                        newImageUri = Uri.parse("android.resource://${packageName}/drawable/logo_app").toString()
                    }
                    if (newListTitle != null) {
                        val newId = originalList.size + 1
                        val newShoppingList = ShoppingList(newId, newListTitle, mutableListOf(), newImageUri ?: Uri.parse("android.resource://${packageName}/drawable/logo_app").toString())

                        originalList.add(newShoppingList)
                        filteredList.clear()
                        filteredList.addAll(originalList)
                        shoppingListAdapter.notifyDataSetChanged()
                    }
                }
                EDIT_LIST_REQUEST_CODE -> {
                    val editedListId = data.getIntExtra("listId", -1)
                    val editedListTitle = data.getStringExtra("listTitle")
                    var editedImageUri = data.getStringExtra("imageUri")
                    if (editedImageUri == "null") {
                        editedImageUri = Uri.parse("android.resource://${packageName}/drawable/logo_app").toString()
                    }
                    if (editedListId != -1 && editedListTitle != null) {
                        val listToEdit = originalList.find { it.id == editedListId }
                        listToEdit?.let {
                            it.title = editedListTitle
                            it.imageUri = editedImageUri ?: it.imageUri
                            shoppingListAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }
}
