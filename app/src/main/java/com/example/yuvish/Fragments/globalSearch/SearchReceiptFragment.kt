package com.example.yuvish.Fragments.globalSearch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.yuvish.Adapters.SearchReceiptButtonsAdapter
import com.example.yuvish.Models.globalSearch.ButtonParams
import com.example.yuvish.Models.globalSearch.SearchReceiptResult
import com.example.yuvish.Models.searchCustomer.SearchReceipt
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentSearchCustomerBinding
import com.example.yuvish.databinding.FragmentSearchReceiptBinding
import com.example.yuvish.retrofit.ApiClient
import com.example.yuvish.retrofit.isNull
import kotlinx.coroutines.flow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchReceiptFragment : Fragment(), SearchReceiptButtonsAdapter.CallBack {

    private lateinit var binding: FragmentSearchReceiptBinding
    private val receiptIdFlow: MutableSharedFlow<Int> by lazy {
        MutableSharedFlow()
    }

    private var searchReceiptResult: SearchReceiptResult? = null
    private var yearsList: ArrayList<Int>? = null

    private var selectedYearPosition = 0

    //data needed for refresh
    private var searchReceipt: SearchReceipt? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeReceiptIdFlow()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchReceiptBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clearDisplay()

        if (yearsList.isNull()) {
            loadYears()
        }
        updateDropDown(yearsList!!)

        binding.inputReceiptId.addTextChangedListener {
            val receiptId = it.toString()

            if (receiptId.isEmpty()) {
                clearDisplay()
            } else {
                emitReceiptId(receiptId.toInt())
            }
        }

        binding.year.setOnItemClickListener { parent, view, position, id ->
            selectedYearPosition = position
        }

    }

    private fun loadYears() {
        yearsList = arrayListOf(2022, 2021)
    }

    private fun updateDropDown(yearsList: ArrayList<Int>) {
        val yearsAdapter =
            ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, yearsList)
        binding.year.setAdapter(yearsAdapter)

        if (yearsList.isNotEmpty()) {
            binding.year.setText(yearsList[selectedYearPosition].toString(), false)
        }
    }

    private fun emitReceiptId(receiptId: Int) = flow<String> {
        receiptIdFlow.emit(receiptId)
    }.launchIn(lifecycleScope)

    private fun subscribeReceiptIdFlow() = flow<Int> {
        receiptIdFlow
            .debounce(500)
            .collectLatest { receiptId ->
                searchReceipt = SearchReceipt(
                    receiptId, yearsList!![selectedYearPosition]
                )
                searchReceiptById(searchReceipt!!)
            }
    }.launchIn(lifecycleScope)

    private fun updateUI(searchReceiptResult: SearchReceiptResult) {
        binding.receiptNumber.text = searchReceiptResult.order.nomer.toString()
        binding.date.text = transformDate(searchReceiptResult.order.topshir_sana)
        binding.customerName.text = searchReceiptResult.order.costumer.costumer_name
        binding.status.text = searchReceiptResult.order.order_status
//        binding.operatorName.text = searchReceiptResult.order
//        binding.driverName.text = searchReceiptResult.order

        if (searchReceiptResult.order.own == 1) {
            binding.customerPickupImg.visibility = View.VISIBLE
        } else {
            binding.customerPickupImg.visibility = View.INVISIBLE
        }

        updateButtons(searchReceiptResult)
    }

    private fun updateButtons(searchReceiptResult: SearchReceiptResult) {
        val searchReceiptButtonsAdapter = SearchReceiptButtonsAdapter(
            getButtonParamsList(searchReceiptResult),
            this
        )
        binding.buttonsRV.adapter = searchReceiptButtonsAdapter
    }

    private fun searchReceiptById(searchReceipt: SearchReceipt){
        ApiClient.retrofitService.searchReceiptById(searchReceipt).enqueue(object : Callback<SearchReceiptResult>{
            override fun onResponse(call: Call<SearchReceiptResult>, response: Response<SearchReceiptResult>) {
                if (response.code() == 200){
                    notFoundLayoutVisible(response.body().isNull())
                    searchReceiptResult = response.body()
                    updateUI(searchReceiptResult!!)
                }
            }

            override fun onFailure(call: Call<SearchReceiptResult>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireActivity(), "onFailure", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun buttonClickListener(id: Int) {
        when(id){
            2 -> {
                findNavController().navigate(
                    R.id.action_globalSearchFragment_to_confirmationOrderFragment,
                    bundleOf(
                        "orderId" to searchReceiptResult!!.order.order_id
                    )
                )
            }
            3 -> {
                findNavController().navigate(
                    R.id.action_globalSearchFragment_to_getSizeFragment,
                    bundleOf(
                        "orderId" to searchReceiptResult!!.order.order_id
                    )
                )
            }
            5 -> {
                findNavController().navigate(
                    R.id.action_globalSearchFragment_to_homeFragment,
                    bundleOf(
                        "orderId" to searchReceiptResult!!.order.order_id
                    )
                )
            }
            6 -> {
                findNavController().navigate(
                    R.id.action_globalSearchFragment_to_sumbitFragment,
                    bundleOf(
                        "orderId" to searchReceiptResult!!.order.order_id
                    )
                )
            }
        }
    }

    private fun getButtonParamsList(searchReceiptResult: SearchReceiptResult): List<ButtonParams> {
        val orderStatus = searchReceiptResult.order.order_status
        val buttonParamsList = ArrayList<ButtonParams>()

        if (orderStatus == "keltirish")  {
            buttonParamsList.add(
                ButtonParams(1, R.drawable.ic_plus, getString(R.string.bring))
            )
        }

        if (orderStatus == "qabul qilindi")  {
            buttonParamsList.add(
                ButtonParams(2, R.drawable.ic_check, getString(R.string.completion))
            )
        }

        if (orderStatus == "olchov" || orderStatus == "yuvilmoqda")  {
            buttonParamsList.add(
                ButtonParams(3, R.drawable.resize, getString(R.string.getting_the_size))
            )
        }

        if (searchReceiptResult.yuvishdagilar > 0)  {
            buttonParamsList.add(
                ButtonParams(4, R.drawable.noun_carpet_cleaning_1852669, getString(R.string.washing))
            )
        }

        if (searchReceiptResult.qadoqashdagilar > 0)  {
            buttonParamsList.add(
                ButtonParams(5, R.drawable.ic_packing, getString(R.string.packing))
            )
        }

        if (searchReceiptResult.yuvishdagilar == 0 && searchReceiptResult.qadoqashdagilar == 0
            && searchReceiptResult.topshirishdagilar > 0)  {
            buttonParamsList.add(
                ButtonParams(6, R.drawable.ic_submittion, getString(R.string.submission))
            )
        }

        return buttonParamsList
    }

    private fun transformDate(oldDate: String): String {
        val dateArray = oldDate.split("-")
        return "${dateArray[2]}.${dateArray[1]}.${dateArray[0]}"
    }

    private fun notFoundLayoutVisible(visible: Boolean) {
        binding.notFoundLayout.isVisible = visible
        binding.searchResultReceipt.isVisible = !visible
    }

    private fun clearDisplay() {
        binding.notFoundLayout.isVisible = false
        binding.searchResultReceipt.isVisible = false
    }
}