package com.example.yuvish.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
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
import androidx.core.view.isGone
import androidx.navigation.fragment.findNavController
import com.example.yuvish.Models.BaseIndikatorsIndex.SearchIndicatorsResult
import com.example.yuvish.Models.DebtorsPackage.User
import com.example.yuvish.Models.Setting.Setting
import com.example.yuvish.R
import com.example.yuvish.databinding.*
import com.example.yuvish.retrofit.ApiClient
import com.example.yuvish.retrofit.GlobalData
import com.example.yuvish.retrofit.isNotNull
import com.example.yuvish.retrofit.isNull
import com.google.android.material.datepicker.MaterialDatePicker
import com.orhanobut.hawk.Hawk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class BaseFragment : Fragment() {

    lateinit var binding: FragmentBaseBinding
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var setting: Setting
    private var searchIndicatorsResult: SearchIndicatorsResult? = null
    var searchPage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseBinding.inflate(layoutInflater)

        resetDate()
        profile()

        if (searchIndicatorsResult.isNull()) {
            checkSearching()
        } else {
            updateUI(searchIndicatorsResult!!)
            updateAttendance(setting)
        }

        val arrayAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            arrayListOf("2022", "2021")
        )

        searchIndicators("fromDate", "toDate")

        binding.autoCompleteTextViewBase.setAdapter(arrayAdapter)

        binding.btnX.setOnClickListener {
            binding.searchCard.visibility = View.GONE
            searchPage = false

            closeKeyboard()
        }

        binding.txtWashed.setOnClickListener {
            val bundle = getDateBundle()
            if (bundle.isNotNull()) {
                findNavController().navigate(R.id.washedIndicatorsFragment,bundle)
            }
        }

        binding.txtSubmitted.setOnClickListener {
            val bundle = getDateBundle()
            if (bundle.isNotNull()) {
                findNavController().navigate(R.id.submittedIndicatorsFragment, bundle)

            }
        }

        binding.txtRewash.setOnClickListener {
            val bundle = getDateBundle()
            if (bundle.isNotNull()) {
                findNavController().navigate(R.id.rewashReceivedFragment, bundle)

            }
        }

        binding.txtKpi.setOnClickListener {
            val bundle = getDateBundle()
            if (bundle.isNotNull()) {
                findNavController().navigate(R.id.kpiIndicatorsFragment, bundle)

            }
        }

        binding.txtSalary.setOnClickListener {
            val bundle = getDateBundle()
            if (bundle.isNotNull()) {
                findNavController().navigate(R.id.receivedSalaryFragment, bundle)

            }
        }

        binding.btnSearchBase.setOnClickListener {
            GlobalData.Global_Ui = true
            checkSearching()

        }

        binding.resetDate.setOnClickListener {
            resetDate()
            checkSearching()
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
            ActionBarDrawerToggle(
                requireActivity(),
                binding.drawerLayout,
                R.string.open,
                R.string.close
            )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.fromDateLayout.setEndIconOnClickListener {
            showDatePicker(0)
        }

        binding.toDateLayout.setEndIconOnClickListener {
            showDatePicker(1)
        }

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
                R.id.debtors -> {
                    Toast.makeText(
                        requireActivity(),
                        " Qarzdorlar bo'limi tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.debtorsDepartmentFragment)
                }
                R.id.close -> {
                    Toast.makeText(
                        requireActivity(),
                        " Chiqish tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.loginFragment)
                }
            }
            true

        }

        return binding.root
    }

    private fun getDateBundle(): Bundle? {
        val fromDate = binding.fromDateXml.text.toString()
        val toDate = binding.toDateXml.text.toString()

        return if (checkCorrectDate(fromDate, toDate)) {
            bundleOf(
                "fromDate" to fromDate.changeDateStructure(),
                "toDate" to toDate.changeDateStructure()
            )
        } else {
            null
        }
    }

    fun String.changeDateStructure(): String {
        val dateArray = this.split(".") //dd.MM.yyyy
        return "${dateArray[2]}-${dateArray[1]}-${dateArray[0]}" //yyyy-MM-dd
    }


    private fun checkSearching() {
        val fromDate = binding.fromDateXml.text.toString()
        val toDate = binding.toDateXml.text.toString()


        if (checkCorrectDate(fromDate, toDate)) {
            searchIndicators(
                fromDate.changeDateStructure(),
                toDate.changeDateStructure()
            )
        }
    }

    private fun checkCorrectDate(fromDate: String, toDate: String): Boolean {
        return when {
            fromDate == "--.--.----" -> {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.from_date_empty),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            toDate == "--.--.----" -> {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.to_date_empty),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            fromDate.isBefore(getBeginningMonth()) -> {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.entered_date_beginning_this_month),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun showDatePicker(from: Int) {
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener {
            when (from) {
                0 -> {
                    if (it.isBefore(getBeginningMonth())) {
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.entered_date_beginning_this_month),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        binding.fromDateXml.setText(getDateStringFormat(it))
                    }
                }
                1 -> {
                    binding.toDateXml.setText(getDateStringFormat(it))
                }
            }
        }

        picker.show(requireActivity().supportFragmentManager, "tag_picker")
    }

    private fun getBeginningMonth(): Long {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        return calendar.timeInMillis
    }

    private fun resetDate() {
        binding.toDateXml.setText(getDateStringFormat(Date().time))
        binding.fromDateXml.setText(getDateStringFormat(Date().time))
    }

    private fun Long.isBefore(it: Long): Boolean {
        return Date(this).before(Date(it))
    }

    private fun String.isBefore(it: Long): Boolean {
        val dateArray = this.split(".")

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.DAY_OF_MONTH, dateArray[0].toInt())
        calendar.set(Calendar.MONTH, dateArray[1].toInt() - 1)
        calendar.set(Calendar.YEAR, dateArray[2].toInt())

        return calendar.timeInMillis.isBefore(it)
    }

    private fun getDateStringFormat(it: Long): String {
        return SimpleDateFormat("dd.MM.yyyy").format(Date(it))
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(searchIndicatorsResult: SearchIndicatorsResult) {
        binding.washedPcs.text = "${searchIndicatorsResult.mahsulot_qadoqlandi.tovar_dona} ${getString(R.string.pcs)}"
        binding.washedMeterSquare.text = "${searchIndicatorsResult.mahsulot_qadoqlandi.tovar_kv} ${getString(R.string.m)}"
        binding.submitPcs.text = "${searchIndicatorsResult.mahsulot_topshirildi.tovar_dona} ${getString(R.string.pcs)}"
        binding.submitMeterSquare.text = "${searchIndicatorsResult.mahsulot_topshirildi.tovar_kv} ${getString(R.string.m)}"
        binding.salaryKpi.text = "${searchIndicatorsResult.kunlik_maosh} ${getString(R.string.so_m)}"
        binding.salaryReceived.text = "${searchIndicatorsResult.berilgan_maoshlar_sum} ${getString(R.string.so_m)}"
        binding.rewashedPcs.text = "${searchIndicatorsResult.qayta_yuvishga_olindi.tovar_dona} ${getString(R.string.pcs)}"
        binding.rewashedMeterSquare.text = "${searchIndicatorsResult.qayta_yuvishga_olindi.tovar_kv} ${getString(R.string.m)}"
        updateArrivalTime(searchIndicatorsResult)
        updateGoneTime(searchIndicatorsResult)
    }

    private fun updateAttendance(setting: Setting){
        binding.customerNameBase.text = setting.fullname
    }

    private fun updateArrivalTime(searchIndicatorsResult: SearchIndicatorsResult) {
        binding.arrivalTime.text = if (searchIndicatorsResult.today_davomat?.keldi == 1) {
            searchIndicatorsResult.today_davomat.keldi_time
        } else {
            getString(R.string.did_not_come)
        }
    }

    private fun updateGoneTime(searchIndicatorsResult: SearchIndicatorsResult) {
        binding.goneTime.text = if (searchIndicatorsResult.today_davomat?.ketdi == 1) {
            searchIndicatorsResult.today_davomat.ketdi_time
        } else {
            getString(R.string.did_not_leave)
        }
    }

    private fun closeKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.edtId.windowToken, 0)
    }

    private fun searchIndicators(fromDate: String, toDate: String) {
        ApiClient.retrofitService.searchIndicators(fromDate, toDate).enqueue(object : Callback<SearchIndicatorsResult> {
                override fun onResponse(call: Call<SearchIndicatorsResult>, response: Response<SearchIndicatorsResult>) {
                    if (response.code() == 200) {
                        if (GlobalData.Global_Ui) {
                            searchIndicatorsResult = response.body()!!
                            binding.washedPcs.text = "${searchIndicatorsResult!!.mahsulot_yuvildi.tovar_dona} ${"dona"}"
                            binding.washedMeterSquare.text = "${searchIndicatorsResult!!.mahsulot_yuvildi.tovar_kv} ${"m²"}"
                            binding.submitPcs.text = "${searchIndicatorsResult!!.mahsulot_topshirildi.tovar_dona} ${"dona"}"
                            binding.submitMeterSquare.text = "${searchIndicatorsResult!!.mahsulot_topshirildi.tovar_kv} ${"m²"}"
                            binding.rewashedPcs.text = "${searchIndicatorsResult!!.qayta_yuvishga_olindi.tovar_dona} ${"dona"}"
                            binding.rewashedMeterSquare.text = "${searchIndicatorsResult!!.qayta_yuvishga_olindi.tovar_kv} ${"m²"}"
                            binding.salaryReceived.text = "${searchIndicatorsResult!!.berilgan_maoshlar_sum} ${"so'm"}"
                            binding.salaryKpi.text = "${searchIndicatorsResult!!.kunlik_maosh} ${"so'm"}"
                            updateUI(searchIndicatorsResult!!)
                        }
                    }
                }

                override fun onFailure(call: Call<SearchIndicatorsResult>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(requireContext(), "OnFailure", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun profile(){
        ApiClient.retrofitService.profile().enqueue(object : Callback<Setting>{
            override fun onResponse(call: Call<Setting>, response: Response<Setting>) {
                if (response.code() == 200){
                    setting = response.body()!!
                    binding.customerNameBase.setText(response.body()!!.fullname)
                    updateAttendance(setting)
                }
            }

            override fun onFailure(call: Call<Setting>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Server bilan bog'lanishda xatolik", Toast.LENGTH_SHORT).show()
            }

        })
    }

}