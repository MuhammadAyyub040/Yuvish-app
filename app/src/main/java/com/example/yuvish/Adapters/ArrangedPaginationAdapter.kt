package com.example.yuvish.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.ReadyOrders.ReadyOrdersItem
import com.example.yuvish.databinding.KvitansiyaItemArrangedBinding

class ArrangedPaginationAdapter(var onItemClick: OnItemClick) :
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

            holder.binding.txtKvitansiyaNumberArranged.text = readyOrdersItem.nomer.toString()
            holder.binding.txtDateArranged.text = readyOrdersItem.topshir_sana
            holder.binding.txtCustomerArranged.text = readyOrdersItem.costumer.costumer_name
            holder.binding.txtPhoneNumberArranged.text = readyOrdersItem.costumer.costumer_phone_1
            holder.binding.txtLocationArranged.text = readyOrdersItem.costumer.costumer_addres
            holder.binding.txtOperatorArranged.text = readyOrdersItem.operator.fullname
            holder.binding.txtCommentOperatorArranged.text = readyOrdersItem.izoh
            holder.binding.txtCommentCustomerArranged.text = readyOrdersItem.izoh2
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
    }

}