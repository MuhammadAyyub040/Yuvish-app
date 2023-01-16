package com.example.yuvish.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.models.baseIndikatorsIndex.ReceivedRewashIndicatorOrder
import com.example.yuvish.R
import com.example.yuvish.databinding.ReceivedRewashIndicatorGroupItemLayoutBinding

class ReceivedRewashIndicatorGroupAdapter(private val context: Context):
    PagingDataAdapter<ReceivedRewashIndicatorOrder, ReceivedRewashIndicatorGroupAdapter.ItemHolder>(ReceivedRewashIndicatorOrderDiffItemCallback()) {

    inner class ItemHolder(val binding: ReceivedRewashIndicatorGroupItemLayoutBinding):
        RecyclerView.ViewHolder(binding.root){

        fun setItem(receivedRewashIndicatorOrder: ReceivedRewashIndicatorOrder, position: Int){
            val productsRewashedIndicatorChildAdapter = ProductsRewashedIndicatorChildAdapter(context)
            productsRewashedIndicatorChildAdapter.submitList(receivedRewashIndicatorOrder.products)
            binding.productsRV.adapter = productsRewashedIndicatorChildAdapter

            binding.ordinalNumber.text = "${position + 1}."
            binding.receiptNumber.text = receivedRewashIndicatorOrder.order_nomer.toString()
            binding.customerName.text = receivedRewashIndicatorOrder.costumer_name
            binding.totalProductCount.text = "${receivedRewashIndicatorOrder.tovar_dona} ${context.getString(R.string.pcs)}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            ReceivedRewashIndicatorGroupItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setItem(getItem(position)!!, position)
    }

}

class ReceivedRewashIndicatorOrderDiffItemCallback : DiffUtil.ItemCallback<ReceivedRewashIndicatorOrder>() {
    override fun areItemsTheSame(oldItem: ReceivedRewashIndicatorOrder, newItem: ReceivedRewashIndicatorOrder): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ReceivedRewashIndicatorOrder, newItem: ReceivedRewashIndicatorOrder): Boolean {
        return oldItem == newItem
    }
}