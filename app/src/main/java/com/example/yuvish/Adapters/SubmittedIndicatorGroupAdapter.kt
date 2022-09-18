package com.example.yuvish.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.BaseIndikatorsIndex.SubmittedIndicatorOrder
import com.example.yuvish.R
import com.example.yuvish.databinding.SubmittedIndicatorGroupItemLayoutBinding

class SubmittedIndicatorGroupAdapter(
    private val context: Context
    ): PagingDataAdapter<SubmittedIndicatorOrder, SubmittedIndicatorGroupAdapter.ItemHolder>(SubmittedIndicatorOrderDiffItemCallback()) {

    inner class ItemHolder(val binding: SubmittedIndicatorGroupItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        @SuppressLint("SetTextI18n")
        fun setItem(submittedIndicatorOrder: SubmittedIndicatorOrder, position: Int){
            val productsIndicatorChildAdapter = ProductsIndicatorChildAdapter(context)
            productsIndicatorChildAdapter.submitList(submittedIndicatorOrder.products)
            binding.productsRV.adapter = productsIndicatorChildAdapter

            val paymentTypesAdapter = PaymentTypesAdapter(context)
            paymentTypesAdapter.submitList(submittedIndicatorOrder.tolovlar)
            binding.paymentTypesRV.adapter = paymentTypesAdapter

            binding.ordinalNumber.text = "${position + 1}."
            binding.receiptNumber.text = submittedIndicatorOrder.order_nomer.toString()
            binding.customerName.text = submittedIndicatorOrder.costumer_name
            binding.employeesName.text = getEmployees(submittedIndicatorOrder.hodimlar)
            binding.totalProductCount.text = "${submittedIndicatorOrder.tovar_dona} ${context.getString(
                R.string.pcs)}"
            binding.calculateAmount.text = "${submittedIndicatorOrder.hisoblangan_summa} " +
                    context.getString(R.string.so_m)
            binding.debtAmount.text =
                "${context.getString(R.string.qarz)}: ${submittedIndicatorOrder.qarz} " +
                        context.getString(R.string.so_m)
            binding.debtOffAmount.text =
                "${context.getString(R.string.kechildi)}: ${submittedIndicatorOrder.kechildi} " +
                        context.getString(R.string.so_m)
        }

        private fun getEmployees(employeesList: List<String>): String {
            var employees = "${context.getString(R.string.employees)}: "

            employeesList.forEachIndexed { index, name ->
                employees += if (index == 0){
                    name
                }else{
                    ", $name"
                }
            }

            return employees
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            SubmittedIndicatorGroupItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setItem(getItem(position)!!, position)
    }

}

class SubmittedIndicatorOrderDiffItemCallback : DiffUtil.ItemCallback<SubmittedIndicatorOrder>() {
    override fun areItemsTheSame(oldItem: SubmittedIndicatorOrder, newItem: SubmittedIndicatorOrder): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: SubmittedIndicatorOrder, newItem: SubmittedIndicatorOrder): Boolean {
        return oldItem == newItem
    }
}