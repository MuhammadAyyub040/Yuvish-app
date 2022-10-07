package com.example.yuvish.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.yuvish.Adapters.ConfirmationProductsAdapter
import com.example.yuvish.Models.NewOrder.ConfirmOrder
import com.example.yuvish.Models.NewOrder.PutConfirmOrder
import com.example.yuvish.Models.NewOrder.PutService
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentConfirmationOrderBinding
import com.example.yuvish.retrofit.ApiClient
import com.example.yuvish.retrofit.isNull
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConfirmationOrderFragment : Fragment(), ConfirmationProductsAdapter.CallBack {

    lateinit var binding: FragmentConfirmationOrderBinding
    private val confirmationProductsAdapter: ConfirmationProductsAdapter by lazy {
        ConfirmationProductsAdapter(this, requireActivity())
    }

    private var orderId: Int? = null
    private var confirmOrder: ConfirmOrder? = null
    private var whereRequestSentFrom: String? = null

    private var putConfirmOrder: PutConfirmOrder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = arguments?.getInt("orderId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfirmationOrderBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmationProductsRV.adapter = confirmationProductsAdapter

        if (confirmOrder.isNull()){
            getConfirmOrderById(orderId!!)
        }else{
            updateUI(confirmOrder!!)
        }

        binding.toGetSize.setOnClickListener {
            checkConfirmOrder("toGetSize")
        }

        binding.confirm.setOnClickListener {
            checkConfirmOrder("confirm")
        }

    }

    private fun checkConfirmOrder(whereRequestSentFrom: String){
        when{
            confirmOrder.isNull() -> {
                Toast.makeText(requireActivity(), getString(R.string.not_enough_information), Toast.LENGTH_SHORT).show()
            }
            getProductsCount(confirmOrder!!) == 0 -> {
                Toast.makeText(requireActivity(), getString(R.string.must_have_at_least_1_product), Toast.LENGTH_SHORT).show()
            }
            else -> {
                putConfirmOrder = PutConfirmOrder(
                    binding.comment.text.toString(),
                    confirmOrder!!.items.map {
                        PutService(it.value, it.x_id)
                    }
                )
                this.whereRequestSentFrom = whereRequestSentFrom
                putConfirmationOrder(orderId!!, putConfirmOrder!!)
            }
        }
    }

    private fun updateUI(confirmOrder: ConfirmOrder){
        binding.customerName.text = confirmOrder.costumer?.costumer_name
        binding.address.text = confirmOrder.costumer?.costumer_addres
        binding.receiptNumber.text = confirmOrder.nomer.toString()
        binding.comment.setText(confirmOrder.izoh2)

        updateProductsCount(confirmOrder)
        confirmationProductsAdapter.submitList(confirmOrder.items)
    }

    private fun updateProductsCount(confirmOrder: ConfirmOrder){
        val count = getProductsCount(confirmOrder)
        binding.totalProductCount.text = "$count ${getString(R.string.pcs)}"
    }

    private fun getProductsCount(confirmOrder: ConfirmOrder): Int{
        var count = 0
        confirmOrder.items.forEach {
            count += it.value
        }
        return count
    }

    private fun sendBackStackRefreshRequest(){
        setFragmentResult("refreshRequest",
            bundleOf("refreshPermission" to true)
        )
    }

    private fun showSnackBar(text: String){
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    private fun getConfirmOrderById(orderId: Int){
        ApiClient.retrofitService.getConfirmOrderById(orderId).enqueue(object : Callback<ConfirmOrder?>{
            override fun onResponse(call: Call<ConfirmOrder?>, response: Response<ConfirmOrder?>) {
                if (response.code() == 200){
                    confirmOrder = response.body()
                    binding.customerName.text = confirmOrder?.costumer?.costumer_name
                    binding.address.text = confirmOrder?.costumer?.costumer_addres
                    binding.receiptNumber.text = confirmOrder?.nomer?.toString()
                    if (response.body().isNull()){
                        Toast.makeText(requireActivity(), getString(R.string.error), Toast.LENGTH_SHORT).show()
                    }else{
                        confirmOrder = response.body()
                        updateUI(confirmOrder!!)
                    }
                }
            }

            override fun onFailure(call: Call<ConfirmOrder?>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Ma'lumot ketmadi", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun putConfirmationOrder(orderId: Int, putConfirmOrder: PutConfirmOrder){
        ApiClient.retrofitService.putConfirmationOrder(orderId, putConfirmOrder).enqueue(object : Callback<String?>{
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.code() == 200){
                    if (response.body().isNull()){
                        Toast.makeText(requireActivity(), getString(R.string.error), Toast.LENGTH_SHORT).show()
                    }else{
                        showSnackBar(getString(R.string.order_confirmed))

                        when (whereRequestSentFrom) {
                            "toGetSize" -> {
                                findNavController().navigate(R.id.action_confirmationOrderFragment_to_getSizeFragment,
                                    bundleOf("orderId" to orderId)
                                )
                            }
                            "confirm" -> {
                                sendBackStackRefreshRequest()
                                findNavController().navigate(R.id.action_confirmationOrderFragment_to_transportFragment)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Ma'lumot ketmadi", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun updateProductsCount() {

    }

}