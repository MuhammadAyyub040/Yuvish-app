package com.example.yuvish.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.databinding.KvitansiyaItemArrangedBinding

class ViewPagerAdapter2(var onItemClick: OnItemClick, var list: ArrayList<String>) :
    RecyclerView.Adapter<ViewPagerAdapter2.Vh>() {

    inner class Vh(val binding: KvitansiyaItemArrangedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            binding.btnSubmit.setOnClickListener {
                onItemClick.onItemClick(position)
            }
            binding.btnArranged.setOnClickListener {
                onItemClick.onItemClick2(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter2.Vh {
        return Vh(KvitansiyaItemArrangedBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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