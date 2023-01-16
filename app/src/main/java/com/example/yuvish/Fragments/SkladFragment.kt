package com.example.yuvish.Fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.yuvish.Adapters.WerehousePaginationAdapter
import com.example.yuvish.models.DebtorsAPI.Market.ResponseDetail
import com.example.yuvish.models.HolatPaneli.WashingStatusAPI
import com.example.yuvish.models.Warehouse.PaginationPageWerehouse
import com.example.yuvish.models.Warehouse.WarehouseData
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentSkladBinding
import com.example.yuvish.retrofit.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SkladFragment : Fragment(), WerehousePaginationAdapter.OnItemClick {

    lateinit var binding: FragmentSkladBinding
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var werehousePaginationAdapter: WerehousePaginationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSkladBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        werehousePaginationAdapter = WerehousePaginationAdapter(requireActivity(), this)
        getPaginationWarehouse()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statusBar()

        binding.btnSearch.setOnClickListener {
            findNavController().navigate(R.id.action_skladFragment_to_globalSearchFragment)
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

        binding.skladVp.adapter = werehousePaginationAdapter

        binding.navView.setNavigationItemSelectedListener {

            when (it.itemId) {

                R.id.base -> {
                    Toast.makeText(
                        requireActivity(),
                        " Asosiy bo'lim tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_skladFragment_to_baseFragment)
                }
                R.id.new_order -> {
                    Toast.makeText(
                        requireActivity(),
                        " Yangi buyurtmalar tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_skladFragment_to_transportFragment)
                }
                R.id.washing -> {
                    Toast.makeText(
                        requireActivity(),
                        " Yuvish bo'limi tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_skladFragment_to_homeFragment)
                }
                R.id.ready -> {
                    Toast.makeText(
                        requireActivity(),
                        " Tayyor buyurtmalar tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_skladFragment_to_tayyorFragment)
                }
                R.id.warehouse -> {
                    Toast.makeText(
                        requireActivity(),
                        " Sklad bo'limi tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_skladFragment_self)
                }
                R.id.employee_setting -> {
                    Toast.makeText(
                        requireActivity(),
                        " Xodim sozlamalari tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_skladFragment_to_settingFragment)
                }
                R.id.close -> {
                    Toast.makeText(
                        requireActivity(),
                        " Chiqish tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_skladFragment_to_loginFragment)
                }
                R.id.debtors -> {
                    Toast.makeText(
                        requireActivity(),
                        " Qarzdorlar bo'limi tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_skladFragment_to_debtorsDepartmentFragment)
                }
            }
            true

        }

    }

    fun getPaginationWarehouse() {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { PaginationPageWerehouse(ApiClient.retrofitService) }
        ).liveData.observe(this) {
            lifecycleScope.launch {
                werehousePaginationAdapter.submitData(it)
            }
        }
    }

    private fun closeKeyboard(view: View) {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.btnSearch.windowToken, 0)
    }

    override fun onItemClickWarehouse(warehouseData: WarehouseData) {
        findNavController().navigate(R.id.action_skladFragment_to_sumbitFragment, bundleOf(
            "orderId" to warehouseData.order_id
        ))
    }

    private fun orderWarehouse(orderId: Int){
        ApiClient.retrofitService.ordersWarehouse(orderId).enqueue(object : Callback<ResponseDetail>{
            override fun onResponse(call: Call<ResponseDetail>, response: Response<ResponseDetail>) {
                if (response.code() == 200) {
                    Toast.makeText(
                        requireActivity(),
                        "Yetkazishga muvaffaqiyatli o'tkazildi.",
                        Toast.LENGTH_SHORT
                    ).show()
                    werehousePaginationAdapter.refresh()
                }
            }

            override fun onFailure(call: Call<ResponseDetail>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Xatolik yuz berdi", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun statusBar(){
        ApiClient.retrofitService.statusBar().enqueue(object : Callback<WashingStatusAPI>{
            override fun onResponse(call: Call<WashingStatusAPI>, response: Response<WashingStatusAPI>) {
                if (response.code() == 200) {
                    binding.txtWashingWarehouse.text = response.body()!!.yuvilmaganlar_soni.toString()
                    binding.txtWashedWarehouse.text = response.body()!!.qadoqlanmaganlar_soni.toString()
                    binding.txtPackedWarehouse.text = response.body()!!.topshirilmaganlar_soni.toString()
                }
            }

            override fun onFailure(call: Call<WashingStatusAPI>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Server bilan bog'lanolmadik", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onItemClick2Warehouse(warehouseData: WarehouseData) {
        orderWarehouse(warehouseData.order_id)
    }

    override fun onItemClickPhoneNumber(warehouseData: WarehouseData) {
        intentCall(warehouseData.custumer.costumer_phone_1)
    }

    private fun intentCall(phoneNumber: String){
        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        startActivity(callIntent)
    }
}