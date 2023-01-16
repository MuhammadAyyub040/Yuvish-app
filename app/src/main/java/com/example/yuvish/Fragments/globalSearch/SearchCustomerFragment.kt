package com.example.yuvish.Fragments.globalSearch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.addTextChangedListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.yuvish.Adapters.SearchCustomerResultsAdapter
import com.example.yuvish.models.NewOrder.SearchCustomerResult
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentSearchCustomerBinding
import com.example.yuvish.databinding.SelectPhoneDialogLayoutBinding
import com.example.yuvish.models.addCostumer.createOrder
import com.example.yuvish.retrofit.ApiClient
import com.example.yuvish.retrofit.isNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchCustomerFragment : Fragment(),
    SearchCustomerResultsAdapter.CallBack {


    private lateinit var binding: FragmentSearchCustomerBinding
    lateinit var createOrder: createOrder
    private val searchCustomerResultsAdapter: SearchCustomerResultsAdapter by lazy {
        SearchCustomerResultsAdapter(requireActivity(), this, true)
    }
    private val contentFlow: MutableSharedFlow<String> by lazy {
        MutableSharedFlow()
    }

    private var content: String? = null
    private var customerId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeContentFlow()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchCustomerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchResultRV.adapter = searchCustomerResultsAdapter

        binding.content.addTextChangedListener {
            val content = it.toString()

            if (content.isEmpty()) {
                searchCustomerResultsAdapter.submitList(emptyList())
                notFoundLayoutVisible(false)
            } else {
                emitContent(content)
            }
        }

    }

    private fun emitContent(content: String) = flow<String> {
        contentFlow.emit(content)
    }.launchIn(lifecycleScope)

    private fun subscribeContentFlow() = flow<String> {
        contentFlow
            .debounce(500)
            .collectLatest {
                content = it
                searchCustomer(it)
            }
    }.launchIn(lifecycleScope)

    override fun writeReceiptSelected(searchCustomerResult: SearchCustomerResult) {
        when {
            searchCustomerResult.tasdilanadigan_order_id != 0 -> {
                toConfirmationOrderFragment(searchCustomerResult.tasdilanadigan_order_id!!)
            }
            searchCustomerResult.qabulqilinadigan_order_id != 0 -> {
                toWriteReceiptFragment(searchCustomerResult.qabulqilinadigan_order_id!!)
            }
            else -> {
                customerId = searchCustomerResult.costumer1!!.id
                createOrderByCustomerId(customerId!!)
            }
        }
    }

    private fun toConfirmationOrderFragment(orderId: Int) {
        findNavController().navigate(
            R.id.action_globalSearchFragment_to_confirmationOrderFragment,
            bundleOf("orderId" to orderId)
        )
    }

    private fun toWriteReceiptFragment(orderId: Int) {
        findNavController().navigate(
            R.id.action_globalSearchFragment_to_listFragment,
            bundleOf("orderId" to orderId)
        )
    }

    override fun phoneClicked(phone1: String, phone2: String) {
        showPhoneSelectorDialog(phone1, phone2)
    }

    private fun showPhoneSelectorDialog(phone1: String, phone2: String) {
        val bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.bottomDialogStyle)
        val dialogBinding = SelectPhoneDialogLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(dialogBinding.root)

        if (phone1.checkPhoneFormat()) {
            dialogBinding.phone1.text = phone1
        } else {
            dialogBinding.phone1Card.visibility = View.GONE
        }

        if (phone2.checkPhoneFormat()) {
            dialogBinding.phone2.text = phone2
        } else {
            dialogBinding.phone2Card.visibility = View.GONE
        }

        dialogBinding.phone1Card.setOnClickListener {
            intentCall(phone1)
        }

        dialogBinding.phone2Card.setOnClickListener {
            intentCall(phone2)
        }

        dialogBinding.close.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        if (dialogBinding.phone1Card.isVisible || dialogBinding.phone2Card.isVisible) {
            bottomSheetDialog.show()
        } else {
            Toast.makeText(
                requireActivity(),
                getString(R.string.no_phone_number_entered),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun intentCall(phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        startActivity(callIntent)
    }

    private fun String.checkPhoneFormat(): Boolean {
        return this.isNotEmpty() && this.length == 13
    }

    private fun notFoundLayoutVisible(visible: Boolean) {
        binding.notFoundLayout.isVisible = visible
    }

    private fun searchCustomer(content: String) {
        ApiClient.retrofitService.searchCustomer(content).enqueue(object :
            Callback<List<SearchCustomerResult>> {
            override fun onResponse(
                call: Call<List<SearchCustomerResult>>,
                response: Response<List<SearchCustomerResult>>
            ) {
                if (response.code() == 200) {
                    notFoundLayoutVisible(response.body()!!.isEmpty())
                    searchCustomerResultsAdapter.submitList(response.body())
                }
            }

            override fun onFailure(call: Call<List<SearchCustomerResult>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(
                    requireActivity(),
                    "Ma'limot jo'natilishida xatolik",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun createOrderByCustomerId(customerId: Int) {
        ApiClient.retrofitService.createOrderByCustomerId(customerId)
            .enqueue(object : Callback<createOrder> {
                override fun onResponse(call: Call<createOrder>, response: Response<createOrder>) {
                    if (response.code() == 200) {
                        if (response.body().isNull()) {
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.error),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            toWriteReceiptFragment(createOrder.order_id)
                        }
                    }
                }

                override fun onFailure(call: Call<createOrder>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(
                        requireActivity(),
                        "Ma'limot jo'natilishida xatolik",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }
}