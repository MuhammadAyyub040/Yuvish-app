package com.example.yuvish.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.models.baseIndikatorsIndex.Data
import com.example.yuvish.R
import com.example.yuvish.databinding.WashedIndicatorGroupItemBinding

class WashedIndicatorGroupAdapter(
    private val context: Context
    ): PagingDataAdapter<Data, WashedIndicatorGroupAdapter.ItemHolder>(WashedIndicatorOrderDiffItemCallback()) {

    inner class ItemHolder(val binding: WashedIndicatorGroupItemBinding): RecyclerView.ViewHolder(binding.root){

        fun setItem(data: Data, position: Int){
            val productsIndicatorChildAdapter = ProductsIndicatorChildAdapter(context)
            productsIndicatorChildAdapter.submitList(data.products)
            binding.productsRV.adapter = productsIndicatorChildAdapter

            binding.ordinalNumber.text = "${position + 1}."
            binding.receiptNumber.text = data.order_nomer.toString()
            binding.customerName.text = data.costumer_name
            binding.employeesName.text = getEmployees(data.hodimlar)
            binding.totalProductCount.text = "${data.tovar_dona} ${context.getString(R.string.pcs)}"
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
            WashedIndicatorGroupItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setItem(getItem(position)!!, position)
    }

}

class WashedIndicatorOrderDiffItemCallback : DiffUtil.ItemCallback<Data>() {
    override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
        return oldItem == newItem
    }
}