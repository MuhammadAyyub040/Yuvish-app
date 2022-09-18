package com.example.yuvish.Fragments

import com.example.yuvish.R
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.example.yuvish.Models.DebtorsPackage.ConfirmDebt
import com.example.yuvish.Models.DebtorsPackage.Debtors
import com.example.yuvish.Models.DebtorsAPI.Market.Paydebt
import com.example.yuvish.Models.StringHelper
import com.example.yuvish.databinding.FragmentDebtorBinding
import com.example.yuvish.databinding.ItemWarnBinding
import com.example.yuvish.retrofit.ApiClient
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DebtorFragment : Fragment() {

    lateinit var binding: FragmentDebtorBinding
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var debtors: Debtors
    private var debtId: Int? = null
    private var confirmDebt: ConfirmDebt? = null
    var searchPage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDebtorBinding.inflate(layoutInflater)
        debtId = arguments?.getInt("id")
        Log.e("Debtor Id", "onCreateView: $debtId", )
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        debtorCustomer(id)

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

        binding.btnGiveUp.setOnClickListener {
                val dialogBinding = ItemWarnBinding.inflate(layoutInflater)

                val myDialog = AlertDialog.Builder(requireActivity()).create()
                myDialog.setView(dialogBinding.root)

                myDialog.setCancelable(true)
                myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                myDialog.show()
        }

        binding.datePickerDebtor.setOnClickListener {
            showDatePicker()
        }

        binding.btnConfirmation.setOnClickListener {
            checkDebtPayDate()
        }

        toggle = ActionBarDrawerToggle(
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

    private fun checkDebtPayDate(){
        val date = binding.edtDatePickerDebtor.text.toString()

        if (date == "--.--.----"){
            Toast.makeText(requireActivity(), getString(R.string.date_empty), Toast.LENGTH_SHORT).show()
        }else if (!date.isAfterToday()){
            Toast.makeText(requireActivity(), getString(R.string.after_today_date_error), Toast.LENGTH_SHORT).show()
        }else{
            confirmDebt = ConfirmDebt(changeDateStructure(date), debtId!!)
        }
    }

    private fun changeDateStructure(date: String): String {
        val dateArray = date.split(".") //dd.MM.yyyy
        return "${dateArray[2]}-${dateArray[1]}-${dateArray[0]}" //yyyy-MM-dd
    }

    private fun showDatePicker(){
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener {
            if (it.isAfterToday()){
                binding.edtDatePickerDebtor.setText(getDateStringFormat(it))
            }else{
                Toast.makeText(requireActivity(), getString(R.string.after_today_date_error), Toast.LENGTH_SHORT).show()
            }
        }

        picker.show(requireActivity().supportFragmentManager, "tag_picker")
    }

    private fun Long.isAfterToday(): Boolean{
        return Date(this).after(Date(Date().time - 86400000))
    }

    private fun String.isAfterToday(): Boolean{
        val dateArray = this.split(".")

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.DAY_OF_MONTH,dateArray[0].toInt())
        calendar.set(Calendar.MONTH,dateArray[1].toInt()-1)
        calendar.set(Calendar.YEAR,dateArray[2].toInt())

        return calendar.timeInMillis.isAfterToday()
    }

    private fun getDateStringFormat(it: Long): String{
        return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(it))
    }

    private fun setDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val returnDate = "$dayOfMonth ${monthOfYear + 1} $year"
                val date = StringHelper.parseDate(
                    "dd MM yyyy",
                    "dd/MM/yyyy",
                    returnDate
                )
                val dateString = date
                binding.edtDatePickerDebtor.setText(
                    StringHelper.parseDate(
                        "dd/MM/yyyy",
                        "dd MM yyyy",
                        date
                    )
                )
                binding.edtDatePickerDebtor.error = null

            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun putDebt(confirmDebt: ConfirmDebt){
        ApiClient.retrofitService.putDebt(confirmDebt).enqueue(object : Callback<String?>{
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.code() == 200)
                    if (response.body() == null){
                        Toast.makeText(requireActivity(), getString(R.string.error), Toast.LENGTH_SHORT).show()
                    }else {
                        showSnackBar(getString(R.string.payment_date_entered_successfully))
                        sendBackStackRefreshRequest()
                        findNavController().popBackStack()
                    }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "OnFailure", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun showSnackBar(text: String){
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    private fun sendBackStackRefreshRequest(){
        setFragmentResult("refreshRequest",
            bundleOf("refreshPermission" to true)
        )
    }

    private fun paymentDebt(paydebt: Paydebt) {
        ApiClient.retrofitService.requestPayDebt(paydebt).enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.code() == 200)
                    Log.d("TAG", "onResponse")
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "OnFailure", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun debtorCustomer(id: Int) {
        ApiClient.retrofitService.debtorCustomer(id).enqueue(object : Callback<Debtors> {
            override fun onResponse(call: Call<Debtors>, response: Response<Debtors>) {
                Log.e("Debtor", "onResponse: ${response.code()}" )
                if (response.code() == 200) {
                    Log.e("Debtor", "onResponse: ${response.body()}")
                    if (response.body() != null){
                        debtors = response.body()!!
                        binding.debtorCustomerName.text = debtors.costumer.costumer_name
                        binding.debtorCustomerId.text = debtors.costumer.id.toString()
                        binding.debtorCustomerPhoneNumber.text = debtors.costumer.costumer_phone_1
                        binding.debtorCustomerLocation.text = debtors.costumer.costumer_addres
                        binding.edtDebtAmount.setText(debtors.nasiya)
                    }
                }
            }

            override fun onFailure(call: Call<Debtors>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Xatolik yuz berdi", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun closeKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.edtId.windowToken, 0)
    }

}