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
import com.example.yuvish.Adapters.SubmitAdapter
import com.example.yuvish.Adapters.SubmitAdapterChild
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
    lateinit var submitAdapter: SubmitAdapter
    lateinit var list: List<String>
    lateinit var submit: Submit
    var orderId: Int? = null
    var searchPage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSumbitBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = arguments?.getInt("orderId")
        arrangedSubmit(orderId!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submitAdapter = SubmitAdapter(this,true )
        binding.rvSubmit.adapter = submitAdapter
        val arrayAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            arrayListOf("2022", "2021")
        )
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        val arrayAdapter2 = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, arrayListOf("Naqd", "Click", "Plastik"))
        binding.autoCompleteTextViewList.setAdapter(arrayAdapter2)

        binding.btnX.setOnClickListener {
            binding.searchCard.visibility = View.GONE
            searchPage = false

            closeKeyboard(it)
        }

        binding.btnPut.setOnClickListener {
            findNavController().navigate(R.id.tayyorFragment)
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

        binding.btnSubmitted.setOnClickListener {
            val payment = binding.edtPayment.text.toString().trim()

            if (payment.isEmpty()){
                binding.edtPayment.error = "Mijozdan olingan pulni kiriting"
                binding.edtPayment.requestFocus()
            }else
                findNavController().navigate(R.id.debtorFragment)
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

    private val TAG = "SubmitFragment"

    private fun arrangedSubmit(orderId: Int){
        ApiClient.retrofitService.submit(orderId).enqueue(object : Callback<Submit>{
            override fun onResponse(call: Call<Submit>, response: Response<Submit>) {
                if (response.code() == 200)
                    submit = response.body()!!

                binding.arrangedCustomer.text = submit.costumer.costumer_name
                binding.arrangedSubmitNumber.text = submit.costumer.costumer_phone_1
                binding.arrangedSubmitLocation.text = submit.costumer.costumer_addres
                binding.arrangedSubmitOperator.text = submit.operator.fullname
                binding.jamiSumma.text = submit.jami_summa.toString()
                binding.yakuniyTolov.text = submit.yakuniy_summa.toString()
                binding.txtSubmitNomer.text = submit.nomer.toString()
                binding.jamiChegirma.text = submit.jami_chegirma.toString()
                submitAdapter.setData(response.body()!!.buyurtmalar)
                Log.d(TAG, "onResponse: ${submit.buyurtmalar.size}")
            }

            override fun onFailure(call: Call<Submit>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(
                    requireContext(),
                    "Server bilan bog'lanishda xatolik",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun closeKeyboard(view: View) {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.edtId.windowToken, 0)
    }

    override fun rewashClickListener(product: Product) {}

}