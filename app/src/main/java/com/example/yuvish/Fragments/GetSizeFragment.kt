//package com.example.yuvish.Fragments
//
//import android.annotation.SuppressLint
//import android.content.ContentValues.TAG
//import android.graphics.Bitmap
//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ArrayAdapter
//import android.widget.Toast
//import androidx.core.os.bundleOf
//import androidx.core.view.isGone
//import androidx.core.view.isVisible
//import androidx.fragment.app.setFragmentResult
//import androidx.lifecycle.lifecycleScope
//import androidx.navigation.fragment.findNavController
//import androidx.viewpager2.widget.ViewPager2
//import com.bumptech.glide.Glide
//import com.example.yuvish.Adapters.ConfirmedProductsAdapter
//import com.example.yuvish.Adapters.NotSizedProductsAdapter
//import com.example.yuvish.Adapters.SizedProductsAdapter
//import com.example.yuvish.Models.BarcodeManager
//import com.example.yuvish.Models.NewOrder.ProductSize
//import com.example.yuvish.Models.NewOrder.Service
//import com.example.yuvish.Models.NewOrder.SizingOrder
//import com.example.yuvish.Models.NewOrder.SizingProduct
//import com.example.yuvish.Models.PdfManager
//import com.example.yuvish.Models.PrintManager
//import com.example.yuvish.Models.Setting.Setting
//import com.example.yuvish.R
//import com.example.yuvish.databinding.FragmentGetSizeBinding
//import com.example.yuvish.databinding.ProductsBarcodePrintLayoutBinding
//import com.example.yuvish.retrofit.ApiClient
//import com.example.yuvish.retrofit.GlobalData
//import com.example.yuvish.retrofit.isNull
//import com.example.yuvish.retrofit.isZero
//import com.gkemon.XMLtoPDF.PdfGeneratorListener
//import com.gkemon.XMLtoPDF.model.SuccessResponse
//import com.google.android.material.snackbar.Snackbar
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class GetSizeFragment : Fragment(),
//    NotSizedProductsAdapter.CallBack {
//
//    lateinit var binding: FragmentGetSizeBinding
//    private lateinit var pdfManager: PdfManager
//    private lateinit var barcodeManager: BarcodeManager
//    private lateinit var printManager: PrintManager
//    lateinit var setting: Setting
//    private lateinit var servicesAdapter: ArrayAdapter<String>
//    private val notSizedProductsAdapter: NotSizedProductsAdapter by lazy {
//        NotSizedProductsAdapter(requireActivity(), this)
//    }
//    private val sizedProductsAdapter: SizedProductsAdapter by lazy {
//        SizedProductsAdapter(requireActivity())
//    }
//    private val confirmedProductsAdapter: ConfirmedProductsAdapter by lazy {
//        ConfirmedProductsAdapter(requireActivity())
//    }
//
//    private var orderId: Int? = null
//    private var sizingOrder: SizingOrder? = null
//    private var servicesList: ArrayList<Service>? = null
//    private var selectedServicePosition = 0
//
//    private var productId: Int? = null
//    private var productSize: ProductSize? = null
//    private var serviceId: Int? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        orderId = arguments?.getInt("orderId")
//        pdfManager = PdfManager(requireActivity())
//        barcodeManager = BarcodeManager()
//        printManager = PrintManager(requireActivity())
//        profile()
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentGetSizeBinding.inflate(layoutInflater)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        checkListEmpty(false)
//        binding.confirmedProductsRV.adapter = confirmedProductsAdapter
//        binding.sizedProductsRV.adapter = sizedProductsAdapter
//        binding.notSizedProductsVP.adapter = notSizedProductsAdapter
//
//        if (sizingOrder.isNull()) {
//            orderId?.let { getSizingOrderId(it) }
//        } else {
//            updateUI(sizingOrder!!)
//        }
//
//        if (servicesList.isNull()) {
//            getService()
//        } else {
//            updateDropDown()
//        }
//
//        setOnClickListeners()
//    }
//
//    private fun setOnClickListeners() {
//        binding.previous.setOnClickListener {
//            toPreviousPage(binding.notSizedProductsVP)
//        }
//
//        binding.next.setOnClickListener {
//            toNextPage(binding.notSizedProductsVP, notSizedProductsAdapter.itemCount)
//        }
//
//        binding.confirm.setOnClickListener {
//            sendBackStackRefreshRequest()
//            findNavController().popBackStack()
//        }
//
//        binding.servicesType.setOnItemClickListener { parent, view, position, id ->
//            selectedServicePosition = position
//        }
//
//        binding.addProduct.setOnClickListener {
//            if (servicesList.isNull() || servicesList!!.isEmpty()){
//                Toast.makeText(requireActivity(), getString(R.string.no_services), Toast.LENGTH_SHORT).show()
//            }else {
//                serviceId = servicesList!![selectedServicePosition].xizmat_id
//                addOneProduct(orderId!!, serviceId!!)
//            }
//        }
//    }
//
//    private fun toNextPage(viewPager2: ViewPager2, size: Int){
//        if (viewPager2.currentItem != size) {
//            viewPager2.currentItem += 1
//        }
//    }
//
//    private fun toPreviousPage(viewPager2: ViewPager2){
//        if (viewPager2.currentItem != 0) {
//            viewPager2.currentItem -= 1
//        }
//    }
//
//    private fun updateUI(sizingOrder: SizingOrder) {
//        binding.customerName.text = sizingOrder.costumer?.costumer_name
//        binding.address.text = sizingOrder.costumer?.costumer_addres
//        binding.receiptNumber.text = sizingOrder.nomer.toString()
//        binding.operatorComment.text = sizingOrder.izoh
//        binding.comment.text = sizingOrder.izoh2
//
//        updateProductsCount(sizingOrder)
//        sortProductsBySizing(sizingOrder)
//        confirmedProductsAdapter.submitList(sizingOrder.items)
//    }
//
//    private fun updateProductsCount(sizingOrder: SizingOrder) {
//        val count = getProductsCount(sizingOrder)
//        binding.totalProductCount.text = "$count ${getString(R.string.pcs)}"
//    }
//
//    private fun getProductsCount(sizingOrder: SizingOrder): Int {
//        var count = 0
//        sizingOrder.items.forEach {
//            count += it.value
//        }
//        return count
//    }
//
//    private fun sortProductsBySizing(sizingOrder: SizingOrder) {
//        val sizedProductsList = ArrayList<SizingProduct>()
//        val notSizedProductsList = ArrayList<SizingProduct>()
//
//        sizingOrder.products.forEach {
//            if (it.clean_hajm == 0.0) {
//                notSizedProductsList.add(it)
//            } else {
//                sizedProductsList.add(it)
//            }
//        }
//    }
//
//    private fun checkListEmpty(permission: Boolean = true) {
//        binding.sizedProductsTitle.isVisible =
//            if (permission) (sizedProductsAdapter.itemCount.isZero()) else permission
//        binding.sizedProductsRV.isGone =
//            if (permission) (sizedProductsAdapter.itemCount.isZero()) else permission
//
//        binding.notSizedProductsTitle.isVisible =
//            if (permission) (notSizedProductsAdapter.itemCount.isZero()) else permission
//        binding.notSizedProductsVP.isGone =
//            if (permission) (notSizedProductsAdapter.itemCount.isZero()) else permission
//    }
//
//    private fun updateDropDown(){
//        servicesAdapter = ArrayAdapter(
//            requireActivity(), android.R.layout.simple_list_item_1, servicesList!!.map { it.xizmat_turi }
//        )
//        binding.servicesType.setAdapter(servicesAdapter)
//
//        if (servicesList!!.isNotEmpty()) {
//            binding.servicesType.setText(servicesList!![selectedServicePosition].xizmat_turi, false)
//        }
//    }
//
//    private fun sendBackStackRefreshRequest(){
//        setFragmentResult("refreshRequest",
//            bundleOf("refreshPermission" to true)
//        )
//    }
//
//    private fun showSnackBar(text: String){
//        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
//    }
//
//    private fun getService() {
//        ApiClient.retrofitService.getServices().enqueue(object : Callback<List<Service>> {
//            override fun onResponse(call: Call<List<Service>>, response: Response<List<Service>>) {
//                if (response.code() == 200) {
//                    servicesList = response.body() as java.util.ArrayList<Service>?
//                    val arrayAdapter2 = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, servicesList!!.map { it.xizmat_turi })
//                    binding.servicesType.setAdapter(arrayAdapter2)
//                }
//            }
//
//            override fun onFailure(call: Call<List<Service>>, t: Throwable) {
//                t.printStackTrace()
//                Toast.makeText(requireContext(), "Ma'lumot kelmadi", Toast.LENGTH_SHORT).show()
//            }
//
//        })
//    }
//
//    private fun getSizingOrderId(orderId: Int) {
//        ApiClient.retrofitService.getSizingOrderById(orderId)
//            .enqueue(object : Callback<SizingOrder> {
//                override fun onResponse(call: Call<SizingOrder>, response: Response<SizingOrder>) {
//                    if (response.code() == 200) {
//                        sizingOrder = response.body()
//                        binding.customerName.text = sizingOrder?.costumer?.costumer_name
//                        binding.address.text = sizingOrder?.costumer?.costumer_addres
//                        binding.receiptNumber.text = sizingOrder?.nomer.toString()
//                        binding.operatorComment.text = sizingOrder?.izoh
//                        binding.comment.text = sizingOrder?.izoh2
//                        binding.totalProductCount.text = "${sizingOrder?.products?.size}"
//                    }
//                }
//
//                override fun onFailure(call: Call<SizingOrder>, t: Throwable) {
//                    t.printStackTrace()
//                    Toast.makeText(requireContext(), "Ma'lumot kelmadi", Toast.LENGTH_SHORT).show()
//                }
//
//            })
//    }
//
//    private fun addOneProduct(orderId: Int, serviceId: Int){
//        ApiClient.retrofitService.addOneProduct(orderId, serviceId).enqueue(object : Callback<String?>{
//            override fun onResponse(call: Call<String?>, response: Response<String?>) {
//                if (response.code() == 200){
//                    if (response.body().isNull()){
//                        Toast.makeText(requireActivity(), getString(R.string.error), Toast.LENGTH_SHORT).show()
//                    }else{
//                        showSnackBar(getString(R.string.product_added_successfully))
//                        sizingOrder = null
//                        getSizingOrderId(orderId)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<String?>, t: Throwable) {
//                t.printStackTrace()
//                Toast.makeText(requireContext(), "Ma'lumot ketmadi", Toast.LENGTH_SHORT).show()
//            }
//
//        })
//    }
//
//    private fun putProductSize(productId: Int, productSize: ProductSize){
//        ApiClient.retrofitService.putProductSize(productId, productSize).enqueue(object : Callback<String?>{
//            override fun onResponse(call: Call<String?>, response: Response<String?>) {
//                if (response.code() == 200){
//                    if (response.body().isNull()){
//                        Toast.makeText(requireActivity(), getString(R.string.error), Toast.LENGTH_SHORT).show()
//                    }else{
//                        showSnackBar(getString(R.string.measured_successfully))
//                        sizingOrder = null
//                        getSizingOrderId(orderId!!)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<String?>, t: Throwable) {
//                t.printStackTrace()
//                Toast.makeText(requireContext(), "Ma'lumotda xatolik", Toast.LENGTH_SHORT).show()
//            }
//
//        })
//    }
//
//    private fun profile(){
//        ApiClient.retrofitService.profile().enqueue(object : Callback<Setting>{
//            override fun onResponse(call: Call<Setting>, response: Response<Setting>) {
//                if (response.code() == 200){
//                    setting = response.body()!!
//                }
//            }
//
//            override fun onFailure(call: Call<Setting>, t: Throwable) {
//                t.printStackTrace()
//                Toast.makeText(requireContext(), "Server bilan bog'lanishda xatolik", Toast.LENGTH_SHORT).show()
//            }
//
//        })
//    }
//
//    override fun sizedClickListener(productId: Int, productSize: ProductSize) {
//        this.productId = productId
//        this.productSize = productSize
//        putProductSize(productId, productSize)
//    }
//
//    override fun printClickListener(sizingProduct: SizingProduct) {
//        printProductBarcode(sizingProduct)
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun printProductBarcode(sizingProduct: SizingProduct){
//        lifecycleScope.launch(Dispatchers.Main){
//
//            val printBinding = ProductsBarcodePrintLayoutBinding.inflate(layoutInflater)
//            val logoBitmap = loadLogo(
//                "${ApiClient.IMAGE_BASE_URL}${setting.filial.logo}"
//            )
//            val barcodeBitmap = barcodeManager.createImage(sizingProduct.barcode)
//
//            printBinding.logo.setImageBitmap(logoBitmap)
//            printBinding.barcode.setImageBitmap(barcodeBitmap)
//            printBinding.service.text = sizingProduct.xizmat.xizmat_turi
//            printBinding.price.text =
//                "${getString(R.string.narx)}: ${sizingProduct.narx} ${getString(R.string.so_m)} " +
//                        "(1 ${getLocalizedMeasure(sizingProduct.xizmat.olchov)})"
//
//            generatePdf(printBinding.root, ApiClient.BARCODE_PRINT_FILE_NAME)
//        }
//    }
//
//    private fun generatePdf(view: View, fileName: String){
//        val pdfGeneratorListener = object : PdfGeneratorListener() {
//            override fun onStartPDFGeneration() {
//
//            }
//
//            override fun onFinishPDFGeneration() {
//
//            }
//
//            override fun onSuccess(response: SuccessResponse?) {
//                super.onSuccess(response)
//                printManager.showCustomSnackBar(response!!.file)
//            }
//        }
//
//        pdfManager.convertViewToPdf(view, fileName, pdfGeneratorListener)
//    }
//
//    private suspend fun loadLogo(url: String): Bitmap {
//        val logoLoader = Glide.with(requireActivity())
//            .asBitmap()
//            .load(url)
//            .submit()
//
//        return withContext(Dispatchers.Default) {
//            logoLoader.get()
//        }
//    }
//
//    private fun getLocalizedMeasure(measure: String): String{
//        return when(measure){
//            "metr" -> {
//                getString(R.string.m)
//            }
//            "m" -> {
//                getString(R.string.meter)
//            }
//            else -> {
//                measure
//            }
//        }
//    }
//
//}