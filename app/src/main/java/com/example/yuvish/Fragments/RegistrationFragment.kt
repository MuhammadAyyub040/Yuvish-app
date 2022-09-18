package com.example.yuvish.Fragments

import android.app.Activity
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
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.L
import com.example.yuvish.Adapters.RegistrationAdapterChild
import com.example.yuvish.Adapters.RegistrationAdapterGroup
import com.example.yuvish.Models.BarcodeApi.Order
import com.example.yuvish.Models.Registration.Registration
import com.example.yuvish.Models.Registration.ServiceTypeItem
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentRegistrationBinding
import com.example.yuvish.retrofit.ApiClient
import kotlinx.android.synthetic.main.fragment_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationFragment : Fragment(), RegistrationAdapterChild.CaLLBack {

    lateinit var binding: FragmentRegistrationBinding
    lateinit var map: HashMap<String, List<String>>
    lateinit var titleList: ArrayList<String>
    lateinit var list: List<ServiceTypeItem>
    lateinit var registrationAdapterGroup: RegistrationAdapterGroup
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var registration: Registration
    var searchPage = false
    private var orderId: Int? = null
    private var toWhereShowNotification = "entryData"
    private var selectedOrder: Order? = null

    private val TAG = "SubmitFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = arguments?.getInt("orderId")
        ServiceType()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderId?.let { registrationApi(it) }

//        if (selectedOrder == null){
//            loadOrderById("entryData")
//            ApiClient.retrofitService.getShelfs()
//        }else{
//            updateUI(selectedOrder!!)
//        }

        registrationAdapterGroup = RegistrationAdapterGroup(this, false)
        binding.rvRegistrationOrders.adapter = registrationAdapterGroup

        titleList = ArrayList()
        map = HashMap()

        val arrayAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            arrayListOf("2022", "2021")
        )
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        binding.btnX.setOnClickListener {
            binding.searchCard.visibility = View.GONE
            searchPage = false

            closeKeyboard()
        }

        binding.cardPlus.setOnClickListener {

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
        toggle =
            ActionBarDrawerToggle(requireActivity(), binding.drawerLayout, R.string.open, R.string.close)
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

//    private fun updateUI(order: Order){
//        binding.customerName.text = order.costumer.costumer_name
//        binding.address.text = order.costumer.costumer_addres
//        binding.receiptNumber.text = order.nomer.toString()
//    }

//    private fun loadOrderById(where: String){
//        toWhereShowNotification = where
//        ApiClient.retrofitService.getOrderById(orderId!!)
//    }

    private fun registrationApi(orderId: Int){
        ApiClient.retrofitService.registrationApi(orderId).enqueue(object : Callback<Registration>{
            override fun onResponse(call: Call<Registration>, response: Response<Registration>) {
                Log.d(TAG, "onResponse: submitOrder  ${response.code()}")
                if (response.code() == 200) {
                    registration = response.body()!!

                    registrationAdapterGroup.setData(registration.products)
                    binding.customerName.text = registration.costumer.costumer_name
                    binding.address.text = registration.costumer.costumer_addres
                    binding.registrationPhoneNumber.text = registration.costumer.costumer_phone_1
                    binding.receiptNumber.text = registration.nomer.toString()
                }
            }

            override fun onFailure(call: Call<Registration>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Server bilan bog'lanolmadik", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun ServiceType(){
    ApiClient.retrofitService.serviceType().enqueue(object : Callback<List<ServiceTypeItem>>{
        override fun onResponse(call: Call<List<ServiceTypeItem>>, response: Response<List<ServiceTypeItem>>) {

            if (response.code() == 200){
                list = response.body()!!
                val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, list.map { it.xizmat_turi })
                binding.autoCompleteTextViewServiceType.setAdapter(arrayAdapter)
            }
        }

        override fun onFailure(call: Call<List<ServiceTypeItem>>, t: Throwable) {
            t.printStackTrace()
            Toast.makeText(requireContext(), "OnFailure", Toast.LENGTH_SHORT).show() } })
        }

    private fun addOrders(orderId: Int, xizmatId: Int){
        ApiClient.retrofitService.addOrders(orderId, xizmatId).enqueue(object : Callback<String?>{
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.code() == 200)
                    Toast.makeText(requireActivity(), "ishladiku", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Server bilan bog'lanishda xatolik", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun closeKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.edtId.windowToken, 0)
    }

    override fun rewashClickListener(product: com.example.yuvish.Models.Registration.Product) {
    }
}