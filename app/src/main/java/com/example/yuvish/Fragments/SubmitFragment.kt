package com.example.yuvish.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
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
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.yuvish.Adapters.ReceiptPrintProductsGroupAdapter
import com.example.yuvish.Adapters.SubmitAdapterGroup
import com.example.yuvish.Adapters.SubmitAdapterChild
import com.example.yuvish.models.ArrangedSubmit.*
import com.example.yuvish.models.DebtorsAPI.Market.ResponseDetail
import com.example.yuvish.models.PdfManager
import com.example.yuvish.models.PrintManager
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentSumbitBinding
import com.example.yuvish.databinding.ReceiptPrintLayoutBinding
import com.example.yuvish.retrofit.ApiClient
import com.example.yuvish.retrofit.GlobalData
import com.example.yuvish.retrofit.isNull
import com.gkemon.XMLtoPDF.PdfGeneratorListener
import com.gkemon.XMLtoPDF.model.SuccessResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubmitFragment : Fragment(), SubmitAdapterChild.CaLLBack {

    lateinit var binding: FragmentSumbitBinding
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var submitAdapter: SubmitAdapterGroup
    private lateinit var pdfManager: PdfManager
    private lateinit var printManager: PrintManager
    lateinit var list: List<String>
    lateinit var submit: Submit
    lateinit var submittingOrder: SubmittingOrder
    var commentPage = false
    var discountPage = false
    var orderId: Int? = null
    private var productId: Int? = null

    private val TAG = "SubmitFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = arguments?.getInt("orderId")
        list = arrayListOf()
        pdfManager = PdfManager(requireActivity())
        printManager = PrintManager(requireActivity())
        paymentTypes()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSumbitBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrangedSubmit(orderId!!)

        submitAdapter = SubmitAdapterGroup(this, false)
        binding.rvSubmit.adapter = submitAdapter

        val arrayAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            arrayListOf("2022", "2021")
        )
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        binding.cardComment.setOnClickListener {
            binding.linearComment.visibility = View.GONE
            commentPage = false
        }

        binding.backStack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.locationLiner.setOnClickListener {
            intentGoogleMaps(submit.data.geoplugin_latitude, submit.data.geoplugin_longitude)
        }

        binding.cardChegirma.setOnClickListener {
            binding.linearDiscount.visibility = View.GONE
            discountPage = false
        }

        binding.imgPrint.setOnClickListener {
            if (submit.isNull()){
                Toast.makeText(requireActivity(), getString(R.string.not_enough_information), Toast.LENGTH_SHORT).show()
            }else{
                printReceipt(submit)
            }
        }

        binding.btnPut.setOnClickListener {
            transferWarehouse(orderId!!)
        }

        binding.cardComment.setOnClickListener {
            when (commentPage) {
                false -> {
                    binding.linearComment.visibility = View.VISIBLE
                    commentPage = true
                }
                true -> {
                    binding.linearComment.visibility = View.GONE
                    commentPage = false
                }
            }
        }

        binding.cardChegirma.setOnClickListener {
            when (discountPage) {
                false -> {
                    binding.linearDiscount.visibility = View.VISIBLE
                    discountPage = true
                }
                true -> {
                    binding.linearDiscount.visibility = View.GONE
                    discountPage = false
                }
            }
        }

        binding.btnSubmitted.setOnClickListener {
            val payment = binding.editPayment.text.toString().trim()

            if (payment.isEmpty()) {
                binding.editPayment.error = "Mijozdan olingan pulni kiriting"
                binding.editPayment.requestFocus()
            } else {
                submittingOrder = SubmittingOrder(orderId!!, binding.editPayment.text.toString().filter { it.isDigit() }.toInt(), binding.autoCompleteTextViewPaymentType.text.toString().lowercase())
                orderId?.let { submitOrder(it, submittingOrder) }

            }
        }

    }

    private fun arrangedSubmit(orderId: Int) {
        ApiClient.retrofitService.submit(orderId).enqueue(object : Callback<Submit> {
            override fun onResponse(call: Call<Submit>, response: Response<Submit>) {
                Log.e(TAG, "onResponse: ${response.code()} ${response.body()}")
                if (response.code() == 200) {
                    Log.e(TAG, "onResponse: ${response.code()} ${response.body()}")
                    submit = response.body()!!
                    Log.e(TAG, "onResponse: $submit")

                    binding.arrangedCustomer.text = submit.data.custumer.costumer_name
                    binding.arrangedSubmitNumber.text = submit.data.custumer.costumer_phone_1
                    binding.arrangedSubmitLocation.text = submit.data.custumer.costumer_addres
                    binding.arrangedSubmitOperator.text = submit.data.operator.fullname
                    binding.txtOrders.text = submit.data.cleans.size.toString()
                    binding.jamiSumma.text = submit.jami_summa.toString()
                    binding.yakuniyTolov.text = submit.yakuniy_tolov.toString()
                    binding.txtSubmitNomer.text = submit.data.nomer.toString()
                    binding.comment2.text = submit.data.izoh2
                    binding.comment3.text = submit.data.izoh3
                    binding.chegirmaRewash.text = submit.qayta_yuvish_chegirma.toString()
                    binding.chegirmaWasDropped.text = submit.tushish_chegirma.toString()
                    binding.chegirmaTakeAway.text = submit.discount_for_own.toString()
                    binding.totalWasDropped.text = submit.data.chegirma_sum_summa.toString()
                    binding.jamiChegirma.text = submit.jami_chegirma.toString()
                    submitAdapter.setData(getTypeOrders(submit.data.cleans))
                    updateDiscount(submit)
                }
            }

            override fun onFailure(call: Call<Submit>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Server bilan bog'lanishda xatolik", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun paymentTypes() {
        ApiClient.retrofitService.paymentTypes().enqueue(object : Callback<List<String>> {

            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {

                if (response.code() == 200) {
                    list = response.body()!!
                    val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, list)
                    binding.autoCompleteTextViewPaymentType.setAdapter(arrayAdapter)
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "OnFailure", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun submitOrder(orderId: Int, submittingOrder: SubmittingOrder) {
        ApiClient.retrofitService.submittingOrder(orderId, submittingOrder)
            .enqueue(object : Callback<SubmittingOrderResponse> {
                override fun onResponse(call: Call<SubmittingOrderResponse>, response: Response<SubmittingOrderResponse>) {
                    if (response.code() == 200)
                        when (response.body()) {
                            null -> {
                                Toast.makeText(requireActivity(), "null", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                sendBackStackRefreshRequest()
                                findNavController().navigate(
                                    R.id.action_sumbitFragment_to_debtorFragment,
                                    bundleOf("debtId" to response.body())
                                )
                            }
                        }
                }

                override fun onFailure(call: Call<SubmittingOrderResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: submitOrder ${t.message}")
                }

            })
    }

    private fun transferWarehouse(orderId: Int){
        ApiClient.retrofitService.transferWarehouse(orderId).enqueue(object : Callback<ResponseDetail>{
            override fun onResponse(call: Call<ResponseDetail>, response: Response<ResponseDetail>) {
                if (response.code() == 200)
                    Toast.makeText(requireActivity(), "Omborga muvaffaqqiyatli o'tkazildi.", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_sumbitFragment_to_tayyorFragment)
            }

            override fun onFailure(call: Call<ResponseDetail>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Xatolik yuz berdi", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun transferToRewash(productId: Int){
        ApiClient.retrofitService.productTransferToRewash(productId).enqueue(object : Callback<String?>{
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.code() == 200){
                    Toast.makeText(requireContext(), getString(R.string.mahsulot_muvaffaqiyatli_otkazildi), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                t.printStackTrace()
                t.message
                Toast.makeText(requireContext(), "Buyurtma jo'natilmadi", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendBackStackRefreshRequest() {
        setFragmentResult(
            "refreshRequest",
            bundleOf("refreshPermission" to true)
        )
    }

    private fun closeKeyboard(view: View) {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.edtId.windowToken, 0)
    }

    private fun intentGoogleMaps(lat: String, lon: String) {

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?daddr=$lat,$lon")
        )
        startActivity(intent)
    }

    override fun rewashClickListener(product: Product) {
       productId = product.id
       transferToRewash(productId!!)
    }

    @SuppressLint("SetTextI18n")
    private fun updateDiscounts(printBinding: ReceiptPrintLayoutBinding, submit: Submit){
        if (submit.jami_chegirma == 0){
            printBinding.discountsLayout.visibility = View.GONE
        }else{
            printBinding.discountsLayout.visibility = View.VISIBLE
            printBinding.totalDiscount.text = "${submit.jami_chegirma} %"

            if (submit.qayta_yuvish_chegirma == 0){
                printBinding.rewashDiscountLayout.visibility = View.GONE
                printBinding.rewashDiscountDivider.visibility = View.GONE
            }else{
                printBinding.rewashDiscountLayout.visibility = View.VISIBLE
                printBinding.rewashDiscountDivider.visibility = View.VISIBLE
                printBinding.rewashDiscount.text = "${submit.qayta_yuvish_chegirma} ${getString(R.string.so_m)}"
            }

            if (submit.discount_for_own == 0){
                printBinding.customerPickupDiscountLayout.visibility = View.GONE
                printBinding.customerPickupDiscountDivider.visibility = View.GONE
            }else{
                printBinding.customerPickupDiscountLayout.visibility = View.VISIBLE
                printBinding.customerPickupDiscountDivider.visibility = View.VISIBLE
                printBinding.customerPickupDiscount.text = "${submit.discount_for_own} ${getString(R.string.so_m)}"
            }

            if (submit.tushish_chegirma == 0){
                printBinding.droppedDiscountLayout.visibility = View.GONE
                printBinding.droppedDiscountDivider.visibility = View.GONE
            }else{
                printBinding.droppedDiscountLayout.visibility = View.VISIBLE
                printBinding.droppedDiscountDivider.visibility = View.VISIBLE
                printBinding.droppedDiscount.text = "${submit.tushish_chegirma} ${getString(R.string.so_m)}"
            }

            if (submit.data.chegirma_sum_summa == 0){
                printBinding.totalDroppedDiscountLayout.visibility = View.GONE
                printBinding.totalDroppedDiscountDivider.visibility = View.GONE
            }else{
                printBinding.totalDroppedDiscountLayout.visibility = View.VISIBLE
                printBinding.totalDroppedDiscountDivider.visibility = View.VISIBLE
                printBinding.totalDroppedDiscount.text = "${submit.data.chegirma_sum_summa} ${getString(R.string.so_m)}"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateDiscount(submit: Submit){
        if (submit.jami_chegirma == 0){
            binding.discountsLayout.visibility = View.GONE
        }else{
            binding.discountsLayout.visibility = View.VISIBLE
            binding.jamiChegirma.text = "${submit.jami_chegirma} %"

            if (submit.qayta_yuvish_chegirma == 0){
                binding.rewashDiscountLayout.visibility = View.GONE
            }else{
                binding.rewashDiscountLayout.visibility = View.VISIBLE
                binding.chegirmaRewash.text = "${submit.qayta_yuvish_chegirma} ${getString(R.string.so_m)}"
            }

            if (submit.discount_for_own == 0){
                binding.customerPickupDiscountLayout.visibility = View.GONE
            }else{
                binding.customerPickupDiscountLayout.visibility = View.VISIBLE
                binding.chegirmaTakeAway.text = "${submit.discount_for_own} ${getString(R.string.so_m)}"
            }

            if (submit.tushish_chegirma == 0){
                binding.droppedDiscountLayout.visibility = View.GONE
            }else{
                binding.droppedDiscountLayout.visibility = View.VISIBLE
                binding.chegirmaWasDropped.text = "${submit.tushish_chegirma} ${getString(R.string.so_m)}"
            }

            if (submit.data.chegirma_sum_summa == 0){
                binding.totalDroppedDiscountLayout.visibility = View.GONE
            }else{
                binding.totalDroppedDiscountLayout.visibility = View.VISIBLE
                binding.totalWasDropped.text = "${submit.data.chegirma_sum_summa} ${getString(R.string.so_m)}"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun printReceipt(submit: Submit){
        lifecycleScope.launch(Dispatchers.Main){

            val printBinding = ReceiptPrintLayoutBinding.inflate(layoutInflater)
            val logoBitmap = loadLogo(
                "${ApiClient.IMAGE_BASE_URL}${GlobalData.currentUser!!.filial.logo}"
            )
            val receiptPrintProductsGroupAdapter = ReceiptPrintProductsGroupAdapter(
                submitAdapter.getData(), requireActivity()
            )

            printBinding.logo.setImageBitmap(logoBitmap)
            printBinding.productsRV.adapter = receiptPrintProductsGroupAdapter
            printBinding.companyName.text = GlobalData.currentUser?.filial?.filial_name
            printBinding.address.text = GlobalData.currentUser?.filial?.filial_address
            printBinding.destination.text = GlobalData.currentUser?.filial?.filial_destination
            printBinding.phoneNumber.text = "+998${GlobalData.currentUser?.filial?.filial_phone}"
            printBinding.receiptNumber.text = submit.data.nomer.toString()
            printBinding.totalAmount.text = "${submit.jami_summa} ${getString(R.string.so_m)}"
            printBinding.finalAmount.text = "${submit.yakuniy_tolov} ${getString(R.string.so_m)}"
            updateDiscounts(printBinding, submit)

            generatePdf(printBinding.root)
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

    private fun generatePdf(view: View){
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

        pdfManager.convertViewToPdf(view, ApiClient.RECEIPT_PRINT_FILE_NAME, pdfGeneratorListener)
    }

    private fun getTypeOrders(submitProducts: List<Clean>): ArrayList<Buyurtmalar> {
        val typeOrders = ArrayList<Buyurtmalar>()
        val services = HashSet<Int>()

        submitProducts.forEach {
            services.add(it.xizmat.xizmat_id)
        }

        if (services.isEmpty()) return typeOrders

        services.forEach { serviceId ->
            val typeProducts = getTypeProducts(submitProducts, serviceId)

            typeOrders.add(
                Buyurtmalar(
                    typeProducts.size,
                    getTotalVolume(typeProducts),
                    orderId!!,
                    typeProducts[0].serviceName,
                    typeProducts[0].serviceUnit,
                    typeProducts,
                    getTotalAmount(typeProducts)
                )
            )
        }

        return typeOrders
    }

    private fun getTotalVolume(typeProducts: List<Product>): Double {
        var totalVolume = 0.0

        typeProducts.forEach {
            totalVolume += it.clean_hajm
        }

        return totalVolume
    }

    private fun getTotalAmount(typeProducts: List<Product>): Double {
        var totalAmount = 0.0

        typeProducts.forEach {
            totalAmount += it.summa
        }

        return totalAmount
    }

    private fun getTypeProducts(submitProducts: List<Clean>, serviceId: Int): ArrayList<Product> {
        val typeProducts = ArrayList<Product>()

        submitProducts.forEach { product ->
            if (serviceId == product.xizmat.xizmat_id){
                typeProducts.add(
                    Product(
                        product.clean_hajm,
                        product.gilam_boyi,
                        product.gilam_eni,
                        product.id,
                        product.joy,
                        product.narx,
                        product.reclean_place,
                        product.clean_narx,
                        product.xizmat.xizmat_turi,
                        product.xizmat.olchov
                    )
                )
            }
        }

        return typeProducts
    }
}