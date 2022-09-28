package com.example.yuvish.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.NewOrder.SearchCustomerResult
import com.example.yuvish.R
import com.example.yuvish.databinding.TicketSearchCustomerItemBinding

class SearchCustomerResultsAdapter(
    private val context: Context,
    private val callBack: CallBack,
    private val isGlobalSearch: Boolean
    ): ListAdapter<SearchCustomerResult, SearchCustomerResultsAdapter.ItemHolder>(SearchCustomerResultDiffItemCallback()) {

    interface CallBack{
        fun writeReceiptSelected(searchCustomerResult: SearchCustomerResult)
        fun phoneClicked(phone1: String, phone2: String)
    }

    inner class ItemHolder(val binding: TicketSearchCustomerItemBinding): RecyclerView.ViewHolder(binding.root){

        fun setItem(searchCustomerResult: SearchCustomerResult){
            binding.customerName.text = searchCustomerResult.costumer1?.costumer_name
            binding.address.text = searchCustomerResult.costumer1?.costumer_addres
            binding.date.text = searchCustomerResult.costumer1?.costumer_date
            binding.status.text = searchCustomerResult.costumer1?.costumer_status
            binding.activeReceiptsLayout.isVisible = isGlobalSearch
            if (isGlobalSearch){
                binding.activeReceipts.text = calculateActiveOrders(searchCustomerResult.active_orders)
            }

            if (searchCustomerResult.tasdilanadigan_order_id != 0){
                binding.buttonTitle.text = context.getString(R.string.order_confirmation)
                binding.buttonIcon.setImageResource(R.drawable.ic_check)
                binding.writeReceipt.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.yellow)
                )
            }else{
                binding.buttonTitle.text = context.getString(R.string.write_receipt)
                binding.buttonIcon.setImageResource(R.drawable.writing_svgrepo_com)
                binding.writeReceipt.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.blue)
                )
            }

            binding.phone.setOnClickListener {
                callBack.phoneClicked(
                    searchCustomerResult.costumer1?.costumer_phone_1 ?: "",
                    searchCustomerResult.costumer1?.costumer_phone_2 ?: ""
                )
            }

            binding.writeReceipt.setOnClickListener {
                callBack.writeReceiptSelected(searchCustomerResult)
            }
        }

        private fun calculateActiveOrders(activeOrdersList: List<com.example.yuvish.Models.NewOrder.ActiveOrder>): String{
            var activeOrdersStr = ""

            activeOrdersList.forEachIndexed { index, activeOrder ->
                activeOrdersStr += if (index == 0){
                    activeOrder.nomer
                }else {
                    ", ${activeOrder.nomer}"
                }
            }

            return activeOrdersStr
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(TicketSearchCustomerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setItem(getItem(position))
    }

}

class SearchCustomerResultDiffItemCallback : DiffUtil.ItemCallback<SearchCustomerResult>() {
    override fun areItemsTheSame(oldItem: SearchCustomerResult, newItem: SearchCustomerResult): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: SearchCustomerResult, newItem: SearchCustomerResult): Boolean {
        return oldItem == newItem
    }
}