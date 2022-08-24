package com.example.yuvish.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.databinding.KvitansiyaItemUnsortedBinding

class ViewPagerAdapter3(var onItemClick: OnItemClick, var list: List<String>) :
    RecyclerView.Adapter<ViewPagerAdapter3.Vh>() {

    inner class Vh(val binding: KvitansiyaItemUnsortedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            binding.btnSubmit2.setOnClickListener {
                onItemClick.onItemClick(position)
            }
            binding.btnArranged2.setOnClickListener {
                onItemClick.onItemClick2(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter3.Vh {
        return Vh(KvitansiyaItemUnsortedBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClick {
        fun onItemClick(position: Int)
        fun onItemClick2(position: Int)
    }
}