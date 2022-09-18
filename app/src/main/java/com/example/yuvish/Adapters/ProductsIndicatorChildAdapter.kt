package com.example.yuvish.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.BaseIndikatorsIndex.IndicatorProduct
import com.example.yuvish.R
import com.example.yuvish.databinding.ProductIndicatorChildItemLayoutBinding

class ProductsIndicatorChildAdapter(
    private val context: Context
    ): RecyclerView.Adapter<ProductsIndicatorChildAdapter.ItemHolder>() {

    private var indicatorProductsList: List<IndicatorProduct> = emptyList()

    inner class ItemHolder(val binding: ProductIndicatorChildItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        fun setItem(indicatorProduct: IndicatorProduct, position: Int){
            binding.ordinalNumber.text = "${position + 1}."
            binding.type.text = indicatorProduct.name
            binding.count.text = getParameters(indicatorProduct)
        }

        private fun getParameters(indicatorProduct: IndicatorProduct): String {
            return when (indicatorProduct.olchov) {
                "metr" -> {
                    "${indicatorProduct.dona} ${context.getString(R.string.pcs)} / " +
                            "${indicatorProduct.hajm} ${context.getString(R.string.m)}"
                }
                "m" -> {
                    "${indicatorProduct.dona} ${context.getString(R.string.pcs)} / " +
                            "${indicatorProduct.hajm} ${context.getString(R.string.meter)}"
                }
                "dona" -> {
                    "${indicatorProduct.dona} ${context.getString(R.string.pcs)}"
                }
                else -> {
                    "${indicatorProduct.dona} ${context.getString(R.string.pcs)} / " +
                            "${indicatorProduct.hajm} ${indicatorProduct.olchov}"
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
        return indicatorProductsList.size
    }

    private fun getItem(position: Int): IndicatorProduct {
        return indicatorProductsList[position]
    }

    fun submitList(indicatorProductsList: List<IndicatorProduct>){
        this.indicatorProductsList = indicatorProductsList
        notifyDataSetChanged()
    }

}