package com.example.yuvish.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.ArrangedSubmit.Buyurtmalar
import com.example.yuvish.R
import com.example.yuvish.databinding.ItemMahsulotlarNomiBinding

class SubmitAdapterGroup(
    private val caLLBack: SubmitAdapterChild.CaLLBack,
    private val initialExpandableState: Boolean) : RecyclerView.Adapter<SubmitAdapterGroup.SubmitViewHolder>() {

    private var buyurtmalarList = ArrayList<Buyurtmalar>()

    inner class SubmitViewHolder(val binding: ItemMahsulotlarNomiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setItem(buyurtmalar: Buyurtmalar, position: Int) {
            val submitAdapterChild =
                SubmitAdapterChild(buyurtmalar.products, buyurtmalar.olchov, caLLBack)
            binding.rvProduct.adapter = submitAdapterChild
            changeExpandableState(initialExpandableState)

            binding.cardName.setOnClickListener {
                changeExpandableState()
            }

            binding.typeOrder.text = buyurtmalar.name
            binding.txtNumber.text = "${position + 1}."
            binding.parameters.text = getParameters(binding.root.context, buyurtmalar)
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

        private fun getParameters(context: Context, buyurtmalar: Buyurtmalar): String {
            return "${buyurtmalar.count} ${context.getString(R.string.ta)} / " +
                    "${buyurtmalar.hajm} ${
                        if (buyurtmalar.olchov == "metr") context.getString(R.string.m)
                        else buyurtmalar.olchov
                    } / ${buyurtmalar.summa} ${context.getString(R.string.so_m)}"
        }

        private fun getRecourseByState(state: Boolean): Int {
            return if (state) R.drawable.ic_keyboard_arrow else R.drawable.ic_arrow_down
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubmitViewHolder {
        return SubmitViewHolder(
            ItemMahsulotlarNomiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SubmitViewHolder, position: Int) {
        Log.e(TAG, "onBindViewHolder: ${buyurtmalarList[position].id}")
        holder.setItem(buyurtmalarList[position], position)
    }

    override fun getItemCount(): Int = buyurtmalarList.size

    private val TAG = "SubmitAdapter"

    fun setData(list: List<Buyurtmalar>) {
        Log.d(TAG, "setData: ${list.size}")
        buyurtmalarList = list as ArrayList<Buyurtmalar>
        notifyDataSetChanged()
    }
}