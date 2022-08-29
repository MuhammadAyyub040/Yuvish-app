package com.example.yuvish.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.example.yuvish.Models.Authorization.UserToken
import com.example.yuvish.Models.LanguageHelper
import com.example.yuvish.Models.Setting.Setting
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentSettingBinding
import com.example.yuvish.databinding.ItemThemeBinding
import com.example.yuvish.retrofit.ApiClient
import com.orhanobut.hawk.Hawk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingFragment : Fragment() {

    lateinit var binding: FragmentSettingBinding
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var hawk: Hawk
    lateinit var setting: Setting
    lateinit var userToken: UserToken
    var searchPage = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        profile()

        return binding.root
    }

    override fun onResume() {
        val arrayAdapter2 = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, arrayListOf("English", "Русский", "O'zbek"))
        binding.autoCompleteTextViewSettingLanguage.setAdapter(arrayAdapter2)
        super.onResume(
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTheme.setOnClickListener {
            chooseThemeDialog()
        }

        binding.autoCompleteTextViewSettingLanguage.setOnItemClickListener { parent, view, position, id ->
            when (position) {
                0 -> {

                    LanguageHelper.setLocale(requireActivity(),"en")
                }
                1 -> {
                    LanguageHelper.setLocale(requireActivity(),"ru")
                }
                else ->{
                    LanguageHelper.setLocale(requireActivity(), "uzb")
                }
            }
            findNavController().navigate(R.id.settingFragment)
        }

        val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, arrayListOf("2022", "2021"))
        binding.autoCompleteTextViewSetting.setAdapter(arrayAdapter)

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

        binding.btnSaveSetting.setOnClickListener {
            when{
                binding.edtLoginSetting.text.toString() == setting.username &&
                        binding.edtNameSetting.text.toString() == setting.fullname &&
                        binding.edtPhoneNumber.text.toString() == setting.phone.toString()->{

                        }
                else ->{

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

    private fun chooseThemeDialog() {
        val builder = AlertDialog.Builder(requireActivity()).create()
        val dialogBinding = ItemThemeBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)

        dialogBinding.checkboxLight.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            builder.dismiss()
        }
        dialogBinding.checkboxDark.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            builder.dismiss()
        }
        dialogBinding.checkboxDefault.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            builder.dismiss()
        }

        builder.show()
    }

    private fun profile(){
        ApiClient.retrofitService.profile().enqueue(object : Callback<Setting>{
            override fun onResponse(call: Call<Setting>, response: Response<Setting>) {
                setting = response.body()!!
                binding.edtNameSetting.setText(response.body()!!.fullname)
                binding.edtPhoneNumber.setText(response.body()!!.phone.toString())
                binding.edtLoginSetting.setText(response.body()!!.username)
            }

            override fun onFailure(call: Call<Setting>, t: Throwable) {
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
}