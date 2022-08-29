package com.example.yuvish.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Fragments.TayyorFragment
import com.example.yuvish.Models.ReadyOrders.ReadyOrdersItem
import com.example.yuvish.databinding.KvitansiyaItemUnsortedBinding

class NotArrangedPaginationAdapter(context: Context, var onItemClick: TayyorFragment) :
    PagingDataAdapter<ReadyOrdersItem, NotArrangedPaginationAdapter.ArrangedViewHolder>(ArticleDiffItemCallback) {

    inner class ArrangedViewHolder(val binding: KvitansiyaItemUnsortedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int, readyOrdersItem: ReadyOrdersItem) {

            binding.btnSubmit2.setOnClickListener {
                onItemClick.onItemClickSubmit2(readyOrdersItem)
            }
            binding.btnSorting2.setOnClickListener {
                onItemClick.onItemClickUnsorted2(readyOrdersItem)
            }

            binding.txtKvitansiyaNumberNotArranged.text = readyOrdersItem.nomer.toString()
            binding.txtDateNotArranged.text = readyOrdersItem.topshir_sana
            binding.txtCustomerNameNotArranged.text = readyOrdersItem.costumer.costumer_name
            binding.txtPhoneNumberNotArranged.text = readyOrdersItem.costumer.costumer_phone_1
            binding.txtLocationNotArranged.text = readyOrdersItem.costumer.costumer_addres
            binding.txtOperatorNotArranged.text = readyOrdersItem.operator.fullname
            binding.txtCommentOperatorNotArranged.text = readyOrdersItem.izoh
            binding.txtCommentCustomerNotArranged.text = readyOrdersItem.izoh2

        }
    }

    override fun onBindViewHolder(holder: ArrangedViewHolder, position: Int) {
        holder.onBind(position,getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArrangedViewHolder {
        return ArrangedViewHolder(KvitansiyaItemUnsortedBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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
        fun onItemClickSubmit2(readyOrdersItem: ReadyOrdersItem)
        fun onItemClickUnsorted2(readyOrdersItem: ReadyOrdersItem)
    }

}