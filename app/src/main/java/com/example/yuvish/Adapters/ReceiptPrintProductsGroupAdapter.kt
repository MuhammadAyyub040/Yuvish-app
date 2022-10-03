package com.example.yuvish.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.ArrangedSubmit.Buyurtmalar
import com.example.yuvish.R
import com.example.yuvish.databinding.ReceiptPrintProductsGroupItemLayoutBinding

class ReceiptPrintProductsGroupAdapter(
    private val typeOrdersList: List<Buyurtmalar>,
    private val context: Context
): RecyclerView.Adapter<ReceiptPrintProductsGroupAdapter.ItemHolder>() {

    inner class ItemHolder(val binding: ReceiptPrintProductsGroupItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        @SuppressLint("SetTextI18n")
        fun setItem(buyurtmalar: Buyurtmalar){
            val receiptPrintProductsChildAdapter =
                ReceiptPrintProductsChildAdapter(buyurtmalar.products, buyurtmalar.olchov, context)
            binding.childRV.adapter = receiptPrintProductsChildAdapter

            binding.service.text = buyurtmalar.name
            binding.count.text = "${buyurtmalar.count} ${context.getString(R.string.pcs)}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            ReceiptPrintProductsGroupItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setItem(typeOrdersList[position])
    }

    override fun getItemCount(): Int {
        return typeOrdersList.size
    }

}