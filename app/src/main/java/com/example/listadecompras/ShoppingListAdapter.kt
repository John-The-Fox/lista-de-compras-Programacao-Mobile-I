package com.example.listadecompras

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Alteramos o tipo de 'String' para 'ShoppingList'
class ShoppingListAdapter(
    private val shoppingLists: MutableList<ShoppingList>,
    private val onItemClick: (ShoppingList) -> Unit,  // Callback para abrir detalhes
    private val onItemEdit: (ShoppingList) -> Unit,   // Callback para editar
    private val onItemDelete: (ShoppingList) -> Unit  // Callback para deletar
) : RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>() {



    class ShoppingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listTitle: TextView = itemView.findViewById(R.id.listTitle)
        val listImage: ImageView = itemView.findViewById(R.id.listImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shopping_list, parent, false)
        return ShoppingListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val currentList = shoppingLists[position]
        holder.listTitle.text = currentList.title

        // Exibir a imagem da lista (ou placeholder se não houver imagem)
        if (!currentList.imageUri.isNullOrEmpty()) {
            try {
                holder.listImage.setImageURI(Uri.parse(currentList.imageUri))
            } catch (e: Exception) {
                // Em caso de erro, define o placeholder
                holder.listImage.setImageResource(R.mipmap.ic_launcher)
            }
        } else {
            holder.listImage.setImageResource(R.mipmap.ic_launcher)  // Placeholder
        }

        // Configurando as interações
        holder.itemView.setOnClickListener {
            onItemClick(currentList)
        }
        holder.itemView.setOnLongClickListener {
            // Configurar swipe para editar/excluir aqui ou no RecyclerView
            true
        }
    }

    override fun getItemCount(): Int {
        return shoppingLists.size
    }

    // Método atualizado para adicionar uma nova lista de compras
    fun addShoppingList(newShoppingList: ShoppingList) {
        shoppingLists.add(newShoppingList)
        notifyItemInserted(shoppingLists.size - 1)
    }
}
