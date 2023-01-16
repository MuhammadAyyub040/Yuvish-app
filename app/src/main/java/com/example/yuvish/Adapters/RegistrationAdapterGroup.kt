package com.example.yuvish.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.models.Registration.Product
import com.example.yuvish.R
import com.example.yuvish.databinding.ItemMahsulotlarNomiRegistrationBinding

class RegistrationAdapterGroup(
    private val initialExpandableState: Boolean
) : RecyclerView.Adapter<RegistrationAdapterGroup.RegistrationViewHolder>() {

    private var buyurtmalarList = ArrayList<Product>()

    inner class RegistrationViewHolder(val binding: ItemMahsulotlarNomiRegistrationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setItem(product: Product, position: Int) {
            val registrationAdapterChild =
                RegistrationAdapterChild(buyurtmalarList, product.xizmat.olchov)
            binding.rvProduct.adapter = registrationAdapterChild
            changeExpandableState(initialExpandableState)

            binding.cardNameRegistration.setOnClickListener {
                changeExpandableState()
            }

            binding.typeOrder.text = product.xizmat.xizmat_turi
            binding.txtNumber.text = "${position + 1}."
            binding.parametersRegistration.text = getParameters(binding.root.context,)
        }

        private fun changeExpandableState() {
            binding.rvProduct.isVisible = !binding.rvProduct.isVisible
            binding.imageSubmit.setImageResource(
                getRecourseByState(binding.rvProduct.isVisible)
            )
        }

        private fun changeExpandableState(state: Boolean) {
            binding.rvProduct.isVisible = state
            binding.imageSubmit.setImageResource(
                getRecourseByState(state)
            )
        }

        private fun getParameters(context: Context): String {
            return "${buyurtmalarList.size} ${context.getString(R.string.ta)}"
        }

        private fun getRecourseByState(state: Boolean): Int {
            return if (state) R.drawable.ic_keyboard_arrow else R.drawable.ic_arrow_down
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegistrationViewHolder {
        return RegistrationViewHolder(
            ItemMahsulotlarNomiRegistrationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RegistrationViewHolder, position: Int) {
        holder.setItem(buyurtmalarList[position], position)
    }

    override fun getItemCount(): Int = buyurtmalarList.size


    fun setData(list: List<Product>) {
        buyurtmalarList = list as ArrayList<Product>
        notifyDataSetChanged()
    }
}