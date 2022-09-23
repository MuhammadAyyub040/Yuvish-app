package com.example.yuvish.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.BaseIndikatorsIndex.KpiIndicatorOrder
import com.example.yuvish.R
import com.example.yuvish.databinding.KpiIndicatorItemLayoutBinding

class KpiIndicatorAdapter(
    private val context: Context
    ): PagingDataAdapter<KpiIndicatorOrder, KpiIndicatorAdapter.ItemHolder>(KpiIndicatorOrderDiffItemCallback()) {

    inner class ItemHolder(val binding: KpiIndicatorItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        @SuppressLint("SetTextI18n")
        fun setItem(kpiIndicatorOrder: KpiIndicatorOrder, position: Int){
            binding.ordinalNumber.text = "${position + 1}."
            binding.name.text = kpiIndicatorOrder.user.fullname
            binding.salary.text = "${context.getString(R.string.maosh)}: " +
                    "${kpiIndicatorOrder.maosh} ${context.getString(R.string.so_m)}"
            binding.time.text = "${context.getString(R.string.gone_time)}: ${kpiIndicatorOrder.ketdi_time}"
            binding.date.text = transformDate(kpiIndicatorOrder.sana)
        }

        private fun transformDate(oldDate: String): String{
            val dateArray = oldDate.split("-")
            return "${dateArray[2]}.${dateArray[1]}.${dateArray[0]}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            KpiIndicatorItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setItem(getItem(position)!!, position)
    }

}

class KpiIndicatorOrderDiffItemCallback : DiffUtil.ItemCallback<KpiIndicatorOrder>() {
    override fun areItemsTheSame(oldItem: KpiIndicatorOrder, newItem: KpiIndicatorOrder): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: KpiIndicatorOrder, newItem: KpiIndicatorOrder): Boolean {
        return oldItem == newItem
    }
}