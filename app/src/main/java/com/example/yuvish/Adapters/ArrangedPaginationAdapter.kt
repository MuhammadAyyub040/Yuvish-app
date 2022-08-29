package com.example.yuvish.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Fragments.TayyorFragment
import com.example.yuvish.Models.ReadyOrders.ReadyOrdersItem
import com.example.yuvish.databinding.KvitansiyaItemArrangedBinding

class ArrangedPaginationAdapter(context: Context, var onItemClick: OnItemClick) :
    PagingDataAdapter<ReadyOrdersItem, ArrangedPaginationAdapter.ArrangedViewHolder>(ArticleDiffItemCallback) {

    inner class ArrangedViewHolder(val binding: KvitansiyaItemArrangedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int, readyOrdersItem: ReadyOrdersItem) {

            binding.btnSubmit.setOnClickListener {
                onItemClick.onItemClickSubmit(readyOrdersItem)
            }
            binding.btnSorting.setOnClickListener {
                onItemClick.onItemClickUnsorted(readyOrdersItem)
            }

            binding.txtKvitansiyaNumberArranged.text = readyOrdersItem.nomer.toString()
            binding.txtDateArranged.text = readyOrdersItem.topshir_sana
            binding.txtCustomerArranged.text = readyOrdersItem.costumer.costumer_name
            binding.txtPhoneNumberArranged.text = readyOrdersItem.costumer.costumer_phone_1
            binding.txtLocationArranged.text = readyOrdersItem.costumer.costumer_addres
            binding.txtOperatorArranged.text = readyOrdersItem.operator.fullname
            binding.txtCommentOperatorArranged.text = readyOrdersItem.izoh
            binding.txtCommentCustomerArranged.text = readyOrdersItem.izoh2

        }
    }

    override fun onBindViewHolder(holder: ArrangedViewHolder, position: Int) {
        holder.onBind(position,getItem(position)!!)
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