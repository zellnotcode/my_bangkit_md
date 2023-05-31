package com.zell.submissionpemula

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zell.submissionpemula.databinding.ItemFoodsBinding

class FoodAdapter(private val listFood: ArrayList<Food>) : RecyclerView.Adapter<FoodAdapter.ListViewHolder>() {
    private lateinit var onItemClick: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClick = onItemClickCallback
    }

    class ListViewHolder(var binding: ItemFoodsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemFoodsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = listFood[position]
        holder.binding.tvFoodName.text = item.name
        holder.binding.ivFoods.setImageResource(item.photo!!)
        holder.itemView.setOnClickListener {onItemClick.onItemClicked(listFood[holder.adapterPosition])}
    }

    override fun getItemCount(): Int = listFood.size

    interface OnItemClickCallback {
        fun onItemClicked(data: Food)
    }
}
