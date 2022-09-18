package com.example.yuvish.Adapters

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.DebtorsAPI.Market.FilterDebtorsItem
import com.example.yuvish.Models.DebtorsAPI.Market.MarketPaginationItem
import com.example.yuvish.Models.Warehouse.DifferenceDayManager
import com.example.yuvish.databinding.KvitansiyaItemDebtsBinding

class UnMarkedPaginationAdapter (var onItemClick: OnItemClick, val context: Context) :
    PagingDataAdapter<MarketPaginationItem, UnMarkedPaginationAdapter.UnMarkedViewHolder>(ArticleDiffItemCallback){

    private var paymentTypesList = emptyList<FilterDebtorsItem>()

    private var amountValues = arrayOfNulls<String>(itemCount)
    private var paymentTypePositionValues = arrayOfNulls<Int>(itemCount)

    inner class UnMarkedViewHolder(val binding: KvitansiyaItemDebtsBinding) : RecyclerView.ViewHolder(binding.root) {

//        private fun checkPayDebt(marketPaginationItem: MarketPaginationItem, position: Int){
//            val amount = binding.edtAmount.text.toString().toDoubleOrNull() ?: 0.0
//
//            when{
//                amount == 0.0 -> {
//                    Toast.makeText(context, context.getString(R.string.enter_correct_amount), Toast.LENGTH_SHORT).show()
//                }
//                paymentTypesList.isEmpty() -> {
//                    Toast.makeText(context, context.getString(R.string.select_payment_type), Toast.LENGTH_SHORT).show()
//                }
//                else -> {
//                    val payDebt = PayDebt(
//                        marketPaginationItem.id, amount, paymentTypesList[paymentTypePositionValues[position] ?: 0].title
//                    )
//
//                    callBack.payDebtClickListener(payDebt)
//                }
//            }
//        }

        fun onBind(position: Int, marketPaginationItem: MarketPaginationItem) {
//            binding.cardGetMoney.setOnClickListener {
//                checkPayDebt(marketPaginationItem, position)
//            }
            binding.cardSendOf.setOnClickListener {
                onItemClick.onItemClickUnMarked2(marketPaginationItem)
            }

            val arrayAdapter = ArrayAdapter(context, R.layout.simple_list_item_1, arrayListOf("Naqd", "Terminal bank", "Click"))
            binding.autoCompleteTextView.setAdapter(arrayAdapter)

            val differenceDayManager= DifferenceDayManager(marketPaginationItem.ber_date, context)
            val resource = differenceDayManager.getResource()
            val color = differenceDayManager.getColor()

            binding.secondaryDebts.background = resource
            binding.txtDataDebts.setTextColor(color)

            binding.txtDriverDebts.text = marketPaginationItem.user.fullname
            binding.txtAmountDebts.text = "${marketPaginationItem.summa} ${"so'm"}"
            binding.txtNameDebts.text = marketPaginationItem.costumer.costumer_name
            binding.txtPhoneNumberDebts.text = marketPaginationItem.costumer.costumer_phone_1
            binding.txtDataDebts.text = "${differenceDayManager.differanceDay} ${"kun"}"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnMarkedPaginationAdapter.UnMarkedViewHolder {
        return UnMarkedViewHolder(KvitansiyaItemDebtsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: UnMarkedViewHolder, position: Int) {
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
        fun onItemClickUnMarked(marketPaginationItem: MarketPaginationItem)
        fun onItemClickUnMarked2(marketPaginationItem: MarketPaginationItem)
    }

    fun submitPaymentTypesList(paymentTypesList: List<FilterDebtorsItem>){
        this.paymentTypesList = paymentTypesList
        notifyDataSetChanged()
    }
}