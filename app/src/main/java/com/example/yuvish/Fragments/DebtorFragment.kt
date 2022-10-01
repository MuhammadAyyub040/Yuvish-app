package com.example.yuvish.Fragments

import android.annotation.SuppressLint
import com.example.yuvish.R
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
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
import com.example.yuvish.Models.DebtorsPackage.DebtOff
import com.example.yuvish.Models.StringHelper
import com.example.yuvish.databinding.FragmentDebtorBinding
import com.example.yuvish.databinding.ItemWarnBinding
import com.example.yuvish.retrofit.ApiClient
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.orhanobut.hawk.Hawk
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
    private var debtOff: DebtOff? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDebtorBinding.inflate(layoutInflater)
        debtId = arguments?.getInt("debtId")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        debtorCustomer(debtId!!)

        val arrayAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            arrayListOf("2022", "2021")
        )
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        binding.backStack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnGiveUp.setOnClickListener {
            showDebtOffDialog()
        }

        binding.date.setOnClickListener {
            showDatePicker()
        }

        binding.btnConfirmation.setOnClickListener {
            checkDebtPayDate()
        }

    }

    private fun checkDebtPayDate() {
        val date = binding.date.text.toString()

        if (date == "--.--.----") {
            Toast.makeText(requireActivity(), getString(R.string.date_empty), Toast.LENGTH_SHORT)
                .show()
        } else if (!date.isAfterToday()) {
            Toast.makeText(
                requireActivity(),
                getString(R.string.after_today_date_error),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            confirmDebt = ConfirmDebt(changeDateStructure(date), debtId!!)
            putDebt(confirmDebt!!)
        }
    }

    private fun changeDateStructure(date: String): String {
        val dateArray = date.split(".") //dd.MM.yyyy
        return "${dateArray[2]}-${dateArray[1]}-${dateArray[0]}" //yyyy-MM-dd
    }

    private fun showDatePicker() {
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener {
            if (it.isAfterToday()) {
                binding.date.setText(getDateStringFormat(it))
            } else {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.after_today_date_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        picker.show(requireActivity().supportFragmentManager, "tag_picker")
    }

    private fun Long.isAfterToday(): Boolean {
        return Date(this).after(Date(Date().time - 86400000))
    }

    private fun String.isAfterToday(): Boolean {
        val dateArray = this.split(".")

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.DAY_OF_MONTH, dateArray[0].toInt())
        calendar.set(Calendar.MONTH, dateArray[1].toInt() - 1)
        calendar.set(Calendar.YEAR, dateArray[2].toInt())

        return calendar.timeInMillis.isAfterToday()
    }

    private fun getDateStringFormat(it: Long): String {
        return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(it))
    }

    private fun showSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    private fun sendBackStackRefreshRequest() {
        setFragmentResult(
            "refreshRequest",
            bundleOf("refreshPermission" to true)
        )
    }

    private fun putDebt(confirmDebt: ConfirmDebt) {
        ApiClient.retrofitService.putDebt(confirmDebt).enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.code() == 200)
                    if (response.body() == null) {
                        Toast.makeText(requireActivity(), getString(R.string.error), Toast.LENGTH_SHORT).show()
                    } else {
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

    private fun requestDebtOff(debtOff: DebtOff){
        ApiClient.retrofitService.requestDebtOff(debtOff).enqueue(object : Callback<String?>{
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.code() == 200){
                }
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
                if (response.code() == 200) {
                    if (response.body() != null) {
                        debtors = response.body()!!
                        binding.debtorCustomerName.text = debtors.costumer.costumer_name
                        binding.debtorCustomerId.text = debtors.costumer.id.toString()
                        binding.debtorCustomerLocation.text = debtors.costumer.costumer_addres
                        binding.edtDebtAmount.setText(debtors.summa.toString())
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

    @SuppressLint("SetTextI18n")
    private fun showDebtOffDialog(){
        val customDialog = AlertDialog.Builder(requireActivity()).create()
        val dialogBinding = ItemWarnBinding.inflate(layoutInflater)
        customDialog.setView(dialogBinding.root)
        customDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val description = getString(R.string.debt_off_name_and_amount)
            .replace("\'name\'", debtors.costumer.costumer_name ?: "")
            .replace("\'amount\'", debtors.summa.toString() ?: "")
        dialogBinding.txtWarn.text = description

        dialogBinding.cardWarnClose.setOnClickListener {
            customDialog.dismiss()
        }

        dialogBinding.btnGiveUp.setOnClickListener {
            val comment = dialogBinding.commentWarn.text.toString()

            if (comment.length <= 3){
                dialogBinding.commentWarn.error = getString(R.string.izoh_3_ta_belgidan_ko_p_bo_lishi_shart)
            }else{
                debtOff = DebtOff(comment, debtId!!)
                requestDebtOff(debtOff!!)
                customDialog.dismiss()
            }
        }

        customDialog.show()
    }

}