package com.example.yuvish.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.databinding.*

class RegistrationAdapter (var onItemClick: OnItemClick, var list: List<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class GilamVh(val binding: ItemGilamBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
        }
    }
    inner class AdyolVh(val binding: ItemAdyolBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
        }
    }
    inner class PardaVh(val binding: ItemPardaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
        }
    }
    inner class JoyidaVh(val binding: ItemJoyidaYuvishBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
        }
    }
    inner class YangiVh(val binding: ItemYangiVBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
        }
    }
    inner class ToysVh(val binding: ItemYumshoqOyinchoqBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AdyolVh(ItemAdyolBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {

        return super.getItemViewType(position)
    }

    interface OnItemClick {
        fun onItemClick(position: Int)
    }
}
