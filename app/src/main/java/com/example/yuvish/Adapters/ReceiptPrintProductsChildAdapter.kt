package com.example.yuvish.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.ArrangedSubmit.Product
import com.example.yuvish.R
import com.example.yuvish.databinding.ReceiptPrintProductsChildItemLayoutBinding

class ReceiptPrintProductsChildAdapter(
    private val typeProductsList: List<Product>,
    private val measure: String,
    private val context: Context
): RecyclerView.Adapter<ReceiptPrintProductsChildAdapter.ItemHolder>() {

    inner class ItemHolder(val binding: ReceiptPrintProductsChildItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        @SuppressLint("SetTextI18n")
        fun setItem(product: Product, position: Int){
            binding.ordinalNumber.text = "${position + 1}."
            binding.price.text = "${product.summa} ${context.getString(R.string.so_m)}"
            binding.onePrice.text = "x ${product.narx} ${context.getString(R.string.so_m)}"

            binding.size.text = when (measure) {
                "metr" -> {
                    "${product.gilam_boyi} × ${product.gilam_eni} = " +
                            "${product.clean_hajm} ${context.getString(R.string.m)}"
                }
                "m" -> {
                    "${product.clean_hajm} ${context.getString(R.string.meter)}"
                }
                else -> {
                    "${product.clean_hajm} $measure"
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            ReceiptPrintProductsChildItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setItem(typeProductsList[position], position)
    }

    override fun getItemCount(): Int {
        return typeProductsList.size
    }

}