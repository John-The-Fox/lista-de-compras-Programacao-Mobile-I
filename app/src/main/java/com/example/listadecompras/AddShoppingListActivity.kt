package com.example.listadecompras

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.listadecompras.databinding.ActivityAddShoppingListBinding

class AddShoppingListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddShoppingListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botão para salvar lista
        binding.btnSaveList.setOnClickListener {
            val listTitle = binding.etListTitle.text.toString()

            if (listTitle.isNotEmpty()) {
                // Enviar o nome da lista de volta para ShoppingListActivity
                val resultIntent = intent
                resultIntent.putExtra("listTitle", listTitle)
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                // Mostrar erro se o campo estiver vazio
                Toast.makeText(this, "Por favor, insira o título da lista", Toast.LENGTH_SHORT).show()
            }
        }
    }
}