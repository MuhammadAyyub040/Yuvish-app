package com.example.yuvish.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.models.baseIndikatorsIndex.SubmittedIndicatorOrder
import com.example.yuvish.R
import com.example.yuvish.databinding.SubmittedIndicatorGroupItemLayoutBinding
import com.example.yuvish.models.baseIndikatorsIndex.Clean
import com.example.yuvish.models.baseIndikatorsIndex.IndicatorPaymentType
import com.example.yuvish.models.baseIndikatorsIndex.IndicatorProduct
import com.example.yuvish.retrofit.isNotNull
import com.example.yuvish.retrofit.isNull

class SubmittedIndicatorGroupAdapter(
    private val context: Context
    ): PagingDataAdapter<SubmittedIndicatorOrder, SubmittedIndicatorGroupAdapter.ItemHolder>(SubmittedIndicatorOrderDiffItemCallback()) {

    inner class ItemHolder(val binding: SubmittedIndicatorGroupItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        @SuppressLint("SetTextI18n")
        fun setItem(submittedIndicatorOrder: SubmittedIndicatorOrder, position: Int){
            val productsIndicatorChildAdapter = ProductsIndicatorChildAdapter(context)
            productsIndicatorChildAdapter.submitList(getIndicatorProducts(submittedIndicatorOrder.cleans))
            binding.productsRV.adapter = productsIndicatorChildAdapter

            val paymentTypesAdapter = PaymentTypesAdapter(context)
            paymentTypesAdapter.submitList(getIndicatorPaymentTypes(submittedIndicatorOrder))
            binding.paymentTypesRV.adapter = paymentTypesAdapter

            binding.ordinalNumber.text = "${position + 1}."
            binding.receiptNumber.text = submittedIndicatorOrder.nomer.toString()
            binding.customerName.text = submittedIndicatorOrder.custumer.costumer_name
            binding.employeesName.text = getEmployees(submittedIndicatorOrder.cleans)
            binding.totalProductCount.text = "${submittedIndicatorOrder.cleans.size} ${context.getString(
                R.string.pcs)}"
            binding.calculateAmount.text = "${submittedIndicatorOrder.order_last_price} " +
                    context.getString(R.string.so_m)
            binding.debtAmount.text =
                "${context.getString(R.string.qarz)}: ${submittedIndicatorOrder.nasiya_sum_summa} " +
                        context.getString(R.string.so_m)
            binding.debtOffAmount.text =
                "${context.getString(R.string.kechildi)}: ${submittedIndicatorOrder.kechildi_sum_summa} " +
                        context.getString(R.string.so_m)
        }

        private fun getEmployees(cleansList: List<Clean>): String {
            var employees = "${context.getString(R.string.employees)}: "

            cleansList.forEachIndexed { index, clean ->
                employees += if (index == 0){
                    clean.topshiruvchi?.fullname
                }else{
                    ", ${clean.topshiruvchi?.fullname}"
                }
            }

            return employees
        }

    }

    private fun getIndicatorPaymentTypes(submittedIndicatorOrder: SubmittedIndicatorOrder): List<IndicatorPaymentType>{
        val indicatorPaymentTypesList = ArrayList<IndicatorPaymentType>()

        if (submittedIndicatorOrder.naqd_sum_summa.isNotNull()){
            indicatorPaymentTypesList.add(
                IndicatorPaymentType(
                    submittedIndicatorOrder.naqd_sum_summa!!,
                    "naqd"
                )
            )
        }

        if (submittedIndicatorOrder.click_sum_summa.isNotNull()){
            indicatorPaymentTypesList.add(
                IndicatorPaymentType(
                    submittedIndicatorOrder.click_sum_summa!!,
                    "click"
                )
            )
        }

        if (submittedIndicatorOrder.terminal_sum_summa.isNotNull()){
            indicatorPaymentTypesList.add(
                IndicatorPaymentType(
                    submittedIndicatorOrder.terminal_sum_summa!!,
                    "Terminal-bank"
                )
            )
        }

        return indicatorPaymentTypesList
    }

    private fun getIndicatorProducts(cleansList: List<Clean>): List<IndicatorProduct>{
        val indicatorProducts = ArrayList<IndicatorProduct>()

        cleansList.forEach {
            indicatorProducts.add(
                IndicatorProduct(
                    it.id, // sonini qo'shish !!!               !!!!                  !!!!!
                    it.clean_hajm,
                    it.xizmat.xizmat_turi,
                    it.xizmat.olchov
                )
            )
        }

        return indicatorProducts
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