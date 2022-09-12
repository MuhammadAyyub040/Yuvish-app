package com.example.yuvish.Fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.yuvish.Adapters.ArrangedPaginationAdapter
import com.example.yuvish.Adapters.NotArrangedPaginationAdapter
import com.example.yuvish.Models.ArrangedSubmit.Submit
import com.example.yuvish.Models.HolatPaneli.TransportStatusAPI
import com.example.yuvish.Models.ReadyOrders.*
import com.example.yuvish.R
import com.example.yuvish.databinding.*
import com.example.yuvish.retrofit.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TayyorFragment : Fragment(), ArrangedPaginationAdapter.OnItemClick, NotArrangedPaginationAdapter.OnItemClick {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var arrangedPaginationAdapter: ArrangedPaginationAdapter
    lateinit var notArrangedPaginationAdapter: NotArrangedPaginationAdapter
    lateinit var submit: Submit
    lateinit var list: List<Autocomplete>
    lateinit var binding: FragmentTayyorBinding
    lateinit var arranging: Arranging
    private var placeHolderPermission = true
    private var selectedFilterPosition = 0
    var searchPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arrangedPaginationAdapter = ArrangedPaginationAdapter(requireActivity(),this)
        notArrangedPaginationAdapter = NotArrangedPaginationAdapter(requireActivity(), this)
        initialGetPaginationNotArranged(driverId = 0)
        initialGetPaginationArranged(driverId = 0)
        autocompleteAll()
        transportStatus()
        list = arrayListOf()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTayyorBinding.inflate(layoutInflater)

        val arrayAdapter2 = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, arrayListOf("2022", "2021"))
        binding.autoCompleteTextViewReady.setAdapter(arrayAdapter2)

        binding.btnX.setOnClickListener {
            binding.searchCard.visibility = View.GONE
            searchPage = false

            closeKeyboard()
        }

        binding.autoCompleteTextViewList.setOnItemClickListener { parent, view, position, id ->
            selectedFilterPosition = position
            getPaginationArranged(list[position].value)
            getPaginationNotArranged(list[position].value)

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

        binding.txtWarehouseTransport.setOnClickListener {
            findNavController().navigate(R.id.skladFragment)
        }



        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.open()
        }

        binding.viewPager.adapter = arrangedPaginationAdapter

        binding.viewPager2.adapter = notArrangedPaginationAdapter

        toggle = ActionBarDrawerToggle(requireActivity(), binding.drawerLayout, R.string.open, R.string.close)

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

    fun getPaginationArranged(driverId: Int) {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { PaginationPageArranged(ApiClient.retrofitService, driverId, "arranged") }
        ).liveData.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                arrangedPaginationAdapter.submitData(it)
            }
        }
    }

    fun initialGetPaginationArranged(driverId: Int) {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { PaginationPageArranged(ApiClient.retrofitService, driverId, "arranged") }
        ).liveData.observe(this) {
            lifecycleScope.launch {
                arrangedPaginationAdapter.submitData(it)
            }
        }
    }

    fun getPaginationNotArranged(driverId: Int) {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { PaginationPageArranged(ApiClient.retrofitService, driverId, "notarranged") }
        ).liveData.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                notArrangedPaginationAdapter.submitData(it)
            }
        }
    }

    fun initialGetPaginationNotArranged(driverId: Int) {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { PaginationPageArranged(ApiClient.retrofitService, driverId, "notarranged") }
        ).liveData.observe(this) {
            lifecycleScope.launch {
                notArrangedPaginationAdapter.submitData(it)
            }
        }
    }

    private fun closeKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.edtId.windowToken, 0)
    }

    override fun onItemClickSubmit(readyOrdersItem: ReadyOrdersItem) {
        Log.d("TAG", "onItemClickSubmit ${readyOrdersItem.order_id} ")
        findNavController().navigate(R.id.sumbitFragment, bundleOf(
            "orderId" to readyOrdersItem.order_id
        ))
    }

    override fun onItemClickLocation(readyOrdersItem: ReadyOrdersItem) {
        intentGoogleMaps(readyOrdersItem.geoplugin_latitude, readyOrdersItem.geoplugin_longitude)
        Toast.makeText(requireActivity(), "Xaritalarga o'tildi", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClickViewOrder(readyOrdersItem: ReadyOrdersItem) {
        viewOrder(readyOrdersItem.order_id)
    }

    override fun onItemClickUnsorted(readyOrdersItem: ReadyOrdersItem) {
            val dialogBinding = SortingItemBinding.inflate(layoutInflater)

            val myDialog = Dialog(requireActivity())
            myDialog.setContentView(dialogBinding.root)

            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()

        dialogBinding.cardClose.setOnClickListener {
            myDialog.dismiss()
        }
        dialogBinding.check.setOnClickListener {
            val comment = dialogBinding.sortingComment.text.toString()
            val sortNumber = dialogBinding.idNumberSorting.text.toString().toIntOrNull() ?: 0
            if (comment.isNotEmpty() || comment.length >= 3){
                arranging = Arranging(comment, readyOrdersItem.order_id, sortNumber)
                arrangingSorted(arranging)
                myDialog.dismiss()
            }else{
                dialogBinding.sortingComment.error = getString(R.string.izoh_3_ta_belgidan_ko_p_bo_lishi_shart)
            }
        }
    }

    override fun onItemClickSubmit2(readyOrdersItem: ReadyOrdersItem) {
        Log.d("TAG", "onItemClickSubmit2")
        findNavController().navigate(R.id.sumbitFragment, bundleOf(
            "orderId" to readyOrdersItem.order_id
        ))
    }

    override fun onItemClickUnsortedLocation(readyOrdersItem: ReadyOrdersItem) {
        intentGoogleMaps(submit.geoplugin_latitude, submit.geoplugin_longitude)
    }

    override fun onItemClickOrder(readyOrdersItem: ReadyOrdersItem) {
        viewOrder(readyOrdersItem.order_id)
    }

    override fun onItemClickUnsorted2(readyOrdersItem: ReadyOrdersItem) {
        val dialogBinding = SortingItemBinding.inflate(layoutInflater)

        val myDialog = Dialog(requireActivity())
        myDialog.setContentView(dialogBinding.root)

        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        dialogBinding.cardClose.setOnClickListener {
            myDialog.dismiss()
        }
        dialogBinding.check.setOnClickListener {
            val comment2 = dialogBinding.sortingComment.text.toString()
            val sortNumber2 = dialogBinding.idNumberSorting.text.toString().toIntOrNull() ?: 0
            if (comment2.isNotEmpty() || comment2.length >= 3){
                arranging = Arranging(comment2, readyOrdersItem.order_id, sortNumber2)
                arrangingSorted(arranging)
                myDialog.dismiss()
            }else{
                dialogBinding.sortingComment.error = getString(R.string.izoh_3_ta_belgidan_ko_p_bo_lishi_shart)
            }
        }
    }
    private fun autocompleteAll(){
        ApiClient.retrofitService.autocomplete().enqueue(object : Callback<List<Autocomplete>>{

            override fun onResponse(call: Call<List<Autocomplete>>, response: Response<List<Autocomplete>>) {

                if (response.code() == 200){
                    list = response.body()!!
                    val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, list.map { it.title })
                    binding.autoCompleteTextViewList.setAdapter(arrayAdapter)
                }
                Log.d( "onResponse", response.body().toString())
            }

            override fun onFailure(call: Call<List<Autocomplete>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(
                    requireContext(),
                    "OnFailure",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    fun transportStatus(){
        ApiClient.retrofitService.transportStatusBar().enqueue(object : Callback<TransportStatusAPI>{
            override fun onResponse(call: Call<TransportStatusAPI>, response: Response<TransportStatusAPI>){
                if (response.code() == 200) {
                    binding.txtOrderTransport.text = response.body()!!.yangi_buyurtmalar.toString()
                    binding.txtWarehouseTransport.text = response.body()!!.ombordagilar_soni.toString()
                    binding.txtSubmitTransport.text = response.body()!!.tayyor_buyurtmalar.toString()
                }
            }

            override fun onFailure(call: Call<TransportStatusAPI>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Server bilan bog'lanolmadik", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun intentGoogleMaps(lat: String, lon: String){

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?daddr=$lat,$lon")
        )
        startActivity(intent)
    }

    fun viewOrder(orderId: Int){
        ApiClient.retrofitService.viewOrder(orderId).enqueue(object : Callback<ViewOrderItem>{
            override fun onResponse(call: Call<ViewOrderItem>, response: Response<ViewOrderItem>) {
                if (response.code() == 200) {

                    val dialogBinding = ItemMahsulotlarBinding.inflate(layoutInflater)

                    val myDialog = Dialog(requireActivity())
                    myDialog.setContentView(dialogBinding.root)

                    myDialog.setCancelable(true)
                    myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    myDialog.show()
                    Log.d( "onResponse", response.body().toString())
                }
            }

            override fun onFailure(call: Call<ViewOrderItem>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Xatolik", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun arrangingSorted(arranging: Arranging){
        ApiClient.retrofitService.arranging(arranging).enqueue(object : Callback<Arranging>{
            override fun onResponse(call: Call<Arranging>, response: Response<Arranging>) {
                if (response.code() == 200){
                    if (response.body() == null){
                        Toast.makeText(requireActivity(), "error", Toast.LENGTH_SHORT).show()
                    }else{
                        placeHolderPermission = true
                        arrangedPaginationAdapter.refresh()
                        notArrangedPaginationAdapter.refresh()
                    }
                }
            }

            override fun onFailure(call: Call<Arranging>, t: Throwable) {
                Toast.makeText(requireContext(), "Failure", Toast.LENGTH_SHORT).show()
            }
        })
    }
}