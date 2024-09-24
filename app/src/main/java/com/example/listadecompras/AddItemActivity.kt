package com.example.listadecompras

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.content.Intent
import com.example.listadecompras.databinding.ActivityAddItemBinding

class AddItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding
    private var itemToEdit: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Verifica se estamos editando um item
        itemToEdit = intent.getParcelableExtra("itemToEdit")
        itemToEdit?.let { item ->
            binding.etItemName.setText(item.name)
            binding.etItemQuantity.setText(item.quantity.toString())
            binding.etItemCategory.setText(item.category)
            binding.etItemUnit.setText(item.unit)
        }

        binding.btnSaveItem.setOnClickListener {
            val itemName = binding.etItemName.text.toString()
            val itemQuantity = binding.etItemQuantity.text.toString().toInt()
            val itemCategory = binding.etItemCategory.text.toString()
            val itemUnit = binding.etItemUnit.text.toString()

            val item = itemToEdit?.copy(
                name = itemName,
                quantity = itemQuantity,
                category = itemCategory,
                unit = itemUnit
            ) ?: Item(itemName, itemQuantity, itemUnit, itemCategory)

            val resultIntent = intent
            resultIntent.putExtra(if (itemToEdit != null) "updatedItem" else "newItem", item)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}