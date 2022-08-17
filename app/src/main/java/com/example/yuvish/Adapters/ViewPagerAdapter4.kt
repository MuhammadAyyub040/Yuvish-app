package com.example.yuvish.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.databinding.KvitansiyaItem4Binding

class ViewPagerAdapter4(var onItemClick: OnItemClick, var list: List<String>) :
    RecyclerView.Adapter<ViewPagerAdapter4.Vh>() {

    inner class Vh(val binding: KvitansiyaItem4Binding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            binding.btnSubmitSklad.setOnClickListener {
                onItemClick.onItemClick(position)
            }
            binding.btnTransfer.setOnClickListener {
                onItemClick.onItemClick2(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter4.Vh {
        return Vh(KvitansiyaItem4Binding.inflate(LayoutInflater.from(parent.context), parent, false))
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