package com.example.listadecompras

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecompras.databinding.ItemRowBinding

class ItemAdapter(
    private val items: MutableList<Item>,
    private val onEditItem: (Item, Int) -> Unit,
    private val onDeleteItem: (Int) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(private val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item, position: Int) {
            binding.tvItemName.text = item.name
            binding.tvItemQuantity.text = "${item.quantity} ${item.unit}"
            binding.tvItemCategory.text = item.category

            // Botão de Editar
            binding.btnEditItem.setOnClickListener {
                onEditItem(item, position)
            }

            // Botão de Excluir
            binding.btnDeleteItem.setOnClickListener {
                onDeleteItem(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size
}