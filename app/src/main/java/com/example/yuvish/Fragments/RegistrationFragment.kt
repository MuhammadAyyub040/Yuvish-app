package com.example.yuvish.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.L
import com.bumptech.glide.Glide
import com.example.yuvish.Adapters.RegistrationAdapter
import com.example.yuvish.Adapters.RegistrationAdapterChild
import com.example.yuvish.Adapters.RegistrationAdapterGroup
import com.example.yuvish.Models.BarcodeApi.Order
import com.example.yuvish.Models.BarcodeManager
import com.example.yuvish.Models.NewOrder.ProductSize
import com.example.yuvish.Models.NewOrder.SelectedService
import com.example.yuvish.Models.NewOrder.Service
import com.example.yuvish.Models.NewOrder.SizingProduct
import com.example.yuvish.Models.PdfManager
import com.example.yuvish.Models.PrintManager
import com.example.yuvish.Models.Registration.Product
import com.example.yuvish.Models.Registration.Registration
import com.example.yuvish.Models.Registration.ServiceTypeItem
import com.example.yuvish.Models.Setting.Setting
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentRegistrationBinding
import com.example.yuvish.databinding.ProductsBarcodePrintLayoutBinding
import com.example.yuvish.retrofit.ApiClient
import com.example.yuvish.retrofit.isNull
import com.gkemon.XMLtoPDF.PdfGeneratorListener
import com.gkemon.XMLtoPDF.model.SuccessResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationFragment : Fragment(), RegistrationAdapter.CallBack {

    lateinit var binding: FragmentRegistrationBinding
    lateinit var map: HashMap<String, List<String>>
    lateinit var titleList: ArrayList<String>
    lateinit var productList: ArrayList<Product>
    lateinit var list: List<ServiceTypeItem>
    lateinit var registrationAdapterGroup: RegistrationAdapterGroup
    lateinit var registrationAdapter: RegistrationAdapter
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var registration: Registration
    lateinit var setting: Setting

    private lateinit var servicesAdapter: ArrayAdapter<String>
    private lateinit var pdfManager: PdfManager
    private lateinit var printManager: PrintManager
    private lateinit var barcodeManager: BarcodeManager
    private var orderId: Int? = null
    private var toWhereShowNotification = "entryData"
    private var selectedOrder: Order? = null

    private val TAG = "SubmitFragment"
    private var selectedServicePosition = 0
    private val selectedServicesList = ArrayList<SelectedService>()
    private var servicesList: ArrayList<Service>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = arguments?.getInt("orderId")
        getService()
        profile()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderId?.let { registrationApi(it) }

        if (selectedOrder == null){
            loadOrderById("entryData")
            ApiClient.retrofitService.getShelfs()
        }else{
            updateUI(selectedOrder!!)
        }

        registrationAdapterGroup = RegistrationAdapterGroup( false)
        binding.rvRegistrationOrders.adapter = registrationAdapterGroup

        registrationAdapter = RegistrationAdapter(this)
        binding.rvRegistration.adapter = registrationAdapter

        titleList = ArrayList()
        productList = ArrayList()
        map = HashMap()

        val arrayAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            arrayListOf("2022", "2021")
        )
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        binding.backStack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.cardPlus.setOnClickListener {
            if (servicesList.isNull() || servicesList!!.isEmpty()){
                Toast.makeText(requireActivity(), getString(R.string.no_services), Toast.LENGTH_SHORT).show()
            }else {
                addService()
            }
        }

    }

    private fun addService(){
        selectedServicesList.add(0,
            SelectedService(servicesList!![selectedServicePosition], registrationAdapter.itemCount +1 ))
        Log.e(TAG, "addService: ${selectedServicesList.size}")
        registrationAdapter.notifyItemInserted(0)
        registrationAdapter.notifyItemRangeChanged(0, selectedServicesList.size)

//        servicesList!!.removeAt(selectedServicePosition)
        selectedServicePosition = 0
        updateDropDown()
    }

    private fun updateDropDown(){
        servicesAdapter = ArrayAdapter(
            requireActivity(), android.R.layout.simple_list_item_1, servicesList!!.map { it.xizmat_turi }
        )
        binding.autoCompleteTextViewServiceType.setAdapter(servicesAdapter)

        if (servicesList!!.isNotEmpty()) {
            binding.autoCompleteTextViewServiceType.setText(servicesList!![selectedServicePosition].xizmat_turi, false)
        }else{
            binding.autoCompleteTextViewServiceType.text.clear()
        }
    }

    private fun loadOrderById(where: String){
        toWhereShowNotification = where
        ApiClient.retrofitService.getOrderById(orderId!!)
    }

    private fun registrationApi(orderId: Int){
        ApiClient.retrofitService.registrationApi(orderId).enqueue(object : Callback<Registration>{
            override fun onResponse(call: Call<Registration>, response: Response<Registration>) {
                if (response.code() == 200) {
                    registration = response.body()!!

                    registrationAdapterGroup.setData(registration.products)
                    binding.customerName.text = registration.costumer.costumer_name
                    binding.address.text = registration.costumer.costumer_addres
                    binding.registrationPhoneNumber.text = registration.costumer.costumer_phone_1
                    binding.receiptNumber.text = registration.nomer.toString()
                }
            }

            override fun onFailure(call: Call<Registration>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Server bilan bog'lanolmadik", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getService() {
        ApiClient.retrofitService.getServices().enqueue(object : Callback<List<Service>> {
            override fun onResponse(call: Call<List<Service>>, response: Response<List<Service>>) {
                if (response.code() == 200) {
                    servicesList = response.body() as java.util.ArrayList<Service>?
                    val arrayAdapter2 = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, servicesList!!.map { it.xizmat_turi })
                    binding.autoCompleteTextViewServiceType.setAdapter(arrayAdapter2)
                }
            }

            override fun onFailure(call: Call<List<Service>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Ma'lumot kelmadi", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun profile(){
        ApiClient.retrofitService.profile().enqueue(object : Callback<Setting>{
            override fun onResponse(call: Call<Setting>, response: Response<Setting>) {
                if (response.code() == 200){
                    setting = response.body()!!
                }
            }

            override fun onFailure(call: Call<Setting>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Server bilan bog'lanishda xatolik", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun closeKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.edtId.windowToken, 0)
    }

    @SuppressLint("SetTextI18n")
    private fun printProductBarcode(sizingProduct: SizingProduct){
        lifecycleScope.launch(Dispatchers.Main){

            val printBinding = ProductsBarcodePrintLayoutBinding.inflate(layoutInflater)
            val logoBitmap = loadLogo(
                "${ApiClient.IMAGE_BASE_URL}${setting.filial.logo}"
            )
            val barcodeBitmap = barcodeManager.createImage(sizingProduct.barcode)

            printBinding.logo.setImageBitmap(logoBitmap)
            printBinding.barcode.setImageBitmap(barcodeBitmap)
            printBinding.service.text = sizingProduct.xizmat.xizmat_turi
            printBinding.price.text =
                "${getString(R.string.narx)}: ${sizingProduct.narx} ${getString(R.string.so_m)} " +
                        "(1 ${getLocalizedMeasure(sizingProduct.xizmat.olchov)})"

            generatePdf(printBinding.root, ApiClient.BARCODE_PRINT_FILE_NAME)
        }
    }

    private suspend fun loadLogo(url: String): Bitmap {
        val logoLoader = Glide.with(requireActivity())
            .asBitmap()
            .load(url)
            .submit()

        return withContext(Dispatchers.Default) {
            logoLoader.get()
        }
    }

    private fun generatePdf(view: View, fileName: String){
        val pdfGeneratorListener = object : PdfGeneratorListener() {
            override fun onStartPDFGeneration() {

            }

            override fun onFinishPDFGeneration() {

            }

            override fun onSuccess(response: SuccessResponse?) {
                super.onSuccess(response)
                printManager.showCustomSnackBar(response!!.file)
            }
        }

        pdfManager.convertViewToPdf(view, fileName, pdfGeneratorListener)
    }

    private fun getLocalizedMeasure(measure: String): String{
        return when(measure){
            "metr" -> {
                getString(R.string.m)
            }
            "m" -> {
                getString(R.string.meter)
            }
            else -> {
                measure
            }
        }
    }

    override fun washedClickListener(productId: Int, productSize: ProductSize) {

    }

    override fun printClickListener(sizingProduct: SizingProduct) {
        printProductBarcode(sizingProduct)
    }

    private fun updateUI(order: Order){
        binding.customerName.text = order.costumer.costumer_name
        binding.address.text = order.costumer.costumer_addres
        binding.receiptNumber.text = order.nomer.toString()

        updateProductCount(registration)
    }


    private fun updateProductCount(registration: Registration) {
        val count = getProductCount(registration)
        binding.txtOrdersRegistration.text = "$count${getString(R.string.pcs)} "
    }

    private fun getProductCount(registration: Registration): Int{
        var count = 0
        registration.items.forEach{
            count += it.value
        }
        return count
    }
}