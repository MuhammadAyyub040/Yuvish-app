package com.example.yuvish.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.ReadyOrders.ReadyOrdersItem
import com.example.yuvish.Models.Warehouse.DifferenceDayManager
import com.example.yuvish.databinding.KvitansiyaItemArrangedBinding

class ArrangedPaginationAdapter(val context: Context, var onItemClick: OnItemClick) :
    PagingDataAdapter<ReadyOrdersItem, ArrangedPaginationAdapter.ArrangedViewHolder>(ArticleDiffItemCallback) {

    inner class ArrangedViewHolder(val binding: KvitansiyaItemArrangedBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onBindViewHolder(holder: ArrangedViewHolder, position: Int) {
        val readyOrdersItem = getItem(position)

        if (readyOrdersItem != null) {
            holder.binding.btnSubmit.setOnClickListener {
                onItemClick.onItemClickSubmit(readyOrdersItem)
            }
            holder.binding.btnSorting.setOnClickListener {
                onItemClick.onItemClickUnsorted(readyOrdersItem)
            }
            holder.binding.txtLocationArranged.setOnClickListener {
                onItemClick.onItemClickLocation(readyOrdersItem)
            }
            holder.binding.txtOrderNumber.setOnClickListener {
                onItemClick.onItemClickViewOrder(readyOrdersItem)
            }

            holder.binding.txtPhoneNumberArranged.setOnClickListener {
                onItemClick.onItemClickPhoneNumber(readyOrdersItem)
            }

            val differenceDayManager= DifferenceDayManager(readyOrdersItem.topshir_sana, context)
            val resource = differenceDayManager.getResource()
            val color = differenceDayManager.getColor()

            holder.binding.secondaryArranged.background = resource
            holder.binding.txtDateArranged.setTextColor(color)
            holder.binding.txtKvitansiyaNumberArranged.setTextColor(color)

            holder.binding.txtKvitansiyaNumberArranged.text = readyOrdersItem.nomer.toString()
            holder.binding.txtDateArranged.text = "${differenceDayManager.differanceDay} ${"kun"}"
            holder.binding.txtCustomerArranged.text = readyOrdersItem.costumer.costumer_name
            holder.binding.txtPhoneNumberArranged.text = readyOrdersItem.costumer.costumer_phone_1
            holder.binding.txtLocationArranged.text = readyOrdersItem.costumer.costumer_addres
            holder.binding.txtOperatorArranged.text = readyOrdersItem.operator.fullname
            holder.binding.txtCommentOperatorArranged.text = readyOrdersItem.izoh
            holder.binding.txtCommentCustomerArranged.text = readyOrdersItem.izoh2
            holder.binding.txtOrderNumber.text = "${readyOrdersItem.product_count} ${"ta"}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArrangedViewHolder {
        return ArrangedViewHolder(KvitansiyaItemArrangedBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    private object ArticleDiffItemCallback: DiffUtil.ItemCallback<ReadyOrdersItem>() {
        override fun areItemsTheSame(oldItem: ReadyOrdersItem, newItem: ReadyOrdersItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ReadyOrdersItem, newItem: ReadyOrdersItem): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClick {
        fun onItemClickSubmit(readyOrdersItem: ReadyOrdersItem)
        fun onItemClickUnsorted(readyOrdersItem: ReadyOrdersItem)
        fun onItemClickLocation(readyOrdersItem: ReadyOrdersItem)
        fun onItemClickViewOrder(readyOrdersItem: ReadyOrdersItem)
        fun onItemClickPhoneNumber(readyOrdersItem: ReadyOrdersItem)
    }

    private fun startActivity(intent: Intent) {

    }

}