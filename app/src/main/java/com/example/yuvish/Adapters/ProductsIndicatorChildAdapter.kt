package com.example.yuvish.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.BaseIndikatorsIndex.IndicatorProduct
import com.example.yuvish.Models.BaseIndikatorsIndex.JamiTableFooter
import com.example.yuvish.R
import com.example.yuvish.databinding.ProductIndicatorChildItemLayoutBinding

class ProductsIndicatorChildAdapter(
    private val context: Context
    ): RecyclerView.Adapter<ProductsIndicatorChildAdapter.ItemHolder>() {

    private var productsList: List<IndicatorProduct> = emptyList()

    inner class ItemHolder(val binding: ProductIndicatorChildItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        fun setItem(indicatorProdact: IndicatorProduct, position: Int){
            binding.ordinalNumber.text = "${position + 1}."
            binding.type.text = indicatorProdact.name
            binding.count.text = getParameters(indicatorProdact)
        }

        private fun getParameters(indicatorProdact: IndicatorProduct): String {
            return when (indicatorProdact.olchov) {
                "metr" -> {
                    "${indicatorProdact.dona} ${context.getString(R.string.pcs)} / " +
                            "${indicatorProdact.hajm} ${context.getString(R.string.m)}"
                }
                "m" -> {
                    "${indicatorProdact.dona} ${context.getString(R.string.pcs)} / " +
                            "${indicatorProdact.hajm} ${context.getString(R.string.meter)}"
                }
                "dona" -> {
                    "${indicatorProdact.dona} ${context.getString(R.string.pcs)}"
                }
                else -> {
                    "${indicatorProdact.dona} ${context.getString(R.string.pcs)} / " +
                            "${indicatorProdact.hajm} ${indicatorProdact.olchov}"
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            ProductIndicatorChildItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setItem(getItem(position), position)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    private fun getItem(position: Int): IndicatorProduct {
        return productsList[position]
    }

    fun submitList(productsList: List<IndicatorProduct>){
        this.productsList = productsList
        notifyDataSetChanged()
    }

}