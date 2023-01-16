package com.example.yuvish.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.yuvish.Adapters.ProductsRewashedIndicatorChildAdapter
import com.example.yuvish.Adapters.ReceivedRewashIndicatorGroupAdapter
import com.example.yuvish.models.baseIndikatorsIndex.ReceivedRewashIndicator
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentRewashReceivedBinding
import com.example.yuvish.models.baseIndikatorsIndex.IndicatorProduct
import com.example.yuvish.retrofit.ApiClient
import com.example.yuvish.retrofit.isNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RewashReceivedFragment : Fragment() {

    private lateinit var binding: FragmentRewashReceivedBinding

    private val receivedRewashIndicatorGroupAdapter: ReceivedRewashIndicatorGroupAdapter by lazy {
        ReceivedRewashIndicatorGroupAdapter(requireActivity())
    }
    private val totalProductsAdapter: ProductsRewashedIndicatorChildAdapter by lazy {
        ProductsRewashedIndicatorChildAdapter(requireActivity())
    }

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
        binding = FragmentRewashReceivedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fromAndToDate.text = getString(R.string.between_rewashed, fromDate.transformDate(), toDate.transformDate())

        if (totalIndicatorProductsList.isNull()){
            getReceivedRewashIndicators(fromDate, toDate, 1)
        }else{
            updateTotalIndicators(totalIndicatorProductsList!!)
        }

        binding.totalRewashProductsRV.adapter = totalProductsAdapter
        binding.indicatorsRewashRV.adapter = receivedRewashIndicatorGroupAdapter

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
//            count += it.dona
        }

        return count
    }

    private fun getReceivedRewashIndicators(fromDate: String, toDate: String, page: Int){
        ApiClient.retrofitService.getReceivedRewashIndicator(fromDate, toDate, page).enqueue(object : Callback<ReceivedRewashIndicator>{
            override fun onResponse(call: Call<ReceivedRewashIndicator>, response: Response<ReceivedRewashIndicator>) {
                if (response.code() == 200){
                    totalIndicatorProductsList = response.body()!!.jami_table_footer
                    updateTotalIndicators(totalIndicatorProductsList!!)
                }
            }

            override fun onFailure(call: Call<ReceivedRewashIndicator>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireActivity(), "Ma'lumot olib kelishda xatolik", Toast.LENGTH_SHORT).show()
            }
        })
    }

}