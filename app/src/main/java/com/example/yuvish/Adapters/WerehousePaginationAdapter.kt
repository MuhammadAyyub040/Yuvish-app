package com.example.yuvish.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.Warehouse.OrdersOmborItem
import com.example.yuvish.databinding.KvitansiyaItemWarehouseBinding

class WerehousePaginationAdapter(context: Context, var onItemClick: OnItemClick) :
    PagingDataAdapter<OrdersOmborItem,WerehousePaginationAdapter.WerehouseViewHolder>(ArticleDiffItemCallback) {

    inner class WerehouseViewHolder(val binding: KvitansiyaItemWarehouseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int,ordersOmborItem: OrdersOmborItem) {

            binding.btnSubmitSklad.setOnClickListener {
                onItemClick.onItemClickWarehouse(ordersOmborItem)
            }
            binding.btnTransfer.setOnClickListener {
                onItemClick.onItemClick2Warehouse(ordersOmborItem)
            }

            binding.txtKvitansiyaNumberWarehouse.text = ordersOmborItem.nomer.toString()
            binding.txtDateWarehouse.text = ordersOmborItem.topshir_sana
            binding.txtNameWarehouse.text = ordersOmborItem.costumer.costumer_name
            binding.txtPhoneNumberWarehouse.text = ordersOmborItem.costumer.costumer_phone_1
            binding.txtLocationWarehouse.text = ordersOmborItem.costumer.costumer_addres
            binding.txtCommentOperator.text = ordersOmborItem.izoh
            binding.txtCommentOrder.text = ordersOmborItem.izoh2
        }
    }

    override fun onBindViewHolder(holder: WerehouseViewHolder, position: Int) {
        holder.onBind(position,getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WerehouseViewHolder {
        return WerehouseViewHolder(KvitansiyaItemWarehouseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    private object ArticleDiffItemCallback: DiffUtil.ItemCallback<OrdersOmborItem>() {
        override fun areItemsTheSame(oldItem: OrdersOmborItem, newItem: OrdersOmborItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: OrdersOmborItem, newItem: OrdersOmborItem): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClick {
        fun onItemClickWarehouse(ordersOmborItem: OrdersOmborItem)
        fun onItemClick2Warehouse(ordersOmborItem: OrdersOmborItem)
    }

}