package com.example.yuvish.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.ArrangedSubmit.PaymentTypesItem
import com.example.yuvish.Models.DebtorsAPI.Market.FilterDebtorsItem
import com.example.yuvish.Models.DebtorsAPI.Market.MarketPaginationItem
import com.example.yuvish.Models.DebtorsAPI.Market.Paydebt
import com.example.yuvish.Models.Warehouse.DifferenceDayManager
import com.example.yuvish.R
import com.example.yuvish.databinding.KvitansiyaItemDebtsBinding
import com.example.yuvish.retrofit.isNull

class DebtorsCompletedAdapter(
    private val context: Context,
    private val callBack: CallBack
    ): PagingDataAdapter<MarketPaginationItem, DebtorsCompletedAdapter.ItemHolder>(
    MarkedPaginationAdapter.DebtOrdersDiffItemCallback()
) {

    private var paymentTypesList = emptyList<PaymentTypesItem>()

    private var amountValues = arrayOfNulls<String>(itemCount)
    private var paymentTypePositionValues = arrayOfNulls<Int>(itemCount)

    interface CallBack{
        fun phoneClickListener(marketPaginationItem: MarketPaginationItem)
        fun payDebtClickListener(payDebt: Paydebt)
        fun debtOffClickListener(marketPaginationItem: MarketPaginationItem)
    }

    inner class ItemHolder(val binding: KvitansiyaItemDebtsBinding): RecyclerView.ViewHolder(binding.root){

        init {
            binding.edtDebtAmount.addTextChangedListener {
                amountValues[binding.edtDebtAmount.tag as Int] = it.toString()
            }

            binding.paymentType.setOnItemClickListener { _, _, position, _ ->
                paymentTypePositionValues[binding.paymentType.tag as Int] = position
            }
        }

        @SuppressLint("SetTextI18n")
        fun setItem(marketPaginationItem: MarketPaginationItem, position: Int){
            val paymentTypesAdapter = ArrayAdapter(
                context, android.R.layout.simple_list_item_1, paymentTypesList.map { it.name })

            val differenceDayManager = DifferenceDayManager(marketPaginationItem.ber_date, context)

            val resource = differenceDayManager.getResource()
            val color = differenceDayManager.getColor()

            binding.secondaryDebts.background = resource
            binding.txtDataDebts.setTextColor(color)

            binding.edtDebtAmount.tag = position
            binding.paymentType.tag = position

            binding.txtDataDebts.text = "${differenceDayManager.differanceDay} ${context.getString(R.string.kun)}"
            binding.txtDriverDebts.text = marketPaginationItem.user.fullname
            binding.txtNameDebts.text = marketPaginationItem.costumer.costumer_name
            binding.txtPhoneNumberDebts.text = marketPaginationItem.costumer.costumer_phone_1
            binding.txtAmountDebts.text = "${marketPaginationItem.summa} ${context.getString(R.string.so_m)}"

            binding.paymentType.setAdapter(paymentTypesAdapter)
            if (paymentTypesList.isNotEmpty()) {
                binding.paymentType.setText(
                    paymentTypesList[paymentTypePositionValues[position] ?: 0].name, false
                )
            }

            binding.edtDebtAmount.setText(
                if (amountValues[position].isNull()) {
                    ""
                } else {
                    amountValues[position]
                }
            )

            binding.txtPhoneNumberDebts.setOnClickListener {
                callBack.phoneClickListener(marketPaginationItem)
            }

            binding.cardGetMoney.setOnClickListener {
                checkPayDebt(marketPaginationItem, position)
            }

            binding.cardSendOf.setOnClickListener {
                callBack.debtOffClickListener(marketPaginationItem)
            }
        }

        private fun checkPayDebt(marketPaginationItem: MarketPaginationItem, position: Int){
            val amount = binding.edtDebtAmount.text.toString().toDoubleOrNull() ?: 0.0

            when{
                amount == 0.0 -> {
                    Toast.makeText(context, context.getString(R.string.enter_correct_amount), Toast.LENGTH_SHORT).show()
                }
                paymentTypesList.isEmpty() -> {
                    Toast.makeText(context, context.getString(R.string.select_payment_type), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val payDebt = Paydebt(
                        marketPaginationItem.id,
                        amount,
                        paymentTypesList[paymentTypePositionValues[position] ?: 0].name
                    )

                    callBack.payDebtClickListener(payDebt)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            KvitansiyaItemDebtsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        checkUpdateArraySize()
        holder.setItem(getItem(position)!!, position)
    }

    fun submitPaymentTypesList(paymentTypesList: List<PaymentTypesItem>){
        this.paymentTypesList = paymentTypesList
        notifyDataSetChanged()
    }

    fun checkUpdateArraySize(){
        if (amountValues.size != itemCount || paymentTypePositionValues.size != itemCount){
            amountValues = arrayOfNulls(itemCount)
            paymentTypePositionValues = arrayOfNulls(itemCount)
        }
    }

}