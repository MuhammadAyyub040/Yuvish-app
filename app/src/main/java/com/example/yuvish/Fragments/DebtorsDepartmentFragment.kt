package com.example.yuvish.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
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
import com.example.yuvish.Adapters.DebtorsCompletedAdapter
import com.example.yuvish.Adapters.MarkedPaginationAdapter
import com.example.yuvish.Models.ArrangedSubmit.PaymentTypesItem
import com.example.yuvish.Models.DebtorsAPI.Market.FilterDebtorsItem
import com.example.yuvish.Models.DebtorsAPI.Market.MarketPaginationItem
import com.example.yuvish.Models.DebtorsAPI.Market.PaginationPageMarked
import com.example.yuvish.Models.DebtorsAPI.Market.Paydebt
import com.example.yuvish.Models.DebtorsPackage.DebtOff
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentDebtorsDepartmentBinding
import com.example.yuvish.databinding.ItemWarnBinding
import com.example.yuvish.retrofit.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DebtorsDepartmentFragment : Fragment(), MarkedPaginationAdapter.OnItemClick,
    DebtorsCompletedAdapter.CallBack {

    lateinit var binding: FragmentDebtorsDepartmentBinding
    lateinit var markedPaginationAdapter: MarkedPaginationAdapter
    lateinit var unMarkedPaginationAdapter: DebtorsCompletedAdapter
    lateinit var list: List<PaymentTypesItem>
    lateinit var listFilter: List<FilterDebtorsItem>
    lateinit var toggle: ActionBarDrawerToggle
    private var selectedFilterPosition = 0

    private var debtOff: DebtOff? = null
    private var payDebt: Paydebt? = null

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
        unMarkedPaginationAdapter = DebtorsCompletedAdapter(requireActivity(),this)
        paymentTypes()
        driverTypes()
        listFilter = arrayListOf()
        list = arrayListOf()

        binding.autoCompleteTextViewDebtors.setOnItemClickListener { parent, view, position, id ->
            selectedFilterPosition = position
            getPaginationPageMarked(listFilter[position].value.toInt())
            getPaginationPageUnMarked(listFilter[position].value.toInt())
        }

        binding.btnSearch.setOnClickListener {
            findNavController().navigate(R.id.action_debtorsDepartmentFragment_to_globalSearchFragment)
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
                    findNavController().navigate(R.id.action_debtorsDepartmentFragment_to_baseFragment)
                }
                R.id.new_order -> {
                    Toast.makeText(
                        requireActivity(),
                        " Yangi buyurtmalar tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_debtorsDepartmentFragment_to_transportFragment)
                }
                R.id.washing -> {
                    Toast.makeText(
                        requireActivity(),
                        " Yuvish bo'limi tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_debtorsDepartmentFragment_to_homeFragment)
                }
                R.id.ready -> {
                    Toast.makeText(
                        requireActivity(),
                        " Tayyor buyurtmalar tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_debtorsDepartmentFragment_to_tayyorFragment)
                }
                R.id.warehouse -> {
                    Toast.makeText(
                        requireActivity(),
                        " Sklad bo'limi tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_debtorsDepartmentFragment_to_skladFragment)
                }
                R.id.employee_setting -> {
                    Toast.makeText(
                        requireActivity(),
                        " Xodim sozlamalari tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_debtorsDepartmentFragment_to_settingFragment)
                }
                R.id.close -> {
                    Toast.makeText(
                        requireActivity(),
                        " Chiqish tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_debtorsDepartmentFragment_to_loginFragment)
                }
                R.id.debtors -> {
                    Toast.makeText(
                        requireActivity(),
                        " Qarzdorlar bo'limi tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_debtorsDepartmentFragment_self)
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
            pagingSourceFactory = {
                PaginationPageMarked(
                    ApiClient.retrofitService,
                    driverId,
                    "marked"
                )
            }
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
            pagingSourceFactory = {
                PaginationPageMarked(
                    ApiClient.retrofitService,
                    driverId,
                    "marked"
                )
            }
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
                PaginationPageMarked(ApiClient.retrofitService, driverId, "unmarked")
            }
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
            pagingSourceFactory = {
                PaginationPageMarked(
                    ApiClient.retrofitService,
                    driverId,
                    "unmarked"
                )
            }
        ).liveData.observe(this) {
            lifecycleScope.launch {
                unMarkedPaginationAdapter.submitData(it)
            }
        }
    }

    private fun paymentDebt(paydebt: Paydebt) {
        ApiClient.retrofitService.requestPayDebt(paydebt).enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.code() == 200){
                    Toast.makeText(requireActivity(), "Pul muvaffaqiyatli olindi!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "OnFailure", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun driverTypes() {
        ApiClient.retrofitService.filterDebtors().enqueue(object : Callback<List<FilterDebtorsItem>> {

            override fun onResponse(call: Call<List<FilterDebtorsItem>>, response: Response<List<FilterDebtorsItem>>) {
                if (response.code() == 200) {
                    listFilter = response.body()!!
                    val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, listFilter.map { it.title })
                    binding.autoCompleteTextViewDebtors.setAdapter(arrayAdapter)
                 }
            }

            override fun onFailure(call: Call<List<FilterDebtorsItem>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "OnFailure", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun paymentTypes() {
        ApiClient.retrofitService.paymentTypes().enqueue(object : Callback<List<PaymentTypesItem>> {

            override fun onResponse(call: Call<List<PaymentTypesItem>>, response: Response<List<PaymentTypesItem>>) {

                if (response.code() == 200) {
                    list = response.body()!!
                    unMarkedPaginationAdapter.submitPaymentTypesList(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<PaymentTypesItem>>, t: Throwable) {
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

    private fun closeKeyboard(view: View) {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.btnSearch.windowToken, 0)
    }

    override fun onItemClickMarked(marketPaginationItem: MarketPaginationItem) {
        Log.e(TAG, "onItemClickMarked: ${marketPaginationItem.id}")
        findNavController().navigate(R.id.action_debtorsDepartmentFragment_to_debtorFragment, bundleOf(
                "debtId" to marketPaginationItem.id
            )
        )
    }

    override fun phoneItemClickMarked(marketPaginationItem: MarketPaginationItem) {
        intentCall(marketPaginationItem.costumer.costumer_phone_1)
    }

    @SuppressLint("SetTextI18n")
    private fun showDebtOffDialog(marketPaginationItem: MarketPaginationItem){
        val customDialog = AlertDialog.Builder(requireActivity()).create()
        val dialogBinding = ItemWarnBinding.inflate(layoutInflater)
        customDialog.setView(dialogBinding.root)
        customDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val description = getString(R.string.debt_off_name_and_amount)
            .replace("\'name\'", marketPaginationItem.costumer.costumer_name ?: "")
            .replace("\'amount\'", marketPaginationItem.summa.toString() ?: "")
        dialogBinding.txtWarn.text = description

        dialogBinding.cardWarnClose.setOnClickListener {
            customDialog.dismiss()
        }

        dialogBinding.btnGiveUp.setOnClickListener {
            val comment = dialogBinding.commentWarn.text.toString()

            if (comment.length <= 3){
                dialogBinding.commentWarn.error = getString(R.string.izoh_3_ta_belgidan_ko_p_bo_lishi_shart)
            }else{
                debtOff = DebtOff(comment, marketPaginationItem.id)
                requestDebtOff(debtOff!!)
                customDialog.dismiss()
            }
        }

        customDialog.show()
    }

    override fun phoneClickListener(marketPaginationItem: MarketPaginationItem) {
        intentCall(marketPaginationItem.costumer.costumer_phone_1)
    }

    private fun intentCall(phoneNumber: String){
        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        startActivity(callIntent)
    }

    override fun payDebtClickListener(payDebt: Paydebt) {
        this.payDebt = payDebt
        paymentDebt(payDebt)
        unMarkedPaginationAdapter.refresh()
        markedPaginationAdapter.refresh()
    }

    override fun debtOffClickListener(marketPaginationItem: MarketPaginationItem) {
        showDebtOffDialog(marketPaginationItem)
    }

}