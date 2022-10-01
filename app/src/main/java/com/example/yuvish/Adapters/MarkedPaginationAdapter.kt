package com.example.yuvish.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.DebtorsAPI.Market.MarketPaginationItem
import com.example.yuvish.Models.Warehouse.DifferenceDayManager
import com.example.yuvish.databinding.KvitansiyaItemUnfinishedBinding

class MarkedPaginationAdapter (var onItemClick: OnItemClick, val context: Context) :
    PagingDataAdapter<MarketPaginationItem, MarkedPaginationAdapter.MarketViewHolder>(ArticleDiffItemCallback) {

    inner class MarketViewHolder(val binding: KvitansiyaItemUnfinishedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int, marketPaginationItem: MarketPaginationItem) {
            binding.cardFinish.setOnClickListener {
                onItemClick.onItemClickMarked(marketPaginationItem)
            }
            binding.txtPhoneNumberUnFinishing.setOnClickListener {
                onItemClick.phoneItemClickMarked(marketPaginationItem)
            }

            val differenceDayManager= DifferenceDayManager(marketPaginationItem.ber_date, context)
            val resource = differenceDayManager.getResource()
            val color = differenceDayManager.getColor()

            binding.secondaryUnFinishing.background = resource
            binding.txtDataUnFinishing.setTextColor(color)

            binding.driverTxt.text = marketPaginationItem.user.fullname
            binding.txtAmountUnFinishing.text = "${marketPaginationItem.summa} ${"so'm"}"
            binding.txtNameUnFinishing.text = marketPaginationItem.costumer.costumer_name
            binding.txtPhoneNumberUnFinishing.text = marketPaginationItem.costumer.costumer_phone_1
            binding.txtDataUnFinishing.text = "${differenceDayManager.differanceDay} ${"kun"}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        return MarketViewHolder(KvitansiyaItemUnfinishedBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        holder.onBind(position, getItem(position)!!)
    }

    private object ArticleDiffItemCallback: DiffUtil.ItemCallback<MarketPaginationItem>() {
        override fun areItemsTheSame(oldItem: MarketPaginationItem, newItem: MarketPaginationItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MarketPaginationItem, newItem: MarketPaginationItem): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClick {
        fun onItemClickMarked(marketPaginationItem: MarketPaginationItem)
        fun phoneItemClickMarked(marketPaginationItem: MarketPaginationItem)
    }

    class DebtOrdersDiffItemCallback : DiffUtil.ItemCallback<MarketPaginationItem>() {
        override fun areItemsTheSame(oldItem: MarketPaginationItem, newItem: MarketPaginationItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MarketPaginationItem, newItem: MarketPaginationItem): Boolean {
            return oldItem == newItem
        }
    }
}