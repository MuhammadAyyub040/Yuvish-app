package com.example.yuvish.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.navigation.fragment.findNavController
import com.example.yuvish.Adapters.ProductsIndicatorChildAdapter
import com.example.yuvish.Adapters.ReceivedRewashIndicatorGroupAdapter
import com.example.yuvish.Models.BaseIndikatorsIndex.IndicatorProduct
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentRewashReceivedBinding
import com.example.yuvish.retrofit.isNull

class RewashReceivedFragment : Fragment() {

    private lateinit var binding: FragmentRewashReceivedBinding

    private val receivedRewashIndicatorGroupAdapter: ReceivedRewashIndicatorGroupAdapter by lazy {
        ReceivedRewashIndicatorGroupAdapter(requireActivity())
    }
    private val totalProductsAdapter: ProductsIndicatorChildAdapter by lazy {
        ProductsIndicatorChildAdapter(requireActivity())
    }

    private var totalIndicatorProductsList: List<IndicatorProduct>? = null
    private lateinit var fromDate: String
    private lateinit var toDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fromDate = arguments?.getString("fromDate")!!
        toDate = arguments?.getString("toDate")!!
//        initObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRewashReceivedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fromAndToDate.text = getString(R.string.between_rewashed, fromDate.transformDate(), toDate.transformDate())

        if (totalIndicatorProductsList.isNull()){
            totalIndicatorsPlaceHolderVisible(true)
        }else{
            totalIndicatorsPlaceHolderVisible(false)
            updateTotalIndicators(totalIndicatorProductsList!!)
        }

        binding.totalProductsRV.adapter = totalProductsAdapter
        binding.indicatorsRV.adapter = receivedRewashIndicatorGroupAdapter

        binding.backStack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun updateTotalIndicators(indicatorProductsList: List<IndicatorProduct>){
        binding.totalProductCount.text = "${calcTotalProductsCount(indicatorProductsList)} ${getString(R.string.pcs)}"
        totalProductsAdapter.submitList(indicatorProductsList)
    }

    private fun String.transformDate(): String{
        val dateArray = this.split("-")
        return "${dateArray[2]}.${dateArray[1]}.${dateArray[0]}"
    }

    private fun calcTotalProductsCount(indicatorProductsList: List<IndicatorProduct>): Int{
        var count = 0

        indicatorProductsList.forEach {
            count += it.dona
        }

        return count
    }

    private fun totalIndicatorsPlaceHolderVisible(visible: Boolean){

        binding.totalProductCount.isGone = visible
        binding.totalProductsRV.isGone = visible
    }

    private fun indicatorsPlaceHolderVisible(visible: Boolean){

        binding.indicatorsRV.isGone = visible
    }

}