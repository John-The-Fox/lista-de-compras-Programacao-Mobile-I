package com.example.listadecompras

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.listadecompras.databinding.ActivityAddShoppingListBinding

class AddShoppingListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddShoppingListBinding
    private var selectedImageUri: Uri? = null
    private var listId: Int = -1 // Variável para armazenar o ID da lista

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recupera os dados passados para a edição, se houver
        val listTitle = intent.getStringExtra("listTitle")
        val imageUri = intent.getStringExtra("imageUri")
        listId = intent.getIntExtra("listId", -1) // Recupera o listId da Intent

        if (listTitle != null) {
            binding.etListTitle.setText(listTitle)
        }
        if (imageUri != null) {
            selectedImageUri = Uri.parse(imageUri)
            binding.ivListImage.setImageURI(selectedImageUri)
        }

        // Configura o botão para escolher imagem
        binding.btnChooseImage.setOnClickListener {
            openImagePicker()
        }

        // Botão para salvar lista
        binding.btnSaveList.setOnClickListener {
            val newListTitle = binding.etListTitle.text.toString()

            if (newListTitle.isNotEmpty()) {
                val resultIntent = Intent().apply {
                    putExtra("listTitle", newListTitle)
                    putExtra("imageUri", selectedImageUri?.toString())
                    putExtra("listId", listId) // Retorna o ID da lista
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Por favor, insira o título da lista", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Função para abrir o seletor de imagens
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        imagePickerLauncher.launch(intent)
    }

    // Activity Result Launcher para pegar a imagem selecionada
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                selectedImageUri = uri
                binding.ivListImage.setImageURI(uri) // Exibe a imagem selecionada
            }
        }
    }
}