package com.example.yuvish.Adapters

import android.content.Context
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvish.Models.NewOrder.ProductSize
import com.example.yuvish.Models.NewOrder.SizingProduct
import com.example.yuvish.R
import com.example.yuvish.databinding.ItemGilamBinding
import com.example.yuvish.databinding.NotsizedproductfirstitemBinding
import com.example.yuvish.databinding.NotsizedproductseconditemBinding
import com.example.yuvish.retrofit.GlobalData
import com.example.yuvish.retrofit.isNull
import java.text.DecimalFormat

class RegistrationAdapter(private val callBack: CallBack): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var sizingProductsList: ArrayList<SizingProduct> = ArrayList()

    private var volumeValues = arrayOfNulls<String>(itemCount)
    private var priceValues = arrayOfNulls<String>(itemCount)
    private var widthValues = arrayOfNulls<String>(itemCount)
    private var heightValues = arrayOfNulls<String>(itemCount)

    interface CallBack{
        fun washedClickListener(productId: Int, productSize: ProductSize)
        fun printClickListener(sizingProduct: SizingProduct)
    }

    inner class FirstHolder(val binding: ItemGilamBinding): RecyclerView.ViewHolder(binding.root){

        init {
            binding.price.addTextChangedListener {
                priceValues[binding.price.tag as Int] = it.toString()
            }

            binding.width.addTextChangedListener {
                widthValues[binding.width.tag as Int] = it.toString()
                calculateVolume()
            }

            binding.height.addTextChangedListener {
                heightValues[binding.height.tag as Int] = it.toString()
                calculateVolume()
            }
        }

        fun setItem(sizingProduct: SizingProduct, position: Int){
            Log.e(TAG, "setItem: ${sizingProductsList.size}")
            binding.typeService.text = sizingProduct.xizmat.xizmat_turi
            binding.price.isFocusable = getPriceChangePermission()
            binding.print.isVisible = getPrintPermission() && sizingProduct.barcode != "0"

            binding.price.tag = position
            binding.width.tag = position
            binding.height.tag = position

            binding.price.setText(
                if (priceValues[position].isNull()) {
                    sizingProduct.narx.toString()
                } else {
                    priceValues[position]
                }
            )

            binding.width.setText(
                if (widthValues[position].isNull()) {
                    ""
                } else {
                    widthValues[position]
                }
            )

            binding.height.setText(
                if (heightValues[position].isNull()) {
                    ""
                } else {
                    heightValues[position]
                }
            )

            binding.sized.setOnClickListener {
                finishSizing(sizingProduct)
            }

            binding.print.setOnClickListener {
                callBack.printClickListener(sizingProduct)
            }
        }

        private fun finishSizing(sizingProduct: SizingProduct){
            val context = binding.root.context
            val width = binding.width.text.toString().toDoubleOrNull() ?: 0.0
            val height = binding.height.text.toString().toDoubleOrNull() ?: 0.0
            val volume = binding.volume.text.toString().toDoubleOrNull() ?: 0.0
            val price = binding.price.text.toString().toIntOrNull() ?: 0

            if (volume == 0.0){
                Toast.makeText(context, context.getString(R.string.enter_correct_product_width_height), Toast.LENGTH_SHORT).show()
            }else if (price < sizingProduct.xizmat.min_narx){
                Toast.makeText(
                    context
                    ,context.getString(R.string.price_must_greater_than_minimum_price)
                        .replace("#", sizingProduct.xizmat.min_narx.toString())
                    , Toast.LENGTH_SHORT
                ).show()
            }else{
                val productSize = ProductSize(sizingProduct.narx, width, height, volume)
                callBack.washedClickListener(sizingProduct.id, productSize)
            }
        }

        private fun calculateVolume(){
            val width = binding.width.text.toString().toDoubleOrNull() ?: 0.0
            val height = binding.height.text.toString().toDoubleOrNull() ?: 0.0
            val volume = width * height
            binding.volume.setText(getStringDecimalFormat(volume))
        }

        private fun getStringDecimalFormat(it: Double): String{
            return DecimalFormat("#.#").format(it).replace(",", ".")
        }

    }

    inner class SecondaryHolder(val binding: NotsizedproductseconditemBinding): RecyclerView.ViewHolder(binding.root){

        init {
            binding.volume.addTextChangedListener {
                volumeValues[binding.volume.tag as Int] = it.toString()
            }

            binding.price.addTextChangedListener {
                priceValues[binding.price.tag as Int] = it.toString()
            }
        }

        fun setItem(sizingProduct: SizingProduct, position: Int){
            Log.e(TAG, "setItem: ${sizingProductsList.size}")
            binding.type.text = sizingProduct.xizmat.xizmat_turi
            binding.price.isFocusable = getPriceChangePermission()
            binding.print.isVisible = (sizingProduct.barcode != "0")
            binding.volumeLayout.suffixText = sizingProduct.xizmat.olchov
            binding.volumeCard.visibility = getVolumeVisibilityByMeasure(sizingProduct.xizmat.olchov)

            binding.volume.tag = position
            binding.price.tag = position

            binding.price.setText(
                if (priceValues[position].isNull()) {
                    sizingProduct.narx.toString()
                } else {
                    priceValues[position]
                }
            )

            binding.volume.setText(
                if (volumeValues[position].isNull()) {
                    ""
                } else {
                    volumeValues[position]
                }
            )

            binding.sized.setOnClickListener {
                finishSizing(sizingProduct, binding.root.context)
            }

            binding.print.setOnClickListener {
                callBack.printClickListener(sizingProduct)
            }
        }

        private fun finishSizing(sizingProduct: SizingProduct, context: Context){
            val volume = binding.volume.text.toString().toDoubleOrNull() ?: 0.0
            val price = binding.price.text.toString().toIntOrNull() ?: 0

            if (binding.volumeCard.isVisible && volume == 0.0){
                Toast.makeText(context, context.getString(R.string.enter_correct_product_volume), Toast.LENGTH_SHORT).show()
            }else if (price < sizingProduct.xizmat.min_narx){
                Toast.makeText(
                    context
                    ,context.getString(R.string.price_must_greater_than_minimum_price)
                        .replace("#", sizingProduct.xizmat.min_narx.toString())
                    , Toast.LENGTH_SHORT
                ).show()
            }else {
                val productSize = ProductSize(sizingProduct.narx, 0.0, 0.0, volume)
                callBack.washedClickListener(sizingProduct.id, productSize)
            }
        }

        private fun getVolumeVisibilityByMeasure(measure: String): Int{
            return if (measure == "dona"){
                View.INVISIBLE
            }else{
                View.VISIBLE
            }
        }

    }

    private fun getPrintPermission(): Boolean{
        return GlobalData.commonSettings?.barcode == 1
    }

    private fun getPriceChangePermission(): Boolean{
        return GlobalData.commonSettings!!.changing_coast == 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 0){
            (holder as FirstHolder).setItem(getItem(position), position)
        }else{
            (holder as SecondaryHolder).setItem(getItem(position), position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).xizmat.olchov == "metr"){
            0
        }else{
            1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            FirstHolder(
                ItemGilamBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        } else {
            SecondaryHolder(
                NotsizedproductseconditemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return sizingProductsList.size
    }

    private fun getItem(position: Int): SizingProduct {
        return sizingProductsList[position]
    }

    fun addItem(sizingProduct: SizingProduct){
        sizingProductsList.add(sizingProduct)
        notifyDataSetChanged()
    }

    fun submitList(sizingProductsList: ArrayList<SizingProduct>){
        this.sizingProductsList = sizingProductsList

        volumeValues = arrayOfNulls(itemCount)
        priceValues = arrayOfNulls(itemCount)
        widthValues = arrayOfNulls(itemCount)
        heightValues = arrayOfNulls(itemCount)

        notifyDataSetChanged()
    }

}