package com.example.yuvish.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.navigation.fragment.findNavController
import com.example.yuvish.Adapters.ProductsIndicatorChildAdapter
import com.example.yuvish.Adapters.SubmittedIndicatorGroupAdapter
import com.example.yuvish.Models.BaseIndikatorsIndex.IndicatorProduct
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentSubmittedIndicatorsBinding

class SubmittedIndicatorsFragment : Fragment() {

    lateinit var binding: FragmentSubmittedIndicatorsBinding
    private val submittedIndicatorGroupAdapter: SubmittedIndicatorGroupAdapter by lazy {
        SubmittedIndicatorGroupAdapter(requireActivity()) }
    private val totalProductsAdapter: ProductsIndicatorChildAdapter by lazy {
        ProductsIndicatorChildAdapter(requireActivity()) }

    private var totalIndicatorProductsList: List<IndicatorProduct>? = null
    private lateinit var fromDate: String
    private lateinit var toDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        fromDate = arguments?.getString("fromDate")!!
//        toDate = arguments?.getString("toDate")!!
//        initObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubmittedIndicatorsBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.fromAndToDate.text = getString(R.string.between_submitted, fromDate.transformDate(), toDate.transformDate())

        binding.totalProductsRV.adapter = totalProductsAdapter
        binding.indicatorsRV.adapter = submittedIndicatorGroupAdapter

        submittedIndicatorGroupAdapter.addLoadStateListener {
//            loadStateManager(it)
        }

        binding.backStack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

//    private fun loadStateManager(it: CombinedLoadStates){
//        when(val refreshState = it.refresh){
//            is LoadState.Loading -> {
//                indicatorsPlaceHolderVisible(true)
//            }
//            is LoadState.NotLoading -> {
//                indicatorsPlaceHolderVisible(false)
//            }
//            is LoadState.Error -> {
//                connectivityManager.checkConnectionError(refreshState.error, "entryData")
//            }
//        }
//
//        when(val appendState = it.append){
//            is LoadState.Loading -> {}
//            is LoadState.NotLoading -> {}
//            is LoadState.Error -> {
//                connectivityManager.checkConnectionError(appendState.error, "entryData")
//            }
//        }
//    }

    private fun updateTotalIndicators(totalIndicatorProductsList: List<IndicatorProduct>){
        binding.totalProductCount.text = "${calcTotalProductsCount(totalIndicatorProductsList)} ${getString(R.string.pcs)}"
        totalProductsAdapter.submitList(totalIndicatorProductsList)
    }

    private fun calcTotalProductsCount(indicatorProductsList: List<IndicatorProduct>): Int{
        var count = 0

        indicatorProductsList.forEach {
            count += it.dona
        }

        return count
    }

    private fun indicatorsPlaceHolderVisible(visible: Boolean){
        binding.indicatorsRV.isGone = visible
    }

    private fun String.transformDate(): String{
        val dateArray = this.split("-")
        return "${dateArray[2]}.${dateArray[1]}.${dateArray[0]}"
    }
}