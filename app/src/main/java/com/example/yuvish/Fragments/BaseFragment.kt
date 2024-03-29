package com.example.yuvish.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.yuvish.models.baseIndikatorsIndex.SearchIndicatorsResult
import com.example.yuvish.models.Setting.Setting
import com.example.yuvish.R
import com.example.yuvish.databinding.*
import com.example.yuvish.retrofit.ApiClient
import com.example.yuvish.retrofit.GlobalData
import com.example.yuvish.retrofit.isNotNull
import com.example.yuvish.retrofit.isNull
import com.google.android.material.datepicker.MaterialDatePicker
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

        binding.txtWashed.setOnClickListener {
            val bundle = getDateBundle()
            if (bundle.isNotNull()) {
                findNavController().navigate(R.id.action_baseFragment_to_washedIndicatorsFragment,bundle)
            }
        }

        binding.txtSubmitted.setOnClickListener {
            val bundle = getDateBundle()
            if (bundle.isNotNull()) {
                findNavController().navigate(R.id.action_baseFragment_to_submittedIndicatorsFragment, bundle)

            }
        }

        binding.txtRewash.setOnClickListener {
            val bundle = getDateBundle()
            if (bundle.isNotNull()) {
                findNavController().navigate(R.id.action_baseFragment_to_rewashReceivedFragment, bundle)

            }
        }

        binding.txtKpi.setOnClickListener {
            val bundle = getDateBundle()
            if (bundle.isNotNull()) {
                findNavController().navigate(R.id.action_baseFragment_to_kpiIndicatorsFragment, bundle)

            }
        }

        binding.txtSalary.setOnClickListener {
            val bundle = getDateBundle()
            if (bundle.isNotNull()) {
                findNavController().navigate(R.id.action_baseFragment_to_receivedSalaryFragment, bundle)

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
            findNavController().navigate(R.id.action_baseFragment_to_globalSearchFragment)
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
                    findNavController().navigate(R.id.action_baseFragment_self)
                }
                R.id.new_order -> {
                    Toast.makeText(
                        requireActivity(),
                        " Yangi buyurtmalar tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_baseFragment_to_transportFragment)
                }
                R.id.washing -> {
                    Toast.makeText(
                        requireActivity(),
                        " Yuvish bo'limi tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_baseFragment_to_homeFragment)
                }
                R.id.ready -> {
                    Toast.makeText(
                        requireActivity(),
                        " Tayyor buyurtmalar tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_baseFragment_to_tayyorFragment)
                }
                R.id.warehouse -> {
                    Toast.makeText(
                        requireActivity(),
                        " Sklad bo'limi tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_baseFragment_to_skladFragment)
                }
                R.id.employee_setting -> {
                    Toast.makeText(
                        requireActivity(),
                        " Xodim sozlamalari tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_baseFragment_to_settingFragment)
                }
                R.id.debtors -> {
                    Toast.makeText(
                        requireActivity(),
                        " Qarzdorlar bo'limi tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_baseFragment_to_debtorsDepartmentFragment)
                }
                R.id.close -> {
                    Toast.makeText(
                        requireActivity(),
                        " Chiqish tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_baseFragment_to_loginFragment)
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


    @SuppressLint("SuspiciousIndentation")
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
//            fromDate == "--.--.----" -> {
//                Toast.makeText(
//                    requireActivity(),
//                    getString(R.string.from_date_empty),
//                    Toast.LENGTH_SHORT
//                ).show()
//                false
//            }
//            toDate == "--.--.----" -> {
//                Toast.makeText(
//                    requireActivity(),
//                    getString(R.string.to_date_empty),
//                    Toast.LENGTH_SHORT
//                ).show()
//                false
//            }
//            fromDate.isBefore(getBeginningMonth()) -> {
//                Toast.makeText(
//                    requireActivity(),
//                    getString(R.string.entered_date_beginning_this_month),
//                    Toast.LENGTH_SHORT
//                ).show()
//                false
//            }
            else -> {
                true
            }
        }
    }

    private fun showDatePicker(from: Int) {
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

//        picker.addOnPositiveButtonClickListener {
//            when (from) {
//                0 -> {
//                    if (it.isBefore(getBeginningMonth())) {
//                        Toast.makeText(
//                            requireActivity(),
//                            getString(R.string.entered_date_beginning_this_month),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } else {
//                        binding.fromDateXml.setText(getDateStringFormat(it))
//                    }
//                }
//                1 -> {
//                    binding.toDateXml.setText(getDateStringFormat(it))
//                }
//            }
//        }

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
        binding.washedPcs.text = "${searchIndicatorsResult.yuvildi.dona} ${getString(R.string.pcs)}"
        binding.washedMeterSquare.text = "${searchIndicatorsResult.yuvildi.hajm} ${getString(R.string.m)}"
        binding.submitPcs.text = "${searchIndicatorsResult.topshirildi.dona} ${getString(R.string.pcs)}"
        binding.submitMeterSquare.text = "${searchIndicatorsResult.topshirildi.hajm} ${getString(R.string.m)}"
        binding.salaryKpi.text = "${searchIndicatorsResult.maosh} ${getString(R.string.so_m)}"
        binding.salaryReceived.text = "${searchIndicatorsResult.maosh_history} ${getString(R.string.so_m)}"
        binding.rewashedPcs.text = "${searchIndicatorsResult.qayta.dona} ${getString(R.string.pcs)}"
        binding.rewashedMeterSquare.text = "${searchIndicatorsResult.qayta.hajm} ${getString(R.string.m)}"
        updateArrivalTime(searchIndicatorsResult)
        updateGoneTime(searchIndicatorsResult)
    }

    private fun updateAttendance(setting: Setting){
        binding.customerNameBase.text = setting.fullname
    }

    private fun updateArrivalTime(searchIndicatorsResult: SearchIndicatorsResult) {
        if (searchIndicatorsResult.davomat.isNull()) {
            getString(R.string.did_not_leave)
        }else{
        binding.arrivalTime.text = if (searchIndicatorsResult.davomat.keldi == 1) {
            searchIndicatorsResult.davomat.keldi_time
        } else
            getString(R.string.did_not_leave)
        }
    }

    private fun updateGoneTime(searchIndicatorsResult: SearchIndicatorsResult)
    {if (searchIndicatorsResult.davomat.isNull()) {
        getString(R.string.did_not_leave)
    }else{
        binding.goneTime.text = if (searchIndicatorsResult.davomat.ketdi == 1) {
            searchIndicatorsResult.davomat.ketdi_time
        } else
            getString(R.string.did_not_leave)
        }
    }

    private fun closeKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.btnSearch.windowToken, 0)
    }

    private fun searchIndicators(fromDate: String, toDate: String) {
        ApiClient.retrofitService.searchIndicators(fromDate, toDate).enqueue(object : Callback<SearchIndicatorsResult> {
                override fun onResponse(call: Call<SearchIndicatorsResult>, response: Response<SearchIndicatorsResult>) {
                    Log.e("TAG", "onResponse: ${response.code()} ${response.body()}")
                    if (response.code() == 200) {
                        Log.e("TAG", "onResponse: ${response.code()} ${response.body()}")
                        if (GlobalData.Global_Ui) {
                            searchIndicatorsResult = response.body()!!
                            binding.washedPcs.text = "${searchIndicatorsResult!!.yuvildi.dona} ${"dona"}"
                            binding.washedMeterSquare.text = "${searchIndicatorsResult!!.yuvildi.hajm} ${"m²"}"
                            binding.submitPcs.text = "${searchIndicatorsResult!!.topshirildi.dona} ${"dona"}"
                            binding.submitMeterSquare.text = "${searchIndicatorsResult!!.topshirildi.hajm} ${"m²"}"
                            binding.rewashedPcs.text = "${searchIndicatorsResult!!.qayta.dona} ${"dona"}"
                            binding.rewashedMeterSquare.text = "${searchIndicatorsResult!!.qayta.hajm} ${"m²"}"
                            binding.salaryReceived.text = "${searchIndicatorsResult!!.maosh_history} ${"so'm"}"
                            binding.salaryKpi.text = "${searchIndicatorsResult!!.maosh} ${"so'm"}"
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
                    binding.customerNameBase.text = response.body()!!.fullname
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