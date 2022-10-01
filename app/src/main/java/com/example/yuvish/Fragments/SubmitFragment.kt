package com.example.yuvish.Fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.yuvish.Adapters.SubmitAdapterGroup
import com.example.yuvish.Adapters.SubmitAdapterChild
import com.example.yuvish.Models.ArrangedSubmit.PaymentTypesItem
import com.example.yuvish.Models.ArrangedSubmit.Product
import com.example.yuvish.Models.ArrangedSubmit.Submit
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentSumbitBinding
import com.example.yuvish.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubmitFragment : Fragment(), SubmitAdapterChild.CaLLBack {

    lateinit var binding: FragmentSumbitBinding
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var submitAdapter: SubmitAdapterGroup
    lateinit var paymentTypesItem: PaymentTypesItem
    lateinit var list: List<PaymentTypesItem>
    lateinit var submit: Submit
    var commentPage = false
    var discountPage = false
    var orderId: Int? = null

    private val TAG = "SubmitFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = arguments?.getInt("orderId")
        list = arrayListOf()
        paymentTypes()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSumbitBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrangedSubmit(orderId!!)

        submitAdapter = SubmitAdapterGroup(this, false)
        binding.rvSubmit.adapter = submitAdapter

        val arrayAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            arrayListOf("2022", "2021")
        )
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        binding.cardComment.setOnClickListener {
            binding.linearComment.visibility = View.GONE
            commentPage = false
        }

        binding.backStack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.locationLiner.setOnClickListener {
            intentGoogleMaps(submit.geoplugin_latitude, submit.geoplugin_longitude)
        }

        binding.cardChegirma.setOnClickListener {
            binding.linearDiscount.visibility = View.GONE
            discountPage = false
        }

        binding.btnPut.setOnClickListener {
            transferWarehouse(orderId!!)
        }

        binding.cardComment.setOnClickListener {
            when (commentPage) {
                false -> {
                    binding.linearComment.visibility = View.VISIBLE
                    commentPage = true
                }
                true -> {
                    binding.linearComment.visibility = View.GONE
                    commentPage = false
                }
            }
        }

        binding.cardChegirma.setOnClickListener {
            when (discountPage) {
                false -> {
                    binding.linearDiscount.visibility = View.VISIBLE
                    discountPage = true
                }
                true -> {
                    binding.linearDiscount.visibility = View.GONE
                    discountPage = false
                }
            }
        }

        binding.btnSubmitted.setOnClickListener {
            val payment = binding.editPayment.text.toString().trim()

            if (payment.isEmpty()) {
                binding.editPayment.error = "Mijozdan olingan pulni kiriting"
                binding.editPayment.requestFocus()
            } else {
                val givenAmount =
                    binding.editPayment.text.toString().filter { it.isDigit() }.toInt()
                val paymentType =
                    binding.autoCompleteTextViewPaymentType.text.toString().lowercase()
                orderId?.let { submitOrder(it, givenAmount, paymentType) }

            }
        }

    }

    private fun arrangedSubmit(orderId: Int) {
        ApiClient.retrofitService.submit(orderId).enqueue(object : Callback<Submit> {
            override fun onResponse(call: Call<Submit>, response: Response<Submit>) {
                if (response.code() == 200) {
                    submit = response.body()!!

                    binding.arrangedCustomer.text = submit.costumer.costumer_name
                    binding.arrangedSubmitNumber.text = submit.costumer.costumer_phone_1
                    binding.arrangedSubmitLocation.text = submit.costumer.costumer_addres
                    binding.arrangedSubmitOperator.text = submit.operator.fullname
                    binding.txtOrders.text = submit.jami_soni.toString()
                    binding.jamiSumma.text = submit.jami_summa.toString()
                    binding.yakuniyTolov.text = submit.yakuniy_summa.toString()
                    binding.txtSubmitNomer.text = submit.nomer.toString()
                    binding.comment2.text = submit.izoh2
                    binding.comment3.text = submit.izoh3
                    binding.chegirmaRewash.text = submit.qayta_yuvish_chegirma.toString()
                    binding.chegirmaWasDropped.text = submit.tushish_chegirma.toString()
                    binding.chegirmaTakeAway.text = submit.own_chegirma.toString()
                    binding.totalWasDropped.text = submit.jami_tushish_chegirma.toString()
                    binding.jamiChegirma.text = submit.jami_chegirma.toString()
                    submitAdapter.setData(response.body()!!.buyurtmalar)
                }
            }

            override fun onFailure(call: Call<Submit>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Server bilan bog'lanishda xatolik", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun paymentTypes() {
        ApiClient.retrofitService.paymentTypes().enqueue(object : Callback<List<PaymentTypesItem>> {

            override fun onResponse(call: Call<List<PaymentTypesItem>>, response: Response<List<PaymentTypesItem>>) {

                if (response.code() == 200) {
                    list = response.body()!!
                    val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, list.map { it.name })
                    binding.autoCompleteTextViewPaymentType.setAdapter(arrayAdapter)
                }
            }

            override fun onFailure(call: Call<List<PaymentTypesItem>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "OnFailure", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun submitOrder(orderId: Int, givenAmount: Int, paymentType: String) {
        ApiClient.retrofitService.submittingOrder(orderId, givenAmount, paymentType)
            .enqueue(object : Callback<Int?> {
                override fun onResponse(call: Call<Int?>, response: Response<Int?>) {
                    if (response.code() == 200)
                        when (response.body()) {
                            null -> {
                                Toast.makeText(requireActivity(), "null", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                sendBackStackRefreshRequest()
                                findNavController().navigate(
                                    R.id.debtorFragment,
                                    bundleOf("debtId" to response.body())
                                )
                            }
                        }
                }

                override fun onFailure(call: Call<Int?>, t: Throwable) {
                    Log.e(TAG, "onFailure: submitOrder ${t.message}")
                }

            })
    }

    private fun transferWarehouse(orderId: Int){
        ApiClient.retrofitService.transferWarehouse(orderId).enqueue(object : Callback<String?>{
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.code() == 200)
                    Toast.makeText(requireActivity(), "Omborga muvaffaqqiyatli o'tkazildi.", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.tayyorFragment)
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Xatolik yuz berdi", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun sendBackStackRefreshRequest() {
        setFragmentResult(
            "refreshRequest",
            bundleOf("refreshPermission" to true)
        )
    }

    private fun closeKeyboard(view: View) {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.edtId.windowToken, 0)
    }

    private fun intentGoogleMaps(lat: String, lon: String) {

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?daddr=$lat,$lon")
        )
        startActivity(intent)
    }

    override fun rewashClickListener(product: Product) {}

}