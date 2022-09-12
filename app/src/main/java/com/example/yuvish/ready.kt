//import android.annotation.SuppressLint
//import android.app.AlertDialog
//import android.content.ActivityNotFoundException
//import android.content.Intent
//import android.graphics.Bitmap
//import android.net.Uri
//import android.os.Bundle
//import android.os.CountDownTimer
//import android.os.Environment
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.webkit.MimeTypeMap
//import android.widget.ArrayAdapter
//import android.widget.Toast
//import androidx.core.content.FileProvider
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.lifecycleScope
//import androidx.navigation.fragment.findNavController
//import androidx.viewpager2.widget.ViewPager2
//import com.bumptech.glide.Glide
//import com.gkemon.XMLtoPDF.PdfGeneratorListener
//import com.gkemon.XMLtoPDF.model.FailureResponse
//import com.gkemon.XMLtoPDF.model.SuccessResponse
//import com.google.android.material.bottomsheet.BottomSheetDialog
//import com.google.android.material.snackbar.Snackbar
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import uz.abubakir_khakimov.yuvish_uz_qadoqlash.R
//import uz.abubakir_khakimov.yuvish_uz_qadoqlash.adapters.PackedProductAdapter
//import uz.abubakir_khakimov.yuvish_uz_qadoqlash.adapters.ReceiptPrintProductsGroupAdapter
//import uz.abubakir_khakimov.yuvish_uz_qadoqlash.adapters.WashedProductAdapter
//import uz.abubakir_khakimov.yuvish_uz_qadoqlash.databinding.*
//import uz.abubakir_khakimov.yuvish_uz_qadoqlash.models.Order
//import uz.abubakir_khakimov.yuvish_uz_qadoqlash.models.Product
//import uz.abubakir_khakimov.yuvish_uz_qadoqlash.models.Shelf
//import uz.abubakir_khakimov.yuvish_uz_qadoqlash.models.SubmitOrder
//import uz.abubakir_khakimov.yuvish_uz_qadoqlash.models.viewModels.MainViewModel
//import uz.abubakir_khakimov.yuvish_uz_qadoqlash.utils.*
//import java.io.File
//
//
//class PackingFragment:
//    Fragment(),
//    ConnectivityManager.CallBack,
//    WashedProductAdapter.CallBack
//{
//    private lateinit var binding: FragmentPackingBinding
//    private lateinit var connectivityManager: ConnectivityManager
//    private lateinit var loadingDialogManager: LoadingDialogManager
//    private lateinit var pdfManager: PdfManager
//    private lateinit var barcodeManager: BarcodeManager
//    private lateinit var printManager: PrintManager
//    private lateinit var viewModel: MainViewModel
//    private val washedProductAdapter: WashedProductAdapter by lazy {
//        WashedProductAdapter(requireActivity(), washedProductsList, this)
//    }
//    private val packedProductAdapter: PackedProductAdapter by lazy {
//        PackedProductAdapter(requireActivity(), packedProductsList)
//    }
//
//    private var orderId: Int? = null
//    private var barcode: String? = null
//    private var selectedOrder: Order? = null
//    private val washedProductsList = ArrayList<Product>()
//    private val packedProductsList = ArrayList<Product>()
//    private val shelfsList = ArrayList<Shelf>()
//    private var toWhereShowNotification = "entryData"
//
//    private var selectedShelfPosition: Int? = null
//    private var productId: Int? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        orderId = arguments?.getInt("orderId")!!
//        barcode = arguments?.getString("barcode", null)
//
//        loadingDialogManager = LoadingDialogManager(requireActivity(), LoadingDialogManager.TYPE_DEFAULT_LOADING)
//        connectivityManager = ConnectivityManager(requireActivity(), this, true)
//        pdfManager = PdfManager(requireActivity())
//        barcodeManager = BarcodeManager()
//        printManager = PrintManager(requireActivity())
//        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
//        initObservers()
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        binding = FragmentPackingBinding.inflate(layoutInflater)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.washedProducts.adapter = washedProductAdapter
//        binding.packedProducts.adapter = packedProductAdapter
//
//        checkListEmpty()
//        if (selectedOrder == null){
//            placeHolderVisible(true)
//            loadOrderById("entryData")
//            viewModel.getShelfs()
//        }else{
//            updateUI(selectedOrder!!)
//        }
//
//        binding.washedPrevious.setOnClickListener {
//            toPreviousPage(binding.washedProducts)
//        }
//
//        binding.washedNext.setOnClickListener {
//            toNextPage(binding.washedProducts, washedProductAdapter.itemCount)
//        }
//
//        binding.packedPrevious.setOnClickListener {
//            toPreviousPage(binding.packedProducts)
//        }
//
//        binding.packedNext.setOnClickListener {
//            toNextPage(binding.packedProducts, packedProductAdapter.itemCount)
//        }
//
//        binding.backStack.setOnClickListener {
//            findNavController().popBackStack()
//        }
//
//        binding.print.setOnClickListener {
//            if (selectedOrder.isNull()){
//                Toast.makeText(requireActivity(), getString(R.string.not_enough_information), Toast.LENGTH_SHORT).show()
//            }else{
//                loadSubmitOrder(orderId!!)
//            }
//        }
//
//    }
//
//    private fun updateUI(order: Order){
//        binding.customerName.text = order.costumer?.costumer_name
//        binding.address.text = order.costumer?.costumer_addres
//        binding.receiptNumber.text = order.nomer.toString()
//        binding.comment.text = order.izoh2
//    }
//
//    private fun loadSubmitOrder(orderId: Int){
//        isLoading(true)
//        viewModel.getSubmitOrder(orderId)
//    }
//
//    private fun loadOrderById(where: String){
//        isLoading(true)
//        toWhereShowNotification = where
//        viewModel.getOrderById(orderId!!)
//    }
//
//    private fun requestForPacking(productId: Int, shelfName: String){
//        isLoading(true)
//        viewModel.requestForPackaging(productId, shelfName)
//    }
//
//    private fun requestForRewashProduct(productId: Int){
//        isLoading(true)
//        viewModel.requestForRewashProduct(productId)
//    }
//
//    private fun initObservers() {
//        viewModel.getOrderByIdData.observe(this){
//            when(it.status){
//                Status.SUCCESS -> {
//                    selectedOrder = it.data
//                    isLoading(false)
//                    placeHolderVisible(false)
//
//                    updateUI(selectedOrder!!)
//                    sortProducts(selectedOrder!!.products!!)
//                    showNotification()
//                    checkListEmpty()
//                }
//                Status.ERROR -> {
//                    isLoading(false)
//                    connectivityManager.checkConnectionError(it.error, "entryData")
//                }
//            }
//        }
//
//        viewModel.getShelfsData.observe(this){
//            when(it.status){
//                Status.SUCCESS -> {
//                    shelfsList.apply {
//                        clear()
//                        addAll(it.data!!)
//                    }
//                }
//                Status.ERROR -> {
//                    connectivityManager.checkConnectionError(it.error, "entryData")
//                }
//            }
//        }
//
//        viewModel.requestForPackagingData.observe(this){
//            when(it.status){
//                Status.SUCCESS -> {
//                    if (it.data == null){
//                        Toast.makeText(requireActivity(), getString(R.string.error), Toast.LENGTH_SHORT).show()
//                        isLoading(false)
//                    }else{
//                        loadOrderById("requestForPackaging")
//                    }
//                }
//                Status.ERROR -> {
//                    isLoading(false)
//                    connectivityManager.checkConnectionError(it.error, "requestForPackaging")
//                }
//            }
//        }
//
//        viewModel.requestForRewashProductData.observe(this){
//            when(it.status){
//                Status.SUCCESS -> {
//                    if (it.data == null){
//                        Toast.makeText(requireActivity(), getString(R.string.error), Toast.LENGTH_SHORT).show()
//                        isLoading(false)
//                    }else{
//                        loadOrderById("requestForRewashProduct")
//                    }
//                }
//                Status.ERROR -> {
//                    isLoading(false)
//                    connectivityManager.checkConnectionError(it.error, "requestForRewashProduct")
//                }
//            }
//        }
//
//        viewModel.getSubmitOrderData.observe(this){
//            when(it.status){
//                Status.SUCCESS -> {
//                    if (it.data.isNull()){
//                        isLoading(false)
//                        Toast.makeText(requireActivity(), getString(R.string.not_enough_information), Toast.LENGTH_SHORT).show()
//                    }else {
//                        printReceipt(it.data!!)
//                    }
//                }
//                Status.ERROR -> {
//                    isLoading(false)
//                    connectivityManager.checkConnectionError(it.error, "getSubmitOrder")
//                }
//            }
//        }
//    }
//
//    override fun refreshClicked(toWhereRefresh: String) {
//        when(toWhereRefresh){
//            "entryData" -> {
//                loadOrderById("entryData")
//                viewModel.getShelfs()
//            }
//            "requestForPackaging" -> {
//                requestForPacking(
//                    productId!!,
//                    if (getShelfPermission()) shelfsList[selectedShelfPosition!!].name else ""
//                )
//            }
//            "requestForRewashProduct" -> {
//                requestForRewashProduct(productId!!)
//            }
//            "getSubmitOrder" -> {
//                loadSubmitOrder(orderId!!)
//            }
//        }
//    }
//
//    private fun showNotification(){
//        when(toWhereShowNotification){
//            "requestForPackaging" -> {
//                showSnackBar(getString(R.string.success_accepted_packaging))
//            }
//            "requestForRewashProduct" -> {
//                showSnackBar(getString(R.string.success_accepted_rewashing))
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
//    private fun sortProducts(productsList: List<Product>){
//        washedProductAdapter.barcodeScanned = false
//        washedProductsList.clear()
//        packedProductsList.clear()
//
//        productsList.forEach {
//            if (it.clean_status == "quridi" || it.clean_status == "qayta quridi"){
//                if (it.barcode == barcode){
//                    washedProductsList.add(0, it)
//                    washedProductAdapter.barcodeScanned = true
//                }else{
//                    washedProductsList.add(it)
//                }
//            }else if (it.clean_status == "qadoqlandi" || it.clean_status == "qayta qadoqlandi"){
//                packedProductsList.add(it)
//            }
//        }
//
//        washedProductAdapter.notifyDataSetChanged()
//        packedProductAdapter.notifyDataSetChanged()
//    }
//
//    private fun checkListEmpty(){
//        if (washedProductsList.isEmpty()){
//            binding.washedProducts.visibility = View.GONE
//            binding.washedNoProductsTitle.visibility = View.VISIBLE
//        }else{
//            binding.washedProducts.visibility = View.VISIBLE
//            binding.washedNoProductsTitle.visibility = View.GONE
//        }
//
//        if (packedProductsList.isEmpty()){
//            binding.packedProducts.visibility = View.GONE
//            binding.packedNoProductsTitle.visibility = View.VISIBLE
//        }else{
//            binding.packedProducts.visibility = View.VISIBLE
//            binding.packedNoProductsTitle.visibility = View.GONE
//        }
//    }
//
//    override fun printClickListener(product: Product) {
//        printProductBarcode(product)
//    }
//
//    override fun rewashClickListener(product: Product) {
//        productId = product.id
//        requestForRewashProduct(product.id)
//    }
//
//    override fun toPacking(product: Product) {
//        if (getShelfPermission()) {
//            showSelectShelfDialog(product)
//        }else{
//            productId = product.id
//            requestForPacking(product.id, "")
//        }
//    }
//
//    private fun getShelfPermission(): Boolean{
//        return GlobalData.commonSettings?.tokcha!!
//    }
//
//    private fun showSelectShelfDialog(product: Product){
//        val customDialog = AlertDialog.Builder(requireActivity()).create()
//        val dialogBinding = SelectShelfDialogLayoutBinding.inflate(layoutInflater)
//        customDialog.setView(dialogBinding.root)
//        customDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//        if (shelfsList.isEmpty()){
//            selectedShelfPosition = null
//        }else{
//            selectedShelfPosition = 0
//
//            val shelfsAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, shelfsList.map { it.name })
//            dialogBinding.shelf.setAdapter(shelfsAdapter)
//            dialogBinding.shelf.setText(shelfsList[selectedShelfPosition!!].name, false)
//        }
//
//        dialogBinding.shelf.setOnItemClickListener { parent, view, position, id ->
//            selectedShelfPosition = position
//        }
//
//        dialogBinding.close.setOnClickListener {
//            customDialog.dismiss()
//        }
//
//        dialogBinding.confirm.setOnClickListener {
//            if (selectedShelfPosition == null){
//                Toast.makeText(requireActivity(), getString(R.string.shelf_not_selected), Toast.LENGTH_SHORT).show()
//            }else{
//                productId = product.id
//                requestForPacking(
//                    product.id,
//                    shelfsList[selectedShelfPosition!!].name
//                )
//                customDialog.dismiss()
//            }
//        }
//
//        customDialog.show()
//    }
//
//    private fun isLoading(bool:Boolean){
//        if (bool){
//            loadingDialogManager.show()
//        }else{
//            loadingDialogManager.dismiss()
//        }
//    }
//
//    private fun placeHolderVisible(visible: Boolean){
//        if (visible){
//            binding.customerNameShimmerPH.visibility = View.VISIBLE
//            binding.addressShimmerPH.visibility = View.VISIBLE
//            binding.receiptNumberShimmerPH.visibility = View.VISIBLE
//            binding.commentShimmerPH.visibility = View.VISIBLE
//
//            binding.customerName.visibility = View.GONE
//            binding.address.visibility = View.GONE
//            binding.receiptNumber.visibility = View.GONE
//            binding.comment.visibility = View.GONE
//
//            binding.customerNameShimmerPH.startShimmer()
//            binding.addressShimmerPH.startShimmer()
//            binding.receiptNumberShimmerPH.startShimmer()
//            binding.commentShimmerPH.startShimmer()
//        }else{
//            binding.customerNameShimmerPH.visibility = View.GONE
//            binding.addressShimmerPH.visibility = View.GONE
//            binding.receiptNumberShimmerPH.visibility = View.GONE
//            binding.commentShimmerPH.visibility = View.GONE
//
//            binding.customerName.visibility = View.VISIBLE
//            binding.address.visibility = View.VISIBLE
//            binding.receiptNumber.visibility = View.VISIBLE
//            binding.comment.visibility = View.VISIBLE
//
//            binding.customerNameShimmerPH.stopShimmer()
//            binding.addressShimmerPH.stopShimmer()
//            binding.receiptNumberShimmerPH.stopShimmer()
//            binding.commentShimmerPH.stopShimmer()
//        }
//    }
//
//    private fun showSnackBar(text: String){
//        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun printProductBarcode(product: Product){
//        lifecycleScope.launch(Dispatchers.Main){
//            isLoading(true)
//
//            val printBinding = ProductsBarcodePrintLayoutBinding.inflate(layoutInflater)
//            val logoBitmap = loadLogo(
//                "${Constants.IMAGE_BASE_URL}${GlobalData.currentUser!!.filial.logo}"
//            )
//            val barcodeBitmap = barcodeManager.createImage(product.barcode)
//
//            printBinding.logo.setImageBitmap(logoBitmap)
//            printBinding.barcode.setImageBitmap(barcodeBitmap)
//            printBinding.service.text = product.xizmat.xizmat_turi
//            printBinding.price.text =
//                "${getString(R.string.price)}: ${product.narx} ${getString(R.string.sum)} " +
//                        "(1 ${getLocalizedMeasure(product.xizmat.olchov)})"
//
//            generatePdf(printBinding.root, Constants.BARCODE_PRINT_FILE_NAME)
//        }
//    }
//
//    private suspend fun loadLogo(url: String): Bitmap{
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
//    private suspend fun generatePdf(view: View, fileName: String){
//        val pdfGeneratorListener = object : PdfGeneratorListener() {
//            override fun onStartPDFGeneration() {
//
//            }
//
//            override fun onFinishPDFGeneration() {
//                isLoading(false)
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
//    private fun getLocalizedMeasure(measure: String): String{
//        return when(measure){
//            "metr" -> {
//                getString(R.string.meter_square)
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
//    @SuppressLint("SetTextI18n")
//    private fun printReceipt(submitOrder: SubmitOrder){
//        lifecycleScope.launch(Dispatchers.Main){
//            isLoading(true)
//
//            val printBinding = ReceiptPrintLayoutBinding.inflate(layoutInflater)
//            val logoBitmap = loadLogo(
//                "${Constants.IMAGE_BASE_URL}${GlobalData.currentUser!!.filial.logo}"
//            )
//            val receiptPrintProductsGroupAdapter = ReceiptPrintProductsGroupAdapter(
//                submitOrder.buyurtmalar, requireActivity()
//            )
//
//            printBinding.logo.setImageBitmap(logoBitmap)
//            printBinding.productsRV.adapter = receiptPrintProductsGroupAdapter
//            printBinding.companyName.text = GlobalData.currentUser?.filial?.filial_name
//            printBinding.address.text = GlobalData.currentUser?.filial?.filial_address
//            printBinding.destination.text = GlobalData.currentUser?.filial?.filial_destination
//            printBinding.phoneNumber.text = "+998${GlobalData.currentUser?.filial?.filial_phone}"
//            printBinding.receiptNumber.text = submitOrder.nomer.toString()
//            printBinding.totalAmount.text = "${submitOrder.jami_summa} ${getString(R.string.sum)}"
//            printBinding.finalAmount.text = "${submitOrder.yakuniy_summa} ${getString(R.string.sum)}"
//            updateDiscounts(printBinding, submitOrder)
//
//            generatePdf(printBinding.root, Constants.RECEIPT_PRINT_FILE_NAME)
//        }
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun updateDiscounts(printBinding: ReceiptPrintLayoutBinding, submitOrder: SubmitOrder){
//        if (submitOrder.jami_chegirma == 0){
//            printBinding.discountsLayout.visibility = View.GONE
//        }else{
//            printBinding.discountsLayout.visibility = View.VISIBLE
//            printBinding.totalDiscount.text = "${submitOrder.jami_chegirma} %"
//
//            if (submitOrder.qayta_yuvish_chegirma == 0){
//                printBinding.rewashDiscountLayout.visibility = View.GONE
//                printBinding.rewashDiscountDivider.visibility = View.GONE
//            }else{
//                printBinding.rewashDiscountLayout.visibility = View.VISIBLE
//                printBinding.rewashDiscountDivider.visibility = View.VISIBLE
//                printBinding.rewashDiscount.text = "${submitOrder.qayta_yuvish_chegirma} ${getString(R.string.sum)}"
//            }
//
//            if (submitOrder.own_chegirma == 0){
//                printBinding.customerPickupDiscountLayout.visibility = View.GONE
//                printBinding.customerPickupDiscountDivider.visibility = View.GONE
//            }else{
//                printBinding.customerPickupDiscountLayout.visibility = View.VISIBLE
//                printBinding.customerPickupDiscountDivider.visibility = View.VISIBLE
//                printBinding.customerPickupDiscount.text = "${submitOrder.own_chegirma} ${getString(R.string.sum)}"
//            }
//
//            if (submitOrder.tushish_chegirma == 0){
//                printBinding.droppedDiscountLayout.visibility = View.GONE
//                printBinding.droppedDiscountDivider.visibility = View.GONE
//            }else{
//                printBinding.droppedDiscountLayout.visibility = View.VISIBLE
//                printBinding.droppedDiscountDivider.visibility = View.VISIBLE
//                printBinding.droppedDiscount.text = "${submitOrder.tushish_chegirma} ${getString(R.string.sum)}"
//            }
//
//            if (submitOrder.jami_tushish_chegirma == 0){
//                printBinding.totalDroppedDiscountLayout.visibility = View.GONE
//                printBinding.totalDroppedDiscountDivider.visibility = View.GONE
//            }else{
//                printBinding.totalDroppedDiscountLayout.visibility = View.VISIBLE
//                printBinding.totalDroppedDiscountDivider.visibility = View.VISIBLE
//                printBinding.totalDroppedDiscount.text = "${submitOrder.jami_tushish_chegirma} ${getString(R.string.sum)}"
//            }
//        }
//    }
//
//}