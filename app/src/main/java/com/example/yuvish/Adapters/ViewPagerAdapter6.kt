package com.example.yuvish.Adapters

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.databinding.KvitansiyaItemDebtsBinding

class ViewPagerAdapter6 (var onItemClick: OnItemClick, var list: List<String>, val context: Context) :
    RecyclerView.Adapter<ViewPagerAdapter6.Vh>() {

    inner class Vh(val binding: KvitansiyaItemDebtsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            binding.cardGetMoney.setOnClickListener {

                val get = binding.edtAmount.text.toString().trim()
                if (get.isEmpty()) {
                    binding.edtAmount.error = "Summani kiriting"
                    binding.edtAmount.requestFocus()
                }else {
                    onItemClick.onItemClick(position)
                }
            }
            binding.cardSendOf.setOnClickListener {
                onItemClick.onItemClick2(position)
            }
            val arrayAdapter = ArrayAdapter(context, R.layout.simple_list_item_1, arrayListOf("Naqd", "Terminal bank", "Click"))
            binding.autoCompleteTextView.setAdapter(arrayAdapter)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(KvitansiyaItemDebtsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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