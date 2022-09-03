package com.example.yuvish.Fragments

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.example.yuvish.Models.ReadyOrders.Autocomplete
import com.example.yuvish.Models.ReadyOrders.PaginationPageArranged
import com.example.yuvish.Models.ReadyOrders.ReadyOrdersItem
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentTayyorBinding
import com.example.yuvish.databinding.SortingItemBinding
import com.example.yuvish.retrofit.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TayyorFragment : Fragment(), ArrangedPaginationAdapter.OnItemClick, NotArrangedPaginationAdapter.OnItemClick {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var arrangedPaginationAdapter: ArrangedPaginationAdapter
    lateinit var notArrangedPaginationAdapter: NotArrangedPaginationAdapter
    lateinit var list: List<Autocomplete>
    var searchPage = false
    lateinit var binding: FragmentTayyorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arrangedPaginationAdapter = ArrangedPaginationAdapter(this)
        notArrangedPaginationAdapter = NotArrangedPaginationAdapter(requireActivity(), this)
        getPaginationArranged()
        getPaginationNotArranged()
        autocompleteAll()
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

        binding.viewPager.adapter = arrangedPaginationAdapter

        binding.viewPager2.adapter = notArrangedPaginationAdapter

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

    fun getPaginationArranged() {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { PaginationPageArranged(ApiClient.retrofitService, 0, "arranged") }
        ).liveData.observe(this) {
            lifecycleScope.launch {
                arrangedPaginationAdapter.submitData(it)
            }
        }
    }

    fun getPaginationNotArranged() {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { PaginationPageArranged(ApiClient.retrofitService, 0, "notarranged") }
        ).liveData.observe(this) {
            lifecycleScope.launch {
                notArrangedPaginationAdapter.submitData(it)
            }
        }
    }

    private fun closeKeyboard(view: View) {
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

        }
    }

    override fun onItemClickSubmit2(readyOrdersItem: ReadyOrdersItem) {
        Log.d("TAG", "onItemClickSubmit2")
        findNavController().navigate(R.id.sumbitFragment, bundleOf(
            "orderId" to readyOrdersItem.order_id
        ))

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
    }
    private fun autocompleteAll(){
        ApiClient.retrofitService.autocomplete().enqueue(object : Callback<List<Autocomplete>>{

            override fun onResponse(
                call: Call<List<Autocomplete>>,
                response: Response<List<Autocomplete>>
            ) {

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
}