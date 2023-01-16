package com.example.yuvish.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.R
import com.example.yuvish.databinding.ProductIndicatorChildItemLayoutBinding
import com.example.yuvish.models.baseIndikatorsIndex.IndicatorProduct

class ProductsRewashedIndicatorChildAdapter(
    private val context: Context
    ): RecyclerView.Adapter<ProductsRewashedIndicatorChildAdapter.ItemHolder>() {

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
                    "${indicatorProdact.soni} ${context.getString(R.string.pcs)} / " +
                            "${indicatorProdact.xajmi} ${context.getString(R.string.m)}"
                }
                "m" -> {
                    "${indicatorProdact.soni} ${context.getString(R.string.pcs)} / " +
                            "${indicatorProdact.xajmi} ${context.getString(R.string.meter)}"
                }
                "dona" -> {
                    "${indicatorProdact.soni} ${context.getString(R.string.pcs)}"
                }
                else -> {
                    "${indicatorProdact.soni} ${context.getString(R.string.pcs)} / " +
                            "${indicatorProdact.xajmi} ${indicatorProdact.olchov}"
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