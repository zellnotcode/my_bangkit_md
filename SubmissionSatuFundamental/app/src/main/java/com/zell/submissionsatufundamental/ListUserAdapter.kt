package com.zell.submissionsatufundamental

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zell.submissionsatufundamental.databinding.ItemUserBinding

class ListUserAdapter(private val onClickItem: (username: String) -> Unit) : RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {
    private val listUser = ArrayList<UserResponse>()

    class ViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listUser[position]
        holder.binding.tvUsername.text = item.login
        Glide.with(holder.itemView.context).load(item.avatarUrl).centerCrop().into(holder.binding.ivUser)

        holder.binding.root.setOnClickListener {
            onClickItem(item.login.toString())
        }
    }

    override fun getItemCount(): Int = listUser.size

    fun setData(data: List<UserResponse>) {
        listUser.clear()
        listUser.addAll(data)
    }
}