package com.example.yuvish.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import com.example.yuvish.Adapters.ReceivedSalaryIndicatorAdapter
import com.example.yuvish.Models.BaseIndikatorsIndex.ReceivedRewashIndicatorOrder
import com.example.yuvish.Models.BaseIndikatorsIndex.ReceivedSalaryIndicator
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentReceivedSalaryBinding
import com.example.yuvish.retrofit.ApiClient
import com.example.yuvish.retrofit.isNull
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class ReceivedSalaryFragment : Fragment() {

    private lateinit var binding: FragmentReceivedSalaryBinding
    lateinit var receivedSalaryIndicatorAdapter: ReceivedSalaryIndicatorAdapter
    lateinit var receivedSalaryIndicator: ReceivedSalaryIndicator

    private var totalIndicatorAmount: Int? = null
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
    ): View{
        binding = FragmentReceivedSalaryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fromAndToDate.text = getString(R.string.between_received_salary, fromDate.transformDate(), toDate.transformDate())

        if (totalIndicatorAmount.isNull()){
            getReceivedRewashIndicators(fromDate, toDate, page = 1)
        }else{
            updateTotalIndicators(totalIndicatorAmount!!)
        }

        receivedSalaryIndicatorAdapter = ReceivedSalaryIndicatorAdapter()
        binding.indicatorsRV.adapter = receivedSalaryIndicatorAdapter

        binding.backStack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun getReceivedRewashIndicators(fromDate: String, toDate: String, page: Int){
        ApiClient.retrofitService.getReceivedSalaryIndicators(fromDate, toDate, page).enqueue(object : Callback<ReceivedSalaryIndicator>{
            override fun onResponse(call: Call<ReceivedSalaryIndicator>, response: Response<ReceivedSalaryIndicator>) {
                if (response.code() == 200) {
                    receivedSalaryIndicator = response.body()!!
                    binding.totalIndicatorAmount.text = "${receivedSalaryIndicator.jami_summa}${getString(R.string.so_m)}"
                }
            }

            override fun onFailure(call: Call<ReceivedSalaryIndicator>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireActivity(), "onFailure", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun updateTotalIndicators(totalIndicatorAmount: Int){
        binding.totalIndicatorAmount.text = "$totalIndicatorAmount ${getString(R.string.so_m)}"
    }

    private fun String.transformDate(): String{
        val dateArray = this.split("-")
        return "${dateArray[2]}.${dateArray[1]}.${dateArray[0]}"
    }

}