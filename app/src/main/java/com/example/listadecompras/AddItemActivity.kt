package com.example.listadecompras

import android.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import com.example.listadecompras.databinding.ActivityAddItemBinding

class AddItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding
    private var itemToEdit: Item? = null
    private val viewModel: ItemViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lista de categorias com emojis
        val categories = listOf(
            "\uD83C\uDF4EFruta", "\uD83E\uDD69Carne", "\uD83E\uDD55Legumes", "\uD83E\uDDC0Laticínios",
            "\uD83E\uDD64Bebidas", "\uD83E\uDDFCHigiene", "\uD83E\uDD56Padaria", "\uD83D\uDCE6Outros"
        )

        // Configurar o Spinner de categorias
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter

        // Verifica se estamos editando um item
        itemToEdit = intent.getParcelableExtra("itemToEdit")
        itemToEdit?.let { item ->
            // Preenche os campos se estiver editando
            binding.etItemName.setText(item.name)
            binding.etItemQuantity.setText(item.quantity.toString())
            binding.etItemUnit.setText(item.unit)
            // Seleciona a categoria no Spinner
            val categoryIndex = categories.indexOf(item.category)
            if (categoryIndex >= 0) {
                binding.spinnerCategory.setSelection(categoryIndex)
            }
        }

        // Lógica do botão "Salvar"
        binding.btnSaveItem.setOnClickListener {
            val itemName = binding.etItemName.text.toString().trim()
            val itemQuantity = binding.etItemQuantity.text.toString().toIntOrNull()
            val itemUnit = binding.etItemUnit.text.toString()
            val itemCategory = binding.spinnerCategory.selectedItem.toString()

            if (itemName.isNotEmpty() && itemQuantity != null && itemUnit.isNotEmpty()) {
                val item = itemToEdit?.copy(
                    name = itemName,
                    quantity = itemQuantity,
                    unit = itemUnit,
                    category = itemCategory
                ) ?: Item(
                    id = viewModel.generateItemId(),  // Novo ID para itens novos
                    name = itemName,
                    quantity = itemQuantity,
                    unit = itemUnit,
                    category = itemCategory
                )

                // Retorna o item editado ou criado
                val resultIntent = Intent().apply {
                    putExtra(if (itemToEdit != null) "updatedItem" else "newItem", item)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
