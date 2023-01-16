package com.example.yuvish.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.models.NewOrder.ConfirmProduct
import com.example.yuvish.R
import com.example.yuvish.databinding.ConfirmedProductItemLayoutBinding

class ConfirmedProductsAdapter(
    private val context: Context
    ): ListAdapter<ConfirmProduct, ConfirmedProductsAdapter.ItemHolder>(ConfirmProductDiffItemCallback()) {

    inner class ItemHolder(val binding: ConfirmedProductItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        fun setItem(confirmProduct: ConfirmProduct, position: Int){
            binding.ordinalNumber.text = "${position + 1}."
            binding.type.text = confirmProduct.xizmat.xizmat_turi
            binding.count.text = "${confirmProduct.value} ${context.getString(R.string.pcs)}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            ConfirmedProductItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setItem(getItem(position), position)
    }

}