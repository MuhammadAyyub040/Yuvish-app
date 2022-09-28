package com.example.yuvish.Fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.yuvish.Adapters.SearchCustomerResultsAdapter
import com.example.yuvish.Models.NewOrder.SearchCustomerResult
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentTransportBinding
import com.example.yuvish.databinding.SelectphonediaologlayoutBinding
import com.example.yuvish.retrofit.ApiClient
import com.example.yuvish.retrofit.isNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class TransportFragment : Fragment(), SearchCustomerResultsAdapter.CallBack {

    lateinit var binding: FragmentTransportBinding
    lateinit var toggle: ActionBarDrawerToggle
    var searchPage = false
    var newPage = false
    private val searchCustomerResultsAdapter: SearchCustomerResultsAdapter by lazy {
        SearchCustomerResultsAdapter(requireActivity(), this, false)
    }

    private var content: String? = null
    private var customerId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("refreshRequest") { requestKey, bundle ->
            if (bundle.getBoolean("refreshPermission")){
                checkContent()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransportBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arrayAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            arrayListOf("2022", "2021")
        )
        binding.autoCompleteTextViewTransport.setAdapter(arrayAdapter)

        binding.rvCustomerSearching.adapter = searchCustomerResultsAdapter

        binding.searchCustomer.setOnClickListener {
            closeKeyboard()
            checkContent()
        }

        binding.btnX.setOnClickListener {
            binding.searchCard.visibility = View.GONE
            searchPage = false

            closeKeyboard()
        }

        binding.btnSearch.setOnClickListener {
            when (searchPage) {
                false -> {
                    binding.searchCard.visibility = View.VISIBLE
                    searchPage = true
                }
                true -> {
                    binding.searchCard.visibility = View.GONE
                    searchPage = false
                }
            }
        }

        binding.btnMenu.setOnClickListener {
            binding.navView.visibility = View.GONE
            newPage = false

            closeKeyboard()
        }

        binding.btnNew.setOnClickListener {
            findNavController().navigate(R.id.addCustomerFragment)
        }

        toggle =
            ActionBarDrawerToggle(
                requireActivity(),
                binding.drawerLayout,
                R.string.open,
                R.string.close
            )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.open()
        }

        binding.navView.setNavigationItemSelectedListener {

            when (it.itemId) {

                R.id.base -> {
                    Toast.makeText(
                        requireActivity(),
                        " Asosiy bo'lim tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.baseFragment)
                }
                R.id.new_order -> {
                    Toast.makeText(
                        requireActivity(),
                        " Yangi buyurtmalar tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.transportFragment)
                }
                R.id.washing -> {
                    Toast.makeText(
                        requireActivity(),
                        " Yuvish bo'limi tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.homeFragment)
                }
                R.id.ready -> {
                    Toast.makeText(
                        requireActivity(),
                        " Tayyor buyurtmalar tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.tayyorFragment)
                }
                R.id.warehouse -> {
                    Toast.makeText(
                        requireActivity(),
                        " Sklad bo'limi tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.skladFragment)
                }
                R.id.employee_setting -> {
                    Toast.makeText(
                        requireActivity(),
                        " Xodim sozlamalari tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.settingFragment)
                }
                R.id.close -> {
                    Toast.makeText(
                        requireActivity(),
                        " Chiqish tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.loginFragment)
                }
                R.id.debtors -> {
                    Toast.makeText(
                        requireActivity(),
                        " Qarzdorlar bo'limi tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.debtorsDepartmentFragment)
                }
            }
            true

        }
    }

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
            R.id.confirmationOrderFragment,
            bundleOf("orderId" to orderId)
        )
    }

    private fun toWriteReceiptFragment(orderId: Int){
        findNavController().navigate(
            R.id.listFragment,
            bundleOf("orderId" to orderId)
        )
    }

    override fun phoneClicked(phone1: String, phone2: String) {
        showPhoneSelectorDialog(phone1, phone2)
    }

    private fun showPhoneSelectorDialog(phone1: String, phone2: String){
        val bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.bottomDialogStyle)
        val dialogBinding = SelectphonediaologlayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(dialogBinding.root)

        if (phone1.checkPhoneFormat()){
            dialogBinding.phone1.text = phone1
        }else{
            dialogBinding.phone1Card.visibility = View.GONE
        }

        if (phone2.checkPhoneFormat()){
            dialogBinding.phone2.text = phone2
        }else{
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
        }else{
            Toast.makeText(requireActivity(), getString(R.string.no_phone_number_entered), Toast.LENGTH_SHORT).show()
        }
    }

    private fun intentCall(phoneNumber: String){
        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        startActivity(callIntent)
    }

    private fun checkContent(){
        content = binding.content.text.toString()

        if (content!!.isEmpty()){
            binding.content.error = getString(R.string.enter_something_search)
        }else{
            searchCustomer(content!!)
        }
    }

    private fun String.checkPhoneFormat(): Boolean{
        return this.isNotEmpty() && this.length == 13
    }

    private fun notFoundLayoutVisible(visible: Boolean){
        binding.notFoundLayout.isVisible = visible
    }

    private fun closeKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.edtId.windowToken, 0)
    }

    private fun searchCustomer(content: String){
        ApiClient.retrofitService.searchCustomer(content).enqueue(object : Callback<List<SearchCustomerResult>>{
            override fun onResponse(call: Call<List<SearchCustomerResult>>, response: Response<List<SearchCustomerResult>>) {
                if (response.code() == 200){
                    notFoundLayoutVisible(response.body()!!.isEmpty())
                    searchCustomerResultsAdapter.submitList(response.body())
                }
                }

            override fun onFailure(call: Call<List<SearchCustomerResult>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireActivity(), "Ma'limot jo'natilishida xatolik", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun createOrderByCustomerId(customerId: Int){
        ApiClient.retrofitService.createOrderByCustomerId(customerId).enqueue(object : Callback<Int?>{
            override fun onResponse(call: Call<Int?>, response: Response<Int?>) {
                if (response.code() == 200){
                    if (response.body().isNull()) {
                        Toast.makeText(requireActivity(), getString(R.string.error), Toast.LENGTH_SHORT).show()
                    } else {
                        toWriteReceiptFragment(response.body()!!)
                    }
                }
            }

            override fun onFailure(call: Call<Int?>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireActivity(), "Ma'limot jo'natilishida xatolik", Toast.LENGTH_SHORT).show()
            }

        })
    }
}