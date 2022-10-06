package com.example.yuvish.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.globalSearch.ButtonParams
import com.example.yuvish.databinding.SearchReceiptButtonsItemLayoutBinding

class SearchReceiptButtonsAdapter(
    private val buttonParamsList: List<ButtonParams>,
    private val callBack: CallBack
): RecyclerView.Adapter<SearchReceiptButtonsAdapter.ItemHolder>() {

    interface CallBack{
        fun buttonClickListener(id: Int)
    }

    inner class ItemHolder(val binding: SearchReceiptButtonsItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        fun setItem(buttonParams: ButtonParams, position: Int){
            binding.icon.setImageResource(buttonParams.icon)
            binding.title.text = buttonParams.title

            binding.buttonRoot.setOnClickListener {
                callBack.buttonClickListener(buttonParams.id)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            SearchReceiptButtonsItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setItem(getItem(position), position)
    }

    fun getItem(position: Int): ButtonParams {
        return buttonParamsList[position]
    }

    override fun getItemCount(): Int {
        return buttonParamsList.size
    }

}