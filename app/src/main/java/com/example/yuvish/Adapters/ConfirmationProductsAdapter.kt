package com.example.yuvish.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.NewOrder.ConfirmProduct
import com.example.yuvish.R
import com.example.yuvish.databinding.ConfirmationProductItemLayoutBinding
import com.example.yuvish.retrofit.closeKeyboard
import com.example.yuvish.retrofit.isNull

class ConfirmationProductsAdapter(private val callBack: CallBack, private val context: Context
    ): ListAdapter<ConfirmProduct, ConfirmationProductsAdapter.ItemHolder>(ConfirmProductDiffItemCallback()) {

    interface CallBack{
        fun updateProductsCount()
    }

    inner class ItemHolder(val binding: ConfirmationProductItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        fun setItem(confirmProduct: ConfirmProduct, position: Int){
            binding.ordinalNumber.text = "${position+1}."
            binding.type.text = confirmProduct.xizmat.xizmat_turi
            binding.count.setText(confirmProduct.value.toString())

            binding.increment.setOnClickListener {
                binding.count.closeKeyboard(context)
                increment()
            }

            binding.decrement.setOnClickListener {
                binding.count.closeKeyboard(context)
                decrement()
            }

            binding.count.addTextChangedListener {
                val countStr = it.toString()
                val count = countStr.toIntOrNull() ?: 0
                
                if (count > 60){
                    binding.count.setText(countStr.trimEndChar())
                    binding.count.setSelection(countStr.length - 1)
                    Toast.makeText(context, context.getString(R.string.no_more_60_products), Toast.LENGTH_SHORT).show()
                }else {
                    confirmProduct.value = count
                    callBack.updateProductsCount()
                }
            }
        }

        private fun String.trimEndChar(): String{
            return if (this.isEmpty()){
                this
            }else {
                this.substring(0, this.length - 1)
            }
        }

        private fun increment() {
            val count = binding.count.text.toString().toIntOrNull()
            when {
                count.isNull() -> {
                    binding.count.setText("1")
                }
                count in 0..59 -> {
                    binding.count.setText(
                        "${count!! + 1}"
                    )
                }
            }
        }

        private fun decrement(){
            val count = binding.count.text.toString().toIntOrNull()
            when {
                count.isNull() -> {
                    binding.count.setText("0")
                }
                count in 1..60 -> {
                    binding.count.setText(
                        "${count!! - 1}"
                    )
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(ConfirmationProductItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setItem(getItem(position), position)
    }
}

class ConfirmProductDiffItemCallback : DiffUtil.ItemCallback<ConfirmProduct>() {
    override fun areItemsTheSame(oldItem: ConfirmProduct, newItem: ConfirmProduct): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ConfirmProduct, newItem: ConfirmProduct): Boolean {
        return oldItem == newItem
    }
}