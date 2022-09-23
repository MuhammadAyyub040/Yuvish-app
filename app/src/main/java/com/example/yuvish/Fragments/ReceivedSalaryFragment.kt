package com.example.yuvish.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import com.example.yuvish.Adapters.ReceivedSalaryIndicatorAdapter
import com.example.yuvish.Models.BaseIndikatorsIndex.ReceivedRewashIndicatorOrder
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentReceivedSalaryBinding
import com.example.yuvish.retrofit.isNull
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ReceivedSalaryFragment : Fragment() {

    private lateinit var binding: FragmentReceivedSalaryBinding
    lateinit var receivedSalaryIndicatorAdapter: ReceivedSalaryIndicatorAdapter

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
    ): View{
        binding = FragmentReceivedSalaryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fromAndToDate.text = getString(R.string.between_received_salary, fromDate.transformDate(), toDate.transformDate())

//        if (totalIndicatorAmount.isNull()){
//            totalIndicatorsPlaceHolderVisible(true)
//            loadTotalIndicator(fromDate, toDate)
//        }else{
//            totalIndicatorsPlaceHolderVisible(false)
//            updateTotalIndicators(totalIndicatorAmount!!)
//        }

        binding.indicatorsRV.adapter = receivedSalaryIndicatorAdapter

        binding.backStack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

//    private fun loadTotalIndicator(fromDate: String, toDate: String, page: Int){
//     getReceivedRewashIndicators(fromDate, toDate, page)
//    }

//    fun getReceivedRewashIndicators(fromDate: String, toDate: String, page: Int){
//            try {
//                val response = repository.getReceivedRewashIndicators(fromDate, toDate, page)
//                if (response.isSuccessful){
//                    getReceivedRewashIndicatorsData.postValue(Result.success(response.body()))
//                }else{
//                    getReceivedRewashIndicatorsData.postValue(Result.error(HttpException(response)))
//                }
//            }catch (t: Throwable){
//                getReceivedRewashIndicatorsData.postValue(Result.error(t))
//            }
//    }

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

    private fun indicatorsPlaceHolderVisible(visible: Boolean){
        binding.indicatorsRV.isGone = visible
    }

}