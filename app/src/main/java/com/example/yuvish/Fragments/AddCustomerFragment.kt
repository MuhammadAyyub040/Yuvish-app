package com.example.yuvish.Fragments

import android.app.Activity
import android.content.ContentValues.TAG
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
import androidx.navigation.fragment.findNavController
import com.example.yuvish.Adapters.SourceAdapter
import com.example.yuvish.Models.NewOrder.Nationality
import com.example.yuvish.Models.NewOrder.PostCustomer
import com.example.yuvish.Models.NewOrder.Sources
import com.example.yuvish.R
import com.example.yuvish.databinding.BottomSheetDiaolgBinding
import com.example.yuvish.databinding.FragmentAddCustomerBinding
import com.example.yuvish.retrofit.ApiClient
import com.example.yuvish.retrofit.GlobalData
import com.example.yuvish.retrofit.isNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("CAST_NEVER_SUCCEEDS")
class AddCustomerFragment : Fragment(),SourceAdapter.OnItemClick {

    lateinit var binding: FragmentAddCustomerBinding
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var sourceAdapter: SourceAdapter
    lateinit var postCustomer: PostCustomer
    private var sourcesList: ArrayList<Sources>? = null
    private var nationalitiesList: ArrayList<Nationality>? = null
    private var customerTypesList: ArrayList<String>? = null
    private lateinit var customerTypeAdapter: ArrayAdapter<String>
    var searchPage = false
    var newPage = false

    private var selectedCustomerTypePosition = 0
    private var selectedNationalityPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding = FragmentAddCustomerBinding.inflate(layoutInflater)
        getNationalities()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sourceLayout.setEndIconOnClickListener {
            if (sourcesList.isNull()){
                Toast.makeText(requireActivity(), getString(R.string.not_enough_information), Toast.LENGTH_SHORT).show()
            }else{
                showSourcesSelectDialog(sourcesList!!)
                getSources()
            }
        }

        if (customerTypesList.isNull()){
            loadCustomerTypes()
        }
        updateCustomerTypeDropDown()

        binding.btnMenu.setOnClickListener {
            binding.navView.visibility = View.GONE
            newPage = false

            closeKeyboard()
        }

        binding.btnCancellation.setOnClickListener {
           findNavController().navigate(R.id.transportFragment)

            closeKeyboard()
        }
        binding.btnSave.setOnClickListener {
            checkInputsForCreateCustomer()
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
    private fun addCustomer(postCustomer: PostCustomer){
        ApiClient.retrofitService.addCustomer(postCustomer).enqueue(object : Callback<Int?> {
            override fun onResponse(call: Call<Int?>, response: Response<Int?>) {
                Log.d(TAG, "onResponse: ${response.body()}")
                if (response.code() == 200) {
                    getByCustomerId(response.body()!!)
                }
            }

            override fun onFailure(call: Call<Int?>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Mijoz qo'shishda xatolik", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getByCustomerId(customerId: Int){
        ApiClient.retrofitService.createOrderByCustomerId(customerId).enqueue(object : Callback<Int?>{
            override fun onResponse(call: Call<Int?>, response: Response<Int?>) {
                if (response.code() == 200){
                    findNavController().navigate(R.id.listFragment, bundleOf(
                        "customerId" to customerId
                    ))
                }
            }

            override fun onFailure(call: Call<Int?>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Mijozga id qo'shishda xatolik", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getNationalities(){
        ApiClient.retrofitService.getNationalities().enqueue(object : Callback<List<Nationality>>{
            override fun onResponse(call: Call<List<Nationality>>, response: Response<List<Nationality>>) {
                Log.d(TAG, "onResponse: ${response.body()}")
                if (response.code() == 200) {
                    val arrayAdapter2 = nationalitiesList?.let { ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, it.map { it.name }) }
                    binding.autoCompleteTextViewLanguage.setAdapter(arrayAdapter2)
                }
            }

            override fun onFailure(call: Call<List<Nationality>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Ma'lumot kelmadi", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getSources(){
        ApiClient.retrofitService.getSources().enqueue(object : Callback<Sources>{
            override fun onResponse(call: Call<Sources>, response: Response<Sources>) {
                if (response.code() == 200)
                    Log.d(TAG, "onResponse: ${response.code()}")
            }

            override fun onFailure(call: Call<Sources>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Manbalar kelmadi", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun closeKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.edtFish.windowToken, 0)
    }

    private fun updateCustomerTypeDropDown(){
        customerTypeAdapter =
            ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, customerTypesList!!)

        binding.autoCompleteCustomerType.setAdapter(customerTypeAdapter)

        if (customerTypesList!!.isNotEmpty()){
            binding.autoCompleteCustomerType.setText(customerTypesList!![selectedCustomerTypePosition], false)
        }
    }

    private fun loadCustomerTypes(){
        customerTypesList = ArrayList()
        customerTypesList?.apply { clear()
            addAll(listOf("narx", "sifat", "premium", "qora_royxat"))
        }
    }

    private fun checkInputsForCreateCustomer(){
        val name = binding.edtFish.text.toString().trim()
        val phoneNumber1 = binding.edtPhoneNumber1.text //911234567
        val phoneNumber2 = binding.edtPhoneNumber2.text //911234567
        val address = binding.edtAddressCustomer.text.toString().trim()
        val source = binding.source.text.toString().trim()

        when {
            name.isEmpty() || address.isEmpty() -> {
                Toast.makeText(requireActivity(), getString(R.string.all_cells_marked_must_be_filled), Toast.LENGTH_SHORT).show()
            }
            phoneNumber1.length != 9 || (phoneNumber2.isNotEmpty() && phoneNumber2.length != 9)-> {
                Toast.makeText(requireActivity(), getString(R.string.please_enter_valid_phone_number), Toast.LENGTH_SHORT).show()
            }
            getNationalityPermission() && (nationalitiesList.isNull() || nationalitiesList!!.isEmpty()) -> {
                Toast.makeText(requireActivity(), getString(R.string.not_enough_information), Toast.LENGTH_SHORT).show()
            }
            else -> {
                postCustomer = PostCustomer(
                    name,
                    phoneNumber1.toString(),
                    phoneNumber2.toString(),
                    address,
                    source,
                    "android",
                    customerTypesList!![selectedCustomerTypePosition],
                    if (getNationalityPermission())
                        nationalitiesList!![selectedNationalityPosition].id else 0
                )
                addCustomer(postCustomer)
            }
        }
        findNavController().navigate(R.id.listFragment)
    }

    private fun getNationalityPermission(): Boolean {
        return GlobalData.commonSettings?.adding_nation == 1
    }

    private fun showSourcesSelectDialog(sourcesList: ArrayList<Sources>){
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        val dialogBinding = BottomSheetDiaolgBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(dialogBinding.root)

        dialogBinding.rvSource.adapter = SourceAdapter(sourcesList, object : SourceAdapter.OnItemClick {
                override fun onItemClick(sources: Sources) {
                    binding.source.setText(sources.name)
                    bottomSheetDialog.dismiss()
                }
            })

        dialogBinding.imgClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    override fun onItemClick(sources: Sources) {
    }
}