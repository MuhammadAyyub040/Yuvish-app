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
import com.example.yuvish.Adapters.SubmittedIndicatorGroupAdapter
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentSubmittedIndicatorsBinding
import com.example.yuvish.models.Warehouse.BasePagingModel
import com.example.yuvish.models.baseIndikatorsIndex.IndicatorProduct
import com.example.yuvish.models.baseIndikatorsIndex.PaginationPageSubmittedIndicator
import com.example.yuvish.models.baseIndikatorsIndex.ReceivedWashedIndicatorPagingSource
import com.example.yuvish.models.baseIndikatorsIndex.SubmittedIndicator
import com.example.yuvish.retrofit.ApiClient
import com.example.yuvish.retrofit.isNull
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubmittedIndicatorsFragment : Fragment() {

    lateinit var binding: FragmentSubmittedIndicatorsBinding
    private val submittedIndicatorGroupAdapter: SubmittedIndicatorGroupAdapter by lazy {
        SubmittedIndicatorGroupAdapter(requireActivity()) }
    private val totalProductsAdapter: ProductsIndicatorChildAdapter by lazy {
        ProductsIndicatorChildAdapter(requireActivity()) }

    private var tableFooterList: List<IndicatorProduct>? = null
    private var totalIndicatorProductsList: BasePagingModel<SubmittedIndicator>? = null
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
            getPaginationSubmitted()
        }else{
            updateTotalIndicators(tableFooterList!!)
        }

        binding.totalProductsRV.adapter = totalProductsAdapter
        binding.indicatorsRV.adapter = submittedIndicatorGroupAdapter

        binding.backStack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun getPaginationSubmitted() {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { PaginationPageSubmittedIndicator(ApiClient.retrofitService, fromDate, toDate) }
        ).liveData.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                submittedIndicatorGroupAdapter.submitData(it)
            }
        }
    }

    private fun updateTotalIndicators(totalIndicatorProductsList: List<IndicatorProduct>){
        binding.totalProductCount.text = "${calcTotalProductsCount(totalIndicatorProductsList)} ${getString(R.string.pcs)}"
        totalProductsAdapter.submitList(totalIndicatorProductsList)
    }

    private fun calcTotalProductsCount(indicatorProductsList: List<IndicatorProduct>): Int{
        var count = 0

        indicatorProductsList.forEach {
            count += it.soni
        }

        return count
    }

    private fun String.transformDate(): String{
        val dateArray = this.split("-")
        return "${dateArray[2]}.${dateArray[1]}.${dateArray[0]}"
    }

    private fun getTableFooter(action: String){
        ApiClient.retrofitService.getTableFooter(action).enqueue(object : Callback<List<IndicatorProduct>>{
            override fun onResponse(call: Call<List<IndicatorProduct>>, response: Response<List<IndicatorProduct>>) {
                if (response.code() == 200){
                    tableFooterList = response.body()!!
                    updateTotalIndicators(tableFooterList!!)
                }
            }

            override fun onFailure(call: Call<List<IndicatorProduct>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Server bilan bog'lanishda xatolik", Toast.LENGTH_SHORT).show()
            }

        })
    }
}