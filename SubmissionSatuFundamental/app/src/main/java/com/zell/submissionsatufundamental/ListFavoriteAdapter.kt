package com.zell.submissionsatufundamental

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zell.submissionsatufundamental.databinding.ItemUserBinding

class ListFavoriteAdapter(private val onClickItem: (username: String) -> Unit) : RecyclerView.Adapter<ListFavoriteAdapter.ViewHolder>() {
    private var listFavorite = emptyList<FavoriteUser>()

    class ViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listFavorite[position]
        holder.binding.tvUsername.text = item.login
        Glide.with(holder.itemView.context).load(item.avatarUrl).centerCrop().into(holder.binding.ivUser)

        holder.binding.root.setOnClickListener {
            onClickItem(item.login)
        }
    }

    override fun getItemCount(): Int = listFavorite.size

    fun setData(data: List<FavoriteUser>) {
        val diffUtil = FavoriteDiffUtil(listFavorite, data)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        listFavorite = data
        diffResult.dispatchUpdatesTo(this)
    }
}