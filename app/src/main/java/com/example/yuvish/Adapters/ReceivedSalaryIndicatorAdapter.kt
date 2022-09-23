package com.example.yuvish.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.BaseIndikatorsIndex.ReceivedSalaryIndicatorOrder
import com.example.yuvish.R
import com.example.yuvish.databinding.ReceivedSalaryIndicatorItemLayoutBinding

class ReceivedSalaryIndicatorAdapter(
    private val context: Context
): PagingDataAdapter<ReceivedSalaryIndicatorOrder, ReceivedSalaryIndicatorAdapter.ItemHolder>(ReceivedSalaryIndicatorOrderDiffItemCallback()) {

    inner class ItemHolder(val binding: ReceivedSalaryIndicatorItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        @SuppressLint("SetTextI18n")
        fun setItem(receivedSalaryIndicatorOrder: ReceivedSalaryIndicatorOrder, position: Int){
            binding.ordinalNumber.text = "${position + 1}."
            binding.name.text = receivedSalaryIndicatorOrder.kassachi
            binding.salary.text = "${context.getString(R.string.maosh)}: " +
                    "${receivedSalaryIndicatorOrder.summa} ${context.getString(R.string.so_m)}"
            binding.comment.text = receivedSalaryIndicatorOrder.izoh
            binding.date.text = getTransformDateAndTime(receivedSalaryIndicatorOrder.sana)
        }

        private fun getTransformDateAndTime(oldDate: String): String{
            val date = oldDate.substring(0, oldDate.indexOf("T"))
            val time = oldDate.substring(oldDate.indexOf("T") + 1)
            return "${transformDate(date)} $time"
        }

        private fun transformDate(oldDate: String): String{
            val dateArray = oldDate.split("-")
            return "${dateArray[2]}.${dateArray[1]}.${dateArray[0]}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            ReceivedSalaryIndicatorItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setItem(getItem(position)!!, position)
    }

}

class ReceivedSalaryIndicatorOrderDiffItemCallback : DiffUtil.ItemCallback<ReceivedSalaryIndicatorOrder>() {
    override fun areItemsTheSame(oldItem: ReceivedSalaryIndicatorOrder, newItem: ReceivedSalaryIndicatorOrder): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ReceivedSalaryIndicatorOrder, newItem: ReceivedSalaryIndicatorOrder): Boolean {
        return oldItem == newItem
    }
}