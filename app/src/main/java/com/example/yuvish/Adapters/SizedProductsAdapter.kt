package com.example.yuvish.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.models.NewOrder.SizingProduct
import com.example.yuvish.R
import com.example.yuvish.databinding.SizedProductItemLayoutBinding

class SizedProductsAdapter(
    private val context: Context
    ): ListAdapter<SizingProduct, SizedProductsAdapter.ItemHolder>(SizingProductDiffItemCallback()) {

    inner class ItemHolder(val binding: SizedProductItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        fun setItem(sizingProduct: SizingProduct){
            binding.type.text = sizingProduct.xizmat.xizmat_turi
            binding.volume.text = getVolumeByMeasure(sizingProduct.clean_hajm, sizingProduct.xizmat.olchov)
            binding.width.text = getDimensionsByMeasure(sizingProduct.gilam_eni, sizingProduct.xizmat.olchov)
            binding.height.text = getDimensionsByMeasure(sizingProduct.gilam_boyi, sizingProduct.xizmat.olchov)
        }

        private fun getVolumeByMeasure(volume: Double, measure: String): String{
            return if (measure == "metr"){
                "$volume ${context.getString(R.string.m)}"
            }else{
                "$volume $measure"
            }
        }

        private fun getDimensionsByMeasure(dimension: Double, measure: String): String{
            return if (measure == "metr"){
                "$dimension ${context.getString(R.string.meter)}"
            }else{
                "-"
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            SizedProductItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setItem(getItem(position))
    }

}

class SizingProductDiffItemCallback : DiffUtil.ItemCallback<SizingProduct>() {
    override fun areItemsTheSame(oldItem: SizingProduct, newItem: SizingProduct): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: SizingProduct, newItem: SizingProduct): Boolean {
        return oldItem == newItem
    }
}