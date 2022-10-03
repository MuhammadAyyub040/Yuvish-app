package com.example.yuvish.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.Registration.Item
import com.example.yuvish.Models.Registration.Product
import com.example.yuvish.R
import com.example.yuvish.databinding.ItemOlchamRegistrationBinding

class RegistrationAdapterChild(
    private val productList: List<Product>,
    private val measure: String) :
    RecyclerView.Adapter<RegistrationAdapterChild.RegistrationChildViewHolder>() {

    inner class RegistrationChildViewHolder(val binding: ItemOlchamRegistrationBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setItem(product: Product){
            val context = binding.root.context
            binding.txtMahsulotStatus.text = product.clean_status

            if (measure == "metr"){
                binding.txtMahsulotOlchamRegistration.text = "${product.gilam_eni} × ${product.gilam_boyi} = " +
                        "${product.clean_hajm} ${context.getString(R.string.m)}"
            }else{
                binding.txtMahsulotOlchamRegistration.text = "${product.clean_hajm} $measure"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegistrationChildViewHolder {
        return RegistrationChildViewHolder(ItemOlchamRegistrationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RegistrationChildViewHolder, position: Int) {
        holder.setItem(productList[position])
    }

    override fun getItemCount(): Int = productList.size
}