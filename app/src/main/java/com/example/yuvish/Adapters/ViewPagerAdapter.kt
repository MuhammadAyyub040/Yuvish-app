package com.example.yuvish.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.databinding.KvitansiyaItemBinding

class ViewPagerAdapter(var onItemClick: OnItemClick, var list: List<String>) :
    RecyclerView.Adapter<ViewPagerAdapter.Vh>() {

    inner class Vh(val binding: KvitansiyaItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            binding.button.setOnClickListener {
                onItemClick.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(KvitansiyaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClick {
        fun onItemClick(position: Int)
    }
}