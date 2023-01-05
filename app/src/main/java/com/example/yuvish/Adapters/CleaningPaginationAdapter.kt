package com.example.yuvish.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.Cleaning.CleaningData
import com.example.yuvish.Models.Warehouse.DifferenceDayManager
import com.example.yuvish.databinding.KvitansiyaItemCleaningBinding

class CleaningPaginationAdapter(val context: Context, var onItemClick: OnItemClick) :
    PagingDataAdapter<CleaningData, CleaningPaginationAdapter.MyViewHolder>(ArticleDiffItemCallback) {

    inner class MyViewHolder(val binding: KvitansiyaItemCleaningBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int,cleaningData: CleaningData) {

            binding.btnRegistration.setOnClickListener {
                onItemClick.onItemClickCleaning(cleaningData)
            }
            binding.txtPhoneNumber.setOnClickListener {
                onItemClick.onItemClickPhoneNumber(cleaningData)
            }

            val differenceDayManager= DifferenceDayManager(cleaningData.topshir_sana, context)
            val resource = differenceDayManager.getResource()
            val color = differenceDayManager.getColor()

            binding.secondaryCleaning.background = resource
            binding.txtDate.setTextColor(color)
            binding.txtKvitansiyaNumber.setTextColor(color)

            binding.txtKvitansiyaNumber.text = cleaningData.nomer.toString()
            binding.txtDate.text = "${differenceDayManager.differanceDay} ${"kun"}"
            binding.txtWashingTest.text = cleaningData.custumer.costumer_name
            binding.txtPhoneNumber.text = cleaningData.custumer.costumer_phone_1
            binding.txtLocation.text = cleaningData.custumer.costumer_addres
            binding.txtTimeOfRegistration.text = cleaningData.order_date
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(position,getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(KvitansiyaItemCleaningBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    private object ArticleDiffItemCallback: DiffUtil.ItemCallback<CleaningData>() {
        override fun areItemsTheSame(oldItem: CleaningData, newItem: CleaningData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CleaningData, newItem: CleaningData): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClick {
        fun onItemClickCleaning(cleaningData: CleaningData)
        fun onItemClickPhoneNumber(cleaningData: CleaningData)
        }
}