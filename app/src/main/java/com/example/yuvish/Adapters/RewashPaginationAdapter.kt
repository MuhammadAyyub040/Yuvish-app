package com.example.yuvish.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.Cleaning.RewashReceipt
import com.example.yuvish.databinding.KvitansiyaItemCleaningBinding

class RewashPaginationAdapter(context: Context, var onItemClick: OnItemClick) :
    PagingDataAdapter<RewashReceipt, RewashPaginationAdapter.MyViewHolder>(ArticleDiffItemCallback) {

    inner class MyViewHolder(val binding: KvitansiyaItemCleaningBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int,rewashReceipt: RewashReceipt) {

            binding.btnRegistration.setOnClickListener {
                onItemClick.onItemClickCleaning(position)
            }

            binding.txtKvitansiyaNumber.text = rewashReceipt.nomer.toString()
            binding.txtDate.text = rewashReceipt.topshir_sana
            binding.txtWashingTest.text = rewashReceipt.costumer.costumer_name
            binding.txtPhoneNumber.text = rewashReceipt.costumer.costumer_phone_1
            binding.txtLocation.text = rewashReceipt.costumer.costumer_addres
            binding.txtTimeOfRegistration.text = rewashReceipt.order_date
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(position,getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(KvitansiyaItemCleaningBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    private object ArticleDiffItemCallback: DiffUtil.ItemCallback<RewashReceipt>() {
        override fun areItemsTheSame(oldItem: RewashReceipt, newItem: RewashReceipt): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RewashReceipt, newItem: RewashReceipt): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClick {
        fun onItemClickCleaning(position: Int)
        }
}