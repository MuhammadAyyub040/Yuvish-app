package com.example.yuvish.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.yuvish.Adapters.ProductsIndicatorChildAdapter
import com.example.yuvish.Adapters.WashedIndicatorGroupAdapter
import com.example.yuvish.Models.BaseIndikatorsIndex.IndicatorProduct
import com.example.yuvish.Models.BaseIndikatorsIndex.ReceivedRewashIndicatorPagingSource
import com.example.yuvish.Models.BaseIndikatorsIndex.ReceivedWashedIndicatorPagingSource
import com.example.yuvish.Models.BaseIndikatorsIndex.WashedIndicator
import com.example.yuvish.Models.ReadyOrders.PaginationPageArranged
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentWashedIndicatorsBinding
import com.example.yuvish.retrofit.ApiClient
import com.example.yuvish.retrofit.isNull
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WashedIndicatorsFragment : Fragment() {

    private lateinit var binding: FragmentWashedIndicatorsBinding

    private val washedIndicatorsGroupAdapter: WashedIndicatorGroupAdapter by lazy {
        WashedIndicatorGroupAdapter(requireActivity())
    }
    private val totalProductsAdapter: ProductsIndicatorChildAdapter by lazy {
        ProductsIndicatorChildAdapter(requireActivity())
    }

    private var totalIndicatorProductsList: List<IndicatorProduct>? = null
    private lateinit var fromDate: String
    private lateinit var toDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPaginationWashed()
        fromDate = arguments?.getString("fromDate")!!
        toDate = arguments?.getString("toDate")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWashedIndicatorsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fromAndToDate.text = getString(R.string.between_washed, fromDate.transformDate(), toDate.transformDate())

        if (totalIndicatorProductsList.isNull()){
            getWashedIndicators(fromDate, toDate, 1)
        }else{
            updateTotalIndicators(totalIndicatorProductsList!!)
        }

        binding.totalProductsRV.adapter = totalProductsAdapter
        binding.indicatorsRV.adapter = washedIndicatorsGroupAdapter

        binding.backStack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    fun getPaginationWashed() {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { ReceivedWashedIndicatorPagingSource(fromDate, toDate, ApiClient.retrofitService) }
        ).liveData.observe(this) {
            lifecycleScope.launch {
                washedIndicatorsGroupAdapter.submitData(it)
            }
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

    private fun getWashedIndicators(fromDate: String, toDate: String, page: Int){
        ApiClient.retrofitService.getWashedIndicator(fromDate, toDate, page).enqueue(object : Callback<WashedIndicator>{
            override fun onResponse(call: Call<WashedIndicator>, response: Response<WashedIndicator>) {
                if (response.code() == 200){
                    totalIndicatorProductsList = response.body()!!.jami_table_footer
                    updateTotalIndicators(totalIndicatorProductsList!!)
                }
            }

            override fun onFailure(call: Call<WashedIndicator>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireActivity(), "Ma'lumot kelmadi", Toast.LENGTH_SHORT).show()
            }

        })
      }

//    private fun getWashedIndicators(fromDate: String, toDate: String){
//        ApiClient.retrofitService.getWashedIndicators(fromDate, toDate).enqueue(object : Callback<WashedIndicator>{
//            override fun onResponse(call: Call<WashedIndicator>, response: Response<WashedIndicator>) {
//                if (response.code() == 200){
//                    washedIndicatorsGroupAdapter.submitData(it)
//                }
//            }
//
//            override fun onFailure(call: Call<WashedIndicator>, t: Throwable) {
//                t.printStackTrace()
//                Toast.makeText(requireActivity(), "Ma'lumot kelmadi", Toast.LENGTH_SHORT).show()
//            }
//
//        })
//    }
}