package com.example.yuvish.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.databinding.KvitansiyaItem5Binding

class ViewPagerAdapter5 (var onItemClick: OnItemClick, var list: List<String>) :
    RecyclerView.Adapter<ViewPagerAdapter5.Vh>() {

    inner class Vh(val binding: KvitansiyaItem5Binding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            binding.cardFinish.setOnClickListener {
                onItemClick.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(KvitansiyaItem5Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClick {
        fun onItemClick(position: Int)
    }
}