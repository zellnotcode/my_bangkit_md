package com.zell.submissionsatufundamental

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zell.submissionsatufundamental.databinding.ItemUserBinding

class ListFollowAdapter : RecyclerView.Adapter<ListFollowAdapter.ViewHolder>() {
    private var listFollow = emptyList<UserResponse>()

    class ViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listFollow[position]
        holder.binding.tvUsername.text = item.login
        Glide.with(holder.itemView.context).load(item.avatarUrl).centerCrop().into(holder.binding.ivUser)
        holder.binding.tvSeeMore.visibility = View.GONE
    }

    override fun getItemCount(): Int = listFollow.size

    fun setData(data: List<UserResponse>) {
        val diffUtil = UserDiffUtil(listFollow, data)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        listFollow = data
        diffResult.dispatchUpdatesTo(this)
    }
}