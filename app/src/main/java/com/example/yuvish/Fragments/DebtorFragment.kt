package com.example.yuvish.Fragments

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
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
import com.example.yuvish.databinding.FragmentDebtorBinding
import com.example.yuvish.databinding.ItemWarnBinding
import java.util.*

class DebtorFragment : Fragment() {

    lateinit var binding: FragmentDebtorBinding
    lateinit var toggle: ActionBarDrawerToggle
    var searchPage = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDebtorBinding.inflate(layoutInflater)

        val arrayAdapter = ArrayAdapter(
            requireActivity(),
            R.layout.simple_list_item_1,
            arrayListOf("2022", "2021")
        )
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        binding.btnX.setOnClickListener {
            binding.searchCard.visibility = View.GONE
            searchPage = false

            closeKeyboard(it)
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

        binding.btnConfirmation.setOnClickListener {
            val warn = binding.datePicerDebtor.text.toString().trim()

            if (warn.isEmpty()) {
                binding.datePicerDebtor.error = "Sanani kiriting"
                binding.datePicerDebtor.requestFocus()
            } else
                findNavController().navigate(com.example.yuvish.R.id.debtorFragment)
        }

        toggle = ActionBarDrawerToggle(
            requireActivity(),
            binding.drawerLayout,
            com.example.yuvish.R.string.open,
            com.example.yuvish.R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.open()
        }

        binding.datePicerDebtor.setOnClickListener {
            setDate()
        }

        binding.navView.setNavigationItemSelectedListener {

            when (it.itemId) {

                com.example.yuvish.R.id.base -> {
                    Toast.makeText(
                        requireActivity(),
                        " Asosiy bo'lim tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(com.example.yuvish.R.id.baseFragment)
                }
                com.example.yuvish.R.id.new_order -> {
                    Toast.makeText(
                        requireActivity(),
                        " Yangi buyurtmalar tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(com.example.yuvish.R.id.transportFragment)
                }
                com.example.yuvish.R.id.washing -> {
                    Toast.makeText(
                        requireActivity(),
                        " Yuvish bo'limi tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(com.example.yuvish.R.id.homeFragment)
                }
                com.example.yuvish.R.id.ready -> {
                    Toast.makeText(
                        requireActivity(),
                        " Tayyor buyurtmalar tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(com.example.yuvish.R.id.tayyorFragment)
                }
                com.example.yuvish.R.id.warehouse -> {
                    Toast.makeText(
                        requireActivity(),
                        " Sklad bo'limi tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(com.example.yuvish.R.id.skladFragment)
                }
                com.example.yuvish.R.id.employee_setting -> {
                    Toast.makeText(
                        requireActivity(),
                        " Xodim sozlamalari tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(com.example.yuvish.R.id.settingFragment)
                }
                com.example.yuvish.R.id.close -> {
                    Toast.makeText(
                        requireActivity(),
                        " Chiqish tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(com.example.yuvish.R.id.loginFragment)
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
                binding.datePicerDebtor.setText(
                    StringHelper.parseDate(
                        "dd/MM/yyyy",
                        "dd MM yyyy",
                        date
                    )
                )
                binding.datePicerDebtor.error = null

            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun closeKeyboard(view: View) {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.edtId.windowToken, 0)
    }

}