package com.example.yuvish.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.models.baseIndikatorsIndex.IndicatorPaymentType
import com.example.yuvish.R
import com.example.yuvish.databinding.PaymentTypeItemLayoutBinding

class PaymentTypesAdapter(
    private val context: Context
): RecyclerView.Adapter<PaymentTypesAdapter.ItemHolder>() {

    private var indicatorPaymentTypesList: List<IndicatorPaymentType> = emptyList()

    inner class ItemHolder(val binding: PaymentTypeItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        fun setItem(indicatorPaymentType: IndicatorPaymentType){
            binding.type.text = indicatorPaymentType.tolov_turi
            binding.amount.text = "${indicatorPaymentType.jami_sum} ${context.getString(R.string.so_m)}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            PaymentTypeItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setItem(getItem(position))
    }

    override fun getItemCount(): Int {
        return indicatorPaymentTypesList.size
    }

    private fun getItem(position: Int): IndicatorPaymentType {
        return indicatorPaymentTypesList[position]
    }

    fun submitList(indicatorPaymentTypesList: List<IndicatorPaymentType>){
        this.indicatorPaymentTypesList = indicatorPaymentTypesList
        notifyDataSetChanged()
    }

}