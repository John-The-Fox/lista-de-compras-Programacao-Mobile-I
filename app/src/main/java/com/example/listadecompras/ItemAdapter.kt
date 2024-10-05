package com.example.listadecompras

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
            val categoryIcon = when (item.category) {
                "\uD83C\uDF4EFruta" -> "\uD83C\uDF4E"
                "\uD83E\uDD69Carne" -> "\uD83E\uDD69"
                "\uD83E\uDD55Legumes" -> "\uD83E\uDD55"
                "\uD83E\uDDC0Laticínios" -> "\uD83E\uDDC0"
                "\uD83E\uDD64Bebidas" -> "\uD83E\uDD64"
                "\uD83E\uDDFCHigiene" -> "\uD83E\uDDFC"
                "\uD83E\uDD56Padaria" -> "\uD83E\uDD56"
                "\uD83D\uDCE6Outros" -> "\uD83D\uDCE6"  // Pode escolher outro ícone se preferir
                else -> "\u2753" // Ícone de interrogação para categorias desconhecidas
            }
            binding.tvCategoryIcon.text = categoryIcon

            // Define o estado do checkbox e aplica o estilo tachado se o item estiver marcado
            binding.cbItemPurchased.isChecked = item.isChecked
            applyStrikeThrough(binding.tvItemName, binding.tvItemQuantity, item.isChecked)

            // Listener para o checkbox, altera o estado do item quando marcado/desmarcado
            binding.cbItemPurchased.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
                applyStrikeThrough(binding.tvItemName, binding.tvItemQuantity, isChecked)
            }
        }
        // Função para aplicar o efeito de texto tachado
        private fun applyStrikeThrough(tvName: TextView, tvQuantity: TextView, isChecked: Boolean) {
            if (isChecked) {
                tvName.paintFlags = tvName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tvQuantity.paintFlags = tvQuantity.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tvName.paintFlags = tvName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                tvQuantity.paintFlags = tvQuantity.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
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