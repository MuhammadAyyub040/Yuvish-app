package com.example.yuvish.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.fragment.findNavController
import com.example.yuvish.Adapters.ViewPagerAdapter2
import com.example.yuvish.Adapters.ViewPagerAdapter3
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentTayyorBinding
import com.example.yuvish.databinding.SortingItemBinding

class TayyorFragment : Fragment() {

    lateinit var toggle: ActionBarDrawerToggle
    var searchPage = false
    lateinit var binding: FragmentTayyorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTayyorBinding.inflate(layoutInflater)
        val arrayAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            arrayListOf("2022", "2021")
        )
        binding.autoCompleteTextViewReady.setAdapter(arrayAdapter)

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

        binding.txtSkladId.setOnClickListener {
            findNavController().navigate(R.id.skladFragment)
        }



        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.open()
        }

        binding.viewPager.adapter = ViewPagerAdapter2(
            object : ViewPagerAdapter2.OnItemClick {
                override fun onItemClick(position: Int) {
                    findNavController().navigate(R.id.sumbitFragment)
                }

                override fun onItemClick2(position: Int) {
                    val dialogBinding = SortingItemBinding.inflate(layoutInflater)

                    val myDialog = Dialog(requireActivity())
                    myDialog.setContentView(dialogBinding.root)

                    myDialog.setCancelable(true)
                    myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    myDialog.show()
                }
            },
            arrayListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
        )
        binding.viewPager2.adapter = ViewPagerAdapter3(
            object : ViewPagerAdapter3.OnItemClick {
                override fun onItemClick(position: Int) {
                    findNavController().navigate(R.id.sumbitFragment)
                }

                override fun onItemClick2(position: Int) {
                    val dialogBinding = SortingItemBinding.inflate(layoutInflater)

                    val myDialog = AlertDialog.Builder(requireActivity()).create()
                    myDialog.setView(dialogBinding.root)

                    dialogBinding.cardClose.setOnClickListener {
                        myDialog.dismiss()
                    }

                    myDialog.setCancelable(true)
                    myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    myDialog.show()
                }
            },
            arrayListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
        )

        toggle =
            ActionBarDrawerToggle(
                requireActivity(),
                binding.drawerLayout,
                R.string.open,
                R.string.close
            )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

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

        return binding.root
    }

    private fun closeKeyboard(view: View) {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.edtId.windowToken, 0)
    }
}