package com.example.yuvish.Fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.yuvish.Adapters.CleaningPaginationAdapter
import com.example.yuvish.Models.Cleaning.PaginationPageCleaning
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentHomeBinding
import com.example.yuvish.retrofit.ApiClient
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), CleaningPaginationAdapter.OnItemClick {

    lateinit var binding: FragmentHomeBinding
    lateinit var cleaningPaginationAdapter: CleaningPaginationAdapter
    lateinit var cleaningPaginationAdapter2: CleaningPaginationAdapter
    lateinit var toggle: ActionBarDrawerToggle
    var searchPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cleaningPaginationAdapter = CleaningPaginationAdapter(requireActivity(), this)
        cleaningPaginationAdapter2 = CleaningPaginationAdapter(requireActivity(),this)
        getPaginationCleaning()
        getPaginationRecleaning()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, arrayListOf("2022", "2021"))
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

        binding.txtHomeId.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        binding.txtWashedId.setOnClickListener {
            findNavController().navigate(R.id.baseFragment)
        }
        binding.txtReadyId.setOnClickListener {
            findNavController().navigate(R.id.tayyorFragment)
        }

        binding.washed.adapter = cleaningPaginationAdapter

        binding.rewashVp.adapter = cleaningPaginationAdapter2

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

        return binding.root
    }

    fun getPaginationCleaning() {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { PaginationPageCleaning(ApiClient.retrofitService, "cleaning") }
        ).liveData.observe(this) {
            lifecycleScope.launch {
                cleaningPaginationAdapter.submitData(it)
            }
        }
    }

    fun getPaginationRecleaning() {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { PaginationPageCleaning(ApiClient.retrofitService, "recleaning") }
        ).liveData.observe(this) {
            lifecycleScope.launch {
                cleaningPaginationAdapter2.submitData(it)
            }
        }
    }

    private fun closeKeyboard(view: View) {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.edtId.windowToken, 0)
    }

    override fun onItemClickCleaning(position: Int) {
        findNavController().navigate(R.id.registrationFragment)
    }

}