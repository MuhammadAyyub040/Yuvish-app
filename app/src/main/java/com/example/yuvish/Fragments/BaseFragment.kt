package com.example.yuvish.Fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.fragment.findNavController
import com.example.yuvish.Models.StringHelper
import com.example.yuvish.R
import com.example.yuvish.databinding.*
import java.util.*

class BaseFragment : Fragment() {

    lateinit var binding: FragmentBaseBinding
    lateinit var toggle: ActionBarDrawerToggle
    var searchPage = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseBinding.inflate(layoutInflater)

        val arrayAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            arrayListOf("2022", "2021")
        )
        binding.autoCompleteTextViewBase.setAdapter(arrayAdapter)

        binding.btnX.setOnClickListener {
            binding.searchCard.visibility = View.GONE
            searchPage = false

            closeKeyboard(it)
        }

        binding.txtWashed.setOnClickListener {
            val dialogBinding = WashedItemBinding.inflate(layoutInflater)

            val myDialog = Dialog(requireActivity())
            myDialog.setContentView(dialogBinding.root)
            dialogBinding.autoCompleteWashedQator


            val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, arrayListOf("10", "20", "50", "70", "100") )

            dialogBinding.autoCompleteWashedQator.setAdapter(adapter)

            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()
        }

        binding.txtSubmitted.setOnClickListener {
            val dialogBinding = SubmittedItemBinding.inflate(layoutInflater)

            val myDialog = Dialog(requireActivity())
            myDialog.setContentView(dialogBinding.root)
            dialogBinding.autoCompleteSubmittedQator

            val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, arrayListOf("10", "20", "50", "70", "100") )

            dialogBinding.autoCompleteSubmittedQator.setAdapter(adapter)

            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()
        }

        binding.txtRewash.setOnClickListener {
            val dialogBinding = RewashItemBinding.inflate(layoutInflater)

            val myDialog = Dialog(requireActivity())
            myDialog.setContentView(dialogBinding.root)
            dialogBinding.autoCompleteTextViewQator
            dialogBinding.autoCompleteTextViewTuri

            val adapterTuri = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, arrayListOf("Hammasi", "Qadoqlashda", "Transportda", "Operatorda") )

            dialogBinding.autoCompleteTextViewTuri.setAdapter(adapterTuri)

            val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, arrayListOf("10", "20", "50", "70", "100") )

            dialogBinding.autoCompleteTextViewQator.setAdapter(adapter)

            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()
        }

        binding.txtKpi.setOnClickListener {
            val dialogBinding = KpiItemBinding.inflate(layoutInflater)

            val myDialog = Dialog(requireActivity())
            myDialog.setContentView(dialogBinding.root)

            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()
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

        binding.edtDatePicker.setOnClickListener {
            setDate()
        }

        binding.edtDatePicker2.setOnClickListener {
            setDate2()
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

    @RequiresApi(Build.VERSION_CODES.N)
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
                binding.edtDatePicker.setText(
                    StringHelper.parseDate(
                        "dd/MM/yyyy",
                        "dd MM yyyy",
                        date
                    )
                )
                binding.edtDatePicker.error = null

            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setDate2() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog2 = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val returnDate = "$dayOfMonth ${monthOfYear + 1} $year"
                val date = StringHelper.parseDate(
                    "dd MM yyyy",
                    "dd/MM/yyyy",
                    returnDate
                )
                val dateString = date
                binding.edtDatePicker2.setText(
                    StringHelper.parseDate(
                        "dd/MM/yyyy",
                        "dd MM yyyy",
                        date
                    )
                )
                binding.edtDatePicker2.error = null

            },
            year,
            month,
            day
        )
        datePickerDialog2.show()
    }

    private fun closeKeyboard(view: View) {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.edtId.windowToken, 0)
    }
}