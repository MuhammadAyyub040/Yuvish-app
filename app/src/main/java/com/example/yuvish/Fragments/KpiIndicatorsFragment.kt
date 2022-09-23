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
import com.example.yuvish.Adapters.KpiIndicatorAdapter
import com.example.yuvish.Models.BaseIndikatorsIndex.KpiIndicator
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentKpiIndicatorsBinding
import com.example.yuvish.retrofit.ApiClient
import com.example.yuvish.retrofit.isNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KpiIndicatorsFragment : Fragment() {

    lateinit var binding: FragmentKpiIndicatorsBinding
    lateinit var kpiIndicator: KpiIndicator
    private val kpiIndicatorAdapter: KpiIndicatorAdapter by lazy {
        KpiIndicatorAdapter(requireActivity())
    }

    private var totalIndicatorAmount: Int? = null
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
        binding = FragmentKpiIndicatorsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fromAndToDate.text = getString(R.string.between_kpi, fromDate.transformDate(), toDate.transformDate())

        if (totalIndicatorAmount.isNull()){
            totalIndicatorsPlaceHolderVisible(true)
            getKpiIndicator(fromDate, toDate, 1)
        }else{
            totalIndicatorsPlaceHolderVisible(false)
            updateTotalIndicators(totalIndicatorAmount!!)
        }

        binding.indicatorsRV.adapter = kpiIndicatorAdapter

        binding.backStack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun updateTotalIndicators(totalIndicatorAmount: Int){
        binding.totalIndicatorAmount.text = "$totalIndicatorAmount ${getString(R.string.so_m)}"
    }

    private fun String.transformDate(): String{
        val dateArray = this.split("-")
        return "${dateArray[2]}.${dateArray[1]}.${dateArray[0]}"
    }

    private fun totalIndicatorsPlaceHolderVisible(visible: Boolean){
        binding.totalIndicatorAmount.isGone = visible
    }

    private fun getKpiIndicator(fromDate: String, toDate: String, page: Int){
        ApiClient.retrofitService.getKpiIndicators(fromDate, toDate, page).enqueue(object : Callback<KpiIndicator>{
            override fun onResponse(call: Call<KpiIndicator>, response: Response<KpiIndicator>) {
                if (response.code() == 200){
                    Log.e("TAG", "onResponse: ${response.code()}" )
                    kpiIndicator = response.body()!!
                    updateTotalIndicators(response.body()!!.jami_summa)
                }
            }

            override fun onFailure(call: Call<KpiIndicator>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "OnFailure", Toast.LENGTH_SHORT).show()
            }

        })
    }

    }