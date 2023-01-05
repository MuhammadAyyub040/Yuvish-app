package com.example.yuvish.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.ReadyOrders.ReadyOrdersItem
import com.example.yuvish.Models.Warehouse.DifferenceDayManager
import com.example.yuvish.Models.Warehouse.OrdersOmborItem
import com.example.yuvish.Models.Warehouse.WarehouseData
import com.example.yuvish.R
import com.example.yuvish.databinding.KvitansiyaItemWarehouseBinding
import com.example.yuvish.retrofit.isNull

class WerehousePaginationAdapter(val context: Context, var onItemClick: OnItemClick) :
    PagingDataAdapter<WarehouseData,WerehousePaginationAdapter.WerehouseViewHolder>(ArticleDiffItemCallback) {

    inner class WerehouseViewHolder(val binding: KvitansiyaItemWarehouseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int,warehouseData: WarehouseData) {

            binding.cardSubmit.setOnClickListener {
                onItemClick.onItemClickWarehouse(warehouseData)
            }
            binding.cardTransport.setOnClickListener {
                onItemClick.onItemClick2Warehouse(warehouseData)
            }
            binding.txtPhoneNumberWarehouse.setOnClickListener {
                onItemClick.onItemClickPhoneNumber(warehouseData)
            }

            val differenceDayManager= DifferenceDayManager(warehouseData.topshir_sana, context)
            val resource = differenceDayManager.getResource()
            val color = differenceDayManager.getColor()

            binding.secondary.background = resource
            binding.txtDateWarehouse.setTextColor(color)
            binding.txtKvitansiyaNumberWarehouse.setTextColor(color)
            binding.txtOperator.text = warehouseData.operator.fullname
            binding.txtKvitansiyaNumberWarehouse.text = warehouseData.nomer.toString()
            binding.txtDateWarehouse.text = "${differenceDayManager.differanceDay} ${"kun"}"
            if (warehouseData.ombor.isNull()){
                binding.omborchiName.text = context.getString(R.string.nomalum)
            }else{
                binding.omborchiName.text = warehouseData.ombor.fullname
            }
            binding.txtNameWarehouse.text = warehouseData.custumer.costumer_name
            binding.txtPhoneNumberWarehouse.text = warehouseData.custumer.costumer_phone_1
            binding.txtLocationWarehouse.text = warehouseData.custumer.costumer_addres
            binding.txtCommentOperator.text = warehouseData.izoh
            binding.txtCommentOrder.text = warehouseData.izoh2
        }
    }

    override fun onBindViewHolder(holder: WerehouseViewHolder, position: Int) {
        holder.onBind(position,getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WerehouseViewHolder {
        return WerehouseViewHolder(KvitansiyaItemWarehouseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    private object ArticleDiffItemCallback: DiffUtil.ItemCallback<WarehouseData>() {
        override fun areItemsTheSame(oldItem: WarehouseData, newItem: WarehouseData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WarehouseData, newItem: WarehouseData): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClick {
        fun onItemClickWarehouse(warehouseData: WarehouseData)
        fun onItemClick2Warehouse(warehouseData: WarehouseData)
        fun onItemClickPhoneNumber(warehouseData: WarehouseData)
    }

}