package com.example.yuvish.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.ArrangedSubmit.Product
import com.example.yuvish.R
import com.example.yuvish.databinding.ItemOlchamBinding

class SubmitAdapterChild(
    private val productList: List<Product>,
    private val measure: String,
    private val callBack: CaLLBack) :
    RecyclerView.Adapter<SubmitAdapterChild.SubmitChildViewHolder>() {

    interface CaLLBack{
        fun rewashClickListener(product: Product)
    }

    inner class SubmitChildViewHolder(val binding: ItemOlchamBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setItem(product: Product){
            val context = binding.root.context
            binding.txtMahsulotNarx.text = "${product.narx} ${context.getString(R.string.so_m)}"
            binding.txtMahsulotSumma.text = "${product.summa} ${context.getString(R.string.so_m)}"
            binding.txtMahsulotTokcha.text = product.joy

            if (measure == "metr"){
                binding.txtMahsulotOlcham.text = "${product.gilam_eni} × ${product.gilam_boyi} = " +
                        "${product.clean_hajm} ${context.getString(R.string.m)}"
            }else{
                binding.txtMahsulotOlcham.text = "${product.clean_hajm} $measure"
            }
            binding.cardRefresh.setOnClickListener {
                callBack.rewashClickListener(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubmitChildViewHolder {
        return SubmitChildViewHolder(ItemOlchamBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SubmitChildViewHolder, position: Int) {
        holder.setItem(productList[position])
    }

    override fun getItemCount(): Int = productList.size
}