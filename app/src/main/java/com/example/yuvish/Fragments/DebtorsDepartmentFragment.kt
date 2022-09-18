package com.example.yuvish.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.yuvish.Adapters.MarkedPaginationAdapter
import com.example.yuvish.Adapters.UnMarkedPaginationAdapter
import com.example.yuvish.Models.DebtorsAPI.Market.FilterDebtorsItem
import com.example.yuvish.Models.DebtorsAPI.Market.MarketPaginationItem
import com.example.yuvish.Models.DebtorsAPI.Market.PaginationPageMarked
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentDebtorsDepartmentBinding
import com.example.yuvish.databinding.ItemWarnBinding
import com.example.yuvish.retrofit.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DebtorsDepartmentFragment : Fragment(), MarkedPaginationAdapter.OnItemClick,
    UnMarkedPaginationAdapter.OnItemClick {

    lateinit var binding: FragmentDebtorsDepartmentBinding
    lateinit var markedPaginationAdapter: MarkedPaginationAdapter
    lateinit var unMarkedPaginationAdapter: UnMarkedPaginationAdapter
    lateinit var list: List<FilterDebtorsItem>
    lateinit var toggle: ActionBarDrawerToggle
    private var selectedFilterPosition = 0
    var searchPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialGetPaginationPageMarked(0)
        initialGetPaginationPageUnMarked(0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDebtorsDepartmentBinding.inflate(layoutInflater)

        markedPaginationAdapter = MarkedPaginationAdapter(this, requireActivity())
        unMarkedPaginationAdapter = UnMarkedPaginationAdapter(this, requireActivity())
        filterDebtor()
        list = arrayListOf()

        val arrayAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            arrayListOf("2022", "2021")
        )
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        binding.btnX.setOnClickListener {
            binding.searchCard.visibility = View.GONE
            searchPage = false

            closeKeyboard(it)
        }

        binding.autoCompleteTextViewDebtors.setOnItemClickListener { parent, view, position, id ->
            selectedFilterPosition = position
            getPaginationPageMarked(list[position].value)
            getPaginationPageUnMarked(list[position].value)
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

        binding.rvNotfinishingDebtors.adapter = markedPaginationAdapter
        binding.rvDebtors.adapter = unMarkedPaginationAdapter

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

    fun getPaginationPageMarked(driverId: Int) {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { PaginationPageMarked(ApiClient.retrofitService, driverId, "marked") }
        ).liveData.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                markedPaginationAdapter.submitData(it)
            }
        }
    }

    fun initialGetPaginationPageMarked(driverId: Int) {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { PaginationPageMarked(ApiClient.retrofitService, driverId, "marked") }
        ).liveData.observe(this) {
            lifecycleScope.launch {
                markedPaginationAdapter.submitData(it)
            }
        }
    }

    fun getPaginationPageUnMarked(driverId: Int) {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                PaginationPageMarked(ApiClient.retrofitService, driverId, "unmarked") }
        ).liveData.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                unMarkedPaginationAdapter.submitData(it)
            }
        }
    }

    fun initialGetPaginationPageUnMarked(driverId: Int) {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { PaginationPageMarked(ApiClient.retrofitService, driverId, "unmarked") }
        ).liveData.observe(this) {
            lifecycleScope.launch {
                unMarkedPaginationAdapter.submitData(it)
            }
        }
    }

    private fun filterDebtor(){
        ApiClient.retrofitService.filterDebtors().enqueue(object : Callback<List<FilterDebtorsItem>> {
            override fun onResponse(call: Call<List<FilterDebtorsItem>>, response: Response<List<FilterDebtorsItem>>) {

                if (response.code() == 200){
                    list = response.body()!!
                    val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, list.map { it.title })
                    binding.autoCompleteTextViewDebtors.setAdapter(arrayAdapter)
                }
            }

            override fun onFailure(call: Call<List<FilterDebtorsItem>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "OnFailure", Toast.LENGTH_SHORT).show() } })
    }

    private fun closeKeyboard(view: View) {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.edtId.windowToken, 0)
    }

    override fun onItemClickMarked(marketPaginationItem: MarketPaginationItem) {
        Log.e("TAG", "onItemClickUnMarked ${marketPaginationItem.id}")
        findNavController().navigate(R.id.debtorFragment, bundleOf(
            "id" to marketPaginationItem.id
        )
        )
    }

    override fun onItemClickUnMarked(marketPaginationItem: MarketPaginationItem) {
        findNavController().navigate(R.id.registrationFragment, bundleOf(
            "orderId" to marketPaginationItem.order_id
        ))
    }

    override fun onItemClickUnMarked2(marketPaginationItem: MarketPaginationItem) {
        val dialogBinding = ItemWarnBinding.inflate(layoutInflater)

        val myDialog = AlertDialog.Builder(requireActivity()).create()
        myDialog.setView(dialogBinding.root)

        dialogBinding.cardWarnClose.setOnClickListener {
            myDialog.dismiss()
        }

        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()
    }

}