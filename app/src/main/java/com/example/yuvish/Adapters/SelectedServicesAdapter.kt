package com.example.yuvish.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.NewOrder.SelectedService
import com.example.yuvish.R
import com.example.yuvish.databinding.SelectedServiceItemLayoutBinding

class SelectedServicesAdapter(
    private val selectedServicesList: ArrayList<SelectedService>,
    private val callBack: CallBack,
    private val context: Context
    ): RecyclerView.Adapter<SelectedServicesAdapter.ItemHolder>() {

    interface CallBack{
        fun deleteClickListener(position: Int)
    }

    inner class ItemHolder(private val binding: SelectedServiceItemLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun setItem(selectedService: SelectedService, position: Int){
            binding.ordinalNumber.text = "${position + 1}."
            binding.type.text = selectedService.service.xizmat_turi
            binding.count.text = "${selectedService.count} ${context.getString(R.string.pcs)}"

            binding.delete.setOnClickListener {
                callBack.deleteClickListener(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(SelectedServiceItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setItem(selectedServicesList[position], position)
    }

    override fun getItemCount(): Int {
        return selectedServicesList.size
    }
}