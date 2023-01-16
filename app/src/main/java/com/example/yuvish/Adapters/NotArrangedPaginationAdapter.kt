package com.example.yuvish.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Fragments.TayyorFragment
import com.example.yuvish.models.ReadyOrders.ReadyOrdersItem
import com.example.yuvish.models.Warehouse.DifferenceDayManager
import com.example.yuvish.databinding.KvitansiyaItemUnsortedBinding
import com.example.yuvish.retrofit.isNull

class NotArrangedPaginationAdapter(val context: Context, var onItemClick: TayyorFragment) :
    PagingDataAdapter<ReadyOrdersItem, NotArrangedPaginationAdapter.ArrangedViewHolder>(ArticleDiffItemCallback) {

    inner class ArrangedViewHolder(val binding: KvitansiyaItemUnsortedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int, readyOrdersItem: ReadyOrdersItem) {

            binding.btnSubmit2.setOnClickListener {
                onItemClick.onItemClickSubmit2(readyOrdersItem)
            }
            binding.btnSorting2.setOnClickListener {
                onItemClick.onItemClickUnsorted2(readyOrdersItem)
            }
            binding.txtLocationNotArranged.setOnClickListener {
                onItemClick.onItemClickLocation(readyOrdersItem)
            }

            binding.txtProduct.setOnClickListener {
                onItemClick.onItemClickOrder(readyOrdersItem)
            }
            binding.txtPhoneNumberNotArranged.setOnClickListener {
                onItemClick.onItemClickUnSortedPhoneNumber(readyOrdersItem)
            }

            val differenceDayManager= DifferenceDayManager(readyOrdersItem.topshir_sana, context)
            val resource = differenceDayManager.getResource()
            val color = differenceDayManager.getColor()

            binding.secondaryNotarranged.background = resource
            binding.txtDateNotArranged.setTextColor(color)
            binding.txtKvitansiyaNumberNotArranged.setTextColor(color)

            binding.txtKvitansiyaNumberNotArranged.text = readyOrdersItem.nomer.toString()
            binding.txtDateNotArranged.text = "${differenceDayManager.differanceDay} ${"kun"}"
            if (readyOrdersItem.custumer.isNull()){
                binding.txtCustomerNameNotArranged.text = ""
                binding.txtPhoneNumberNotArranged.text = ""
                binding.txtLocationNotArranged.text = ""
            }else{
                binding.txtCustomerNameNotArranged.text = readyOrdersItem.custumer.costumer_name
                binding.txtPhoneNumberNotArranged.text = readyOrdersItem.custumer.costumer_phone_1
                binding.txtLocationNotArranged.text = readyOrdersItem.custumer.costumer_addres
            }
            binding.txtOperatorNotArranged.text = readyOrdersItem.operator.fullname
            binding.txtCommentOperatorNotArranged.text = readyOrdersItem.izoh
            binding.txtCommentCustomerNotArranged.text = readyOrdersItem.izoh2
            binding.txtProduct.text = "${readyOrdersItem.cleans_count} ${"ta"}"

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
        fun onItemClickUnsortedLocation(readyOrdersItem: ReadyOrdersItem)
        fun onItemClickOrder(readyOrdersItem: ReadyOrdersItem)
        fun onItemClickUnSortedPhoneNumber(readyOrdersItem: ReadyOrdersItem)
    }

}