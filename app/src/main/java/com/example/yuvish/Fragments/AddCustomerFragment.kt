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
import com.example.yuvish.Adapters.SourceAdapter
import com.example.yuvish.Models.NewOrder.GetCustomer
import com.example.yuvish.Models.NewOrder.Sources
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentAddCustomerBinding
import com.example.yuvish.retrofit.ApiClient
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_diaolg.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCustomerFragment : Fragment(),SourceAdapter.OnItemClick {

    lateinit var binding: FragmentAddCustomerBinding
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var sourceAdapter: SourceAdapter
    lateinit var sources: Sources
    lateinit var list: List<Sources>
    lateinit var getCustomer: GetCustomer
    var searchPage = false
    var newPage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        getSources()

        binding = FragmentAddCustomerBinding.inflate(layoutInflater)

        binding.btnSave.setOnClickListener {

            val fish = binding.edtFish.text.toString().trim()
            val manzil = binding.edtLocationCustomer.text.toString().trim()

            if (fish.isEmpty()) {
                binding.edtFish.error = "Ushbu joyni to'ldiring"
                binding.edtFish.requestFocus()
            } else
                if (manzil.isEmpty()) {
                    binding.edtLocationCustomer.error = "Ushbu joyni to'ldiring"
                    binding.edtLocationCustomer.requestFocus()
                } else {
                    findNavController().navigate(R.id.listFragment)
                }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arrayAdapter2 = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            arrayListOf("O'zbek", "Millatni tanlang")
        )
        addCustomer()
        binding.autoCompleteTextViewLanguage.setAdapter(arrayAdapter2)

        binding.imgSource.setOnClickListener {
            val view:View = layoutInflater.inflate(R.layout.bottom_sheet_diaolg, null)
            val dialog = BottomSheetDialog(requireActivity())
            dialog.setContentView(view)
            dialog.show()

            dialog.img_close.setOnClickListener {
                dialog.dismiss()
            }
            sourceAdapter = SourceAdapter(this@AddCustomerFragment,list )
            dialog.rv_source.adapter = sourceAdapter
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
        binding.btnCancellation.setOnClickListener {
           findNavController().navigate(R.id.transportFragment)

            closeKeyboard()
        }
        binding.btnSave.setOnClickListener {
            findNavController().navigate(R.id.listFragment)
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
    private fun addCustomer(){
        ApiClient.retrofitService.addCustomer().enqueue(object : Callback<GetCustomer> {
            override fun onResponse(call: Call<GetCustomer>, response: Response<GetCustomer>) {
                if (response.code() == 200) {

                    getCustomer = response.body()!!
                    binding.edtFish.setText(getCustomer.costumer_name)
                    binding.edtLocationCustomer.setText(getCustomer.costumer_addres)
                    binding.edtPhoneNumber1.setText(getCustomer.costumer_phone_1)
                    binding.edtPhoneNumber2.setText(getCustomer.costumer_phone_2)
                    binding.edtSource.setText(getCustomer.manba)
                    list = listOf(sources)
                }
            }

            override fun onFailure(call: Call<GetCustomer>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Mijoz qo'shishda xatolik", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getSources(){
        ApiClient.retrofitService.getSources().enqueue(object : Callback<Sources>{
            override fun onResponse(call: Call<Sources>, response: Response<Sources>) {
                if (response.code() == 200)
                    Log.d("TAG", "onResponse")
            }

            override fun onFailure(call: Call<Sources>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Mijoz qo'shishda xatolik", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun closeKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.edtId.windowToken, 0)
    }

    override fun onItemClick(position: Int) {

    }
}