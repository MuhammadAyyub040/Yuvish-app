package com.example.yuvish.Fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.yuvish.Adapters.CleaningPaginationAdapter
import com.example.yuvish.models.Cleaning.CleaningData
import com.example.yuvish.models.Cleaning.PaginationPageCleaning
import com.example.yuvish.models.HolatPaneli.WashingStatusAPI
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentHomeBinding
import com.example.yuvish.retrofit.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), CleaningPaginationAdapter.OnItemClick {

    lateinit var binding: FragmentHomeBinding
    lateinit var cleaningPaginationAdapter: CleaningPaginationAdapter
    lateinit var cleaningPaginationAdapter2: CleaningPaginationAdapter
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cleaningPaginationAdapter = CleaningPaginationAdapter(requireActivity(), this)
        cleaningPaginationAdapter2 = CleaningPaginationAdapter(requireActivity(),this)
        getPaginationCleaning()
        getPaginationRecleaning()
        statusBar()

        setFragmentResultListener("barcode") { requestKey, bundle ->
            binding.edtBarcode.setText(bundle.getString("result"))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        val barcode = binding.edtBarcode.text.toString()
        barcodeId(barcode)
        loadOrderIdByBarcode()

        binding.btnSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_globalSearchFragment)
        }

        binding.barcodeScanner.setOnClickListener {
            checkPermission()
        }
        binding.edtBarcode.setOnClickListener {
            loadOrderIdByBarcode()
        }

        binding.txtWashing.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_self)
        }

        binding.txtWashedId.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_baseFragment)
        }
        binding.txtPacked.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_tayyorFragment)
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
                    Toast.makeText(requireActivity(), " Asosiy bo'lim tanlandi", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_homeFragment_to_baseFragment)
                }
                R.id.new_order -> {
                    Toast.makeText(
                        requireActivity(),
                        " Yangi buyurtmalar tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_homeFragment_to_transportFragment)
                }
                R.id.washing -> {
                    Toast.makeText(
                        requireActivity(),
                        " Yuvish bo'limi tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_homeFragment_self)
                }
                R.id.ready -> {
                    Toast.makeText(
                        requireActivity(),
                        " Tayyor buyurtmalar tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_homeFragment_to_tayyorFragment)
                }
                R.id.warehouse -> {
                    Toast.makeText(
                        requireActivity(),
                        " Sklad bo'limi tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_homeFragment_to_skladFragment)
                }
                R.id.employee_setting -> {
                    Toast.makeText(
                        requireActivity(),
                        " Xodim sozlamalari tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
                }
                R.id.close -> {
                    Toast.makeText(
                        requireActivity(),
                        " Chiqish tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                }
                R.id.debtors -> {
                    Toast.makeText(
                        requireActivity(),
                        " Qarzdorlar bo'limi tanlandi",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_homeFragment_to_debtorsDepartmentFragment)
                }
            }
            true

        }

        return binding.root
    }

    private fun getPaginationCleaning() {
        Pager(
            config = PagingConfig(
                pageSize = 50,
                enablePlaceholders = false,
                initialLoadSize = 50
            ),
            pagingSourceFactory = { PaginationPageCleaning(ApiClient.retrofitService, "yuvish") }
        ).liveData.observe(this) {
            lifecycleScope.launch {
                cleaningPaginationAdapter.submitData(it)
            }
        }
    }

    fun getPaginationRecleaning() {
        Pager(
            config = PagingConfig(
                pageSize = 50,
                enablePlaceholders = false,
                initialLoadSize = 50
            ),
            pagingSourceFactory = { PaginationPageCleaning(ApiClient.retrofitService, "qayta") }
        ).liveData.observe(this) {
            lifecycleScope.launch {
                cleaningPaginationAdapter2.submitData(it)
            }
        }
    }

    private fun closeKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.btnSearch.windowToken, 0)
    }

    override fun onItemClickCleaning(cleaningData: CleaningData) {
        Toast.makeText(requireActivity(), "${cleaningData.order_id}", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_homeFragment_to_registrationFragment, bundleOf(
        "orderId" to cleaningData.order_id
        ))
    }

    override fun onItemClickPhoneNumber(cleaningData: CleaningData) {
        intentCall(cleaningData.custumer.costumer_phone_1)
    }

    private fun intentCall(phoneNumber: String){
        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        startActivity(callIntent)
    }

    private fun checkPermission(){
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            findNavController().navigate(R.id.action_homeFragment_to_barcodeScannerFragment)
        }else{
            requestCameraPermission()
        }
    }

    private val cameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it){
            findNavController().navigate(R.id.action_homeFragment_to_barcodeScannerFragment)
        }
    }

    private fun requestCameraPermission(){
        cameraPermission.launch(Manifest.permission.CAMERA)
    }

    fun statusBar(){
        ApiClient.retrofitService.statusBar().enqueue(object : Callback<WashingStatusAPI>{
            override fun onResponse(call: Call<WashingStatusAPI>, response: Response<WashingStatusAPI>) {
                if (response.code() == 200) {
                    binding.txtWashing.text = response.body()!!.yuvilmaganlar_soni.toString()
                    binding.txtWashedId.text = response.body()!!.qadoqlanmaganlar_soni.toString()
                    binding.txtPacked.text = response.body()!!.topshirilmaganlar_soni.toString()
                }
            }

            override fun onFailure(call: Call<WashingStatusAPI>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Server bilan bog'lanolmadik", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun loadOrderIdByBarcode(){
        val barcode = binding.edtBarcode.text.toString()
        if (barcode.length == 13){
            closeKeyboard()
            ApiClient.retrofitService.getOrderIdByBarcode(barcode)
        }
    }
    
    private fun barcodeId(barcode : String){
        ApiClient.retrofitService.getOrderIdByBarcode(barcode).enqueue(object : Callback<Int>{
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val orderId = response.body()
                if (response.code() == 200){
                    if (orderId == null) {
                        Toast.makeText(requireActivity(), "Barcode topilmadi", Toast.LENGTH_SHORT).show()
                    } else{
                        findNavController().navigate(R.id.action_homeFragment_to_registrationFragment, bundleOf(
                            "orderId" to orderId,
                            "barcode" to binding.edtBarcode.text.toString()
                        )
                        )
                        binding.edtBarcode.text?.clear()
                    }
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Server bilan bog'lanolmadik", Toast.LENGTH_SHORT).show()
            }

        })
    }
    
}