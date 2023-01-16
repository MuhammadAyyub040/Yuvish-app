package com.example.yuvish.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.models.NewOrder.Sources
import com.example.yuvish.databinding.ItemSourceBinding

class SourceAdapter(var list: List<Sources>, var onItemClick: OnItemClick) :
    RecyclerView.Adapter<SourceAdapter.SourceViewHolder>() {

    inner class SourceViewHolder(val binding: ItemSourceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(sources: Sources) {
            binding.sourceName.text = sources.name
            binding.cardGazeta.setOnClickListener {
                onItemClick.onItemClick(sources)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        return SourceViewHolder(ItemSourceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClick {
        fun onItemClick(sources: Sources)
    }
}