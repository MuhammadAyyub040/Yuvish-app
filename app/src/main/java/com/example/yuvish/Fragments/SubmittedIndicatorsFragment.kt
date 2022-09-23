package com.example.yuvish.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.navigation.fragment.findNavController
import com.example.yuvish.Adapters.ProductsIndicatorChildAdapter
import com.example.yuvish.Adapters.SubmittedIndicatorGroupAdapter
import com.example.yuvish.Models.BaseIndikatorsIndex.IndicatorProduct
import com.example.yuvish.Models.BaseIndikatorsIndex.SubmittedIndicator
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentSubmittedIndicatorsBinding
import com.example.yuvish.retrofit.ApiClient
import com.example.yuvish.retrofit.isNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubmittedIndicatorsFragment : Fragment() {

    lateinit var binding: FragmentSubmittedIndicatorsBinding
    lateinit var submittedIndicator: SubmittedIndicator
    private val submittedIndicatorGroupAdapter: SubmittedIndicatorGroupAdapter by lazy {
        SubmittedIndicatorGroupAdapter(requireActivity()) }
    private val totalProductsAdapter: ProductsIndicatorChildAdapter by lazy {
        ProductsIndicatorChildAdapter(requireActivity()) }

    private var totalIndicatorProductsList: List<IndicatorProduct>? = null
    private lateinit var fromDate: String
    private lateinit var toDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fromDate = arguments?.getString("fromDate")!!
        toDate = arguments?.getString("toDate")!!
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

        binding.fromAndToDate.text = getString(R.string.between_submitted, fromDate.transformDate(), toDate.transformDate())

        if (totalIndicatorProductsList.isNull()){
            getSubmitted(fromDate, toDate, 1)
        }else{
            updateTotalIndicators(totalIndicatorProductsList!!)
        }

        binding.totalProductsRV.adapter = totalProductsAdapter
        binding.indicatorsRV.adapter = submittedIndicatorGroupAdapter

        binding.backStack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

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

    private fun String.transformDate(): String{
        val dateArray = this.split("-")
        return "${dateArray[2]}.${dateArray[1]}.${dateArray[0]}"
    }

    private fun getSubmitted(fromDate: String, toDate: String, page: Int){
        ApiClient.retrofitService.getSubmittedIndicators(fromDate, toDate, page).enqueue(object : Callback<SubmittedIndicator>{
            override fun onResponse(call: Call<SubmittedIndicator>, response: Response<SubmittedIndicator>) {
                if (response.code() == 200){

                }
            }

            override fun onFailure(call: Call<SubmittedIndicator>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Server bilan bog'lanishda xatolik", Toast.LENGTH_SHORT).show()
            }

        })
    }
}