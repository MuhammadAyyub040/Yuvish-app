//package uz.abubakir_khakimov.yuvish_uz_qadoqlash.fragments.keyIndicators
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.core.os.bundleOf
//import androidx.core.view.isGone
//import androidx.core.view.isVisible
//import androidx.lifecycle.ViewModelProvider
//import androidx.navigation.fragment.findNavController
//import com.facebook.shimmer.ShimmerFrameLayout
//import com.google.android.material.datepicker.MaterialDatePicker
//import uz.abubakir_khakimov.yuvish_uz_qadoqlash.R
//import uz.abubakir_khakimov.yuvish_uz_qadoqlash.databinding.FragmentKeyIndicatorsBinding
//import uz.abubakir_khakimov.yuvish_uz_qadoqlash.models.SearchIndicatorsResult
//import uz.abubakir_khakimov.yuvish_uz_qadoqlash.models.viewModels.MainViewModel
//import uz.abubakir_khakimov.yuvish_uz_qadoqlash.utils.*
//import java.text.SimpleDateFormat
//import java.util.*
//
//class KeyIndicatorsFragment
//    : Fragment(),
//    ConnectivityManager.CallBack
//{
//
//    private lateinit var binding: FragmentKeyIndicatorsBinding
//    private lateinit var viewModel: MainViewModel
//    private lateinit var connectivityManager: ConnectivityManager
//    private lateinit var searchingDialogManager: LoadingDialogManager
//
//    private var searchIndicatorsResult: SearchIndicatorsResult? = null
//
//    //data needed for refresh
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        connectivityManager = ConnectivityManager(requireActivity(), this, true)
//        searchingDialogManager = LoadingDialogManager(requireActivity(), LoadingDialogManager.TYPE_SEARCHING)
//        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
//        initObservers()
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        binding = FragmentKeyIndicatorsBinding.inflate(layoutInflater)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        resetDate()
//
//        if (searchIndicatorsResult.isNull()){
//            placeHolderVisible(true)
//            checkSearching(false)
//        }else{
//            placeHolderVisible(false)
//            updateUI(searchIndicatorsResult!!)
//        }
//
//        binding.backStack.setOnClickListener {
//            findNavController().popBackStack()
//        }
//
//        binding.fromDateLayout.setEndIconOnClickListener {
//            showDatePicker(0)
//        }
//
//        binding.toDateLayout.setEndIconOnClickListener {
//            showDatePicker(1)
//        }
//
//        binding.globalSearch.setOnClickListener {
//            findNavController().navigate(R.id.action_keyIndicatorsFragment_to_globalSearchFragment)
//        }
//
//        binding.searchIndicators.setOnClickListener {
//            checkSearching()
//        }
//
//        binding.resetDate.setOnClickListener {
//            resetDate()
//            checkSearching()
//        }
//
//        binding.packedLayout.setOnClickListener {
//            val bundle = getDateBundle()
//            if (bundle.isNotNull()) {
//                findNavController().navigate(
//                    R.id.action_keyIndicatorsFragment_to_packedIndicatorsFragment,
//                    bundle
//                )
//            }
//        }
//
//        binding.submittedLayout.setOnClickListener {
//            val bundle = getDateBundle()
//            if (bundle.isNotNull()) {
//                findNavController().navigate(
//                    R.id.action_keyIndicatorsFragment_to_submittedIndicatorsFragment,
//                    bundle
//                )
//            }
//        }
//
//        binding.kpiLayout.setOnClickListener {
//            val bundle = getDateBundle()
//            if (bundle.isNotNull()) {
//                findNavController().navigate(
//                    R.id.action_keyIndicatorsFragment_to_kpiIndicatorsFragment,
//                    bundle
//                )
//            }
//        }
//
//        binding.receivedSalaryLayout.setOnClickListener {
//            val bundle = getDateBundle()
//            if (bundle.isNotNull()) {
//                findNavController().navigate(
//                    R.id.action_keyIndicatorsFragment_to_receivedSalaryIndicatorsFragment,
//                    bundle
//                )
//            }
//        }
//
//        binding.rewashReceivedLayout.setOnClickListener {
//            val bundle = getDateBundle()
//            if (bundle.isNotNull()) {
//                findNavController().navigate(
//                    R.id.action_keyIndicatorsFragment_to_receivedRewashIndicatorsFragment,
//                    bundle
//                )
//            }
//        }
//
//    }
//
//    private fun getDateBundle(): Bundle?{
//        val fromDate = binding.fromDate.text.toString()
//        val toDate = binding.toDate.text.toString()
//
//        return if (checkCorrectDate(fromDate, toDate)) {
//            bundleOf(
//                "fromDate" to fromDate.changeDateStructure(),
//                "toDate" to toDate.changeDateStructure()
//            )
//        }else{
//            null
//        }
//    }
//
//    private fun checkSearching(onLoading: Boolean = true) {
//        val fromDate = binding.fromDate.text.toString()
//        val toDate = binding.toDate.text.toString()
//
//
//        if (checkCorrectDate(fromDate, toDate)) {
//            searchIndicators(
//                fromDate.changeDateStructure(),
//                toDate.changeDateStructure(),
//                onLoading
//            )
//        }
//    }
//
//    private fun checkCorrectDate(fromDate: String, toDate: String): Boolean {
//        return when {
//            fromDate == "--.--.----" -> {
//                Toast.makeText(requireActivity(), getString(R.string.from_date_empty), Toast.LENGTH_SHORT).show()
//                false
//            }
//            toDate == "--.--.----" -> {
//                Toast.makeText(requireActivity(), getString(R.string.to_date_empty), Toast.LENGTH_SHORT).show()
//                false
//            }
//            fromDate.isBefore(getBeginningMonth()) -> {
//                Toast.makeText(requireActivity(), getString(R.string.entered_date_beginning_this_month), Toast.LENGTH_SHORT).show()
//                false
//            }
//            else -> {
//                true
//            }
//        }
//    }
//
//    /*** When reset, datePickers will revert to today's date.*/
//    private fun resetDate(){
//        binding.toDate.setText(getDateStringFormat(Date().time))
//        binding.fromDate.setText(getDateStringFormat(Date().time))
//    }
//
//    private fun searchIndicators(fromDate: String, toDate: String, onLoading: Boolean = true){
//        isSearching(onLoading)
//        viewModel.searchIndicators(fromDate, toDate)
//    }
//
//    private fun initObservers(){
//        viewModel.searchIndicatorsData.observe(this){
//            when(it.status){
//                Status.SUCCESS -> {
//                    isSearching(false)
//                    placeHolderVisible(false)
//                    searchIndicatorsResult = it.data
//                    updateUI(searchIndicatorsResult!!)
//                }
//                Status.ERROR -> {
//                    isSearching(false)
//                    connectivityManager.checkConnectionError(it.error, "searchIndicators")
//                }
//            }
//        }
//    }
//
//    override fun refreshClicked(toWhereRefresh: String) {
//        when(toWhereRefresh){
//            "searchIndicators" -> {
//                checkSearching()
//            }
//        }
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun updateUI(searchIndicatorsResult: SearchIndicatorsResult){
//        binding.packedCount.text =
//            "${searchIndicatorsResult.mahsulot_qadoqlandi.tovar_dona} ${getString(R.string.pcs)}"
//        binding.packedVolume.text =
//            "${searchIndicatorsResult.mahsulot_qadoqlandi.tovar_kv} ${getString(R.string.meter_square)}"
//        binding.submittedCount.text =
//            "${searchIndicatorsResult.mahsulot_topshirildi.tovar_dona} ${getString(R.string.pcs)}"
//        binding.submittedVolume.text =
//            "${searchIndicatorsResult.mahsulot_topshirildi.tovar_kv} ${getString(R.string.meter_square)}"
//        binding.kpiAmount.text =
//            "${searchIndicatorsResult.kunlik_maosh} ${getString(R.string.sum)}"
//        binding.receivedSalaryAmount.text =
//            "${searchIndicatorsResult.berilgan_maoshlar_sum} ${getString(R.string.sum)}"
//        binding.rewashReceivedCount.text =
//            "${searchIndicatorsResult.qayta_yuvishga_olindi.tovar_dona} ${getString(R.string.pcs)}"
//        binding.rewashReceivedVolume.text =
//            "${searchIndicatorsResult.qayta_yuvishga_olindi.tovar_kv} ${getString(R.string.meter_square)}"
//
//        binding.userName.text = GlobalData.currentUser!!.fullname
//        updateArrivalTime(searchIndicatorsResult)
//        updateGoneTime(searchIndicatorsResult)
//    }
//
//    private fun updateArrivalTime(searchIndicatorsResult: SearchIndicatorsResult) {
//        binding.arrivalTime.text = if (searchIndicatorsResult.today_davomat?.keldi == 1) {
//            searchIndicatorsResult.today_davomat.keldi_time
//        } else {
//            getString(R.string.did_not_come)
//        }
//    }
//
//    private fun updateGoneTime(searchIndicatorsResult: SearchIndicatorsResult) {
//        binding.goneTime.text = if (searchIndicatorsResult.today_davomat?.ketdi == 1) {
//            searchIndicatorsResult.today_davomat.ketdi_time
//        } else {
//            getString(R.string.did_not_leave)
//        }
//    }
//
//    private fun showDatePicker(from: Int){
//        val builder = MaterialDatePicker.Builder.datePicker()
//        val picker = builder.build()
//
//        picker.addOnPositiveButtonClickListener {
//            when(from){
//                0 -> {
//                    if (it.isBefore(getBeginningMonth())){
//                        Toast.makeText(requireActivity(), getString(R.string.entered_date_beginning_this_month), Toast.LENGTH_SHORT).show()
//                    }else{
//                        binding.fromDate.setText(getDateStringFormat(it))
//                    }
//                }
//                1 -> {
//                    binding.toDate.setText(getDateStringFormat(it))
//                }
//            }
//        }
//
//        picker.show(requireActivity().supportFragmentManager, "tag_picker")
//    }
//
//    private fun getBeginningMonth(): Long{
//        val calendar = Calendar.getInstance()
//        calendar.time = Date()
//        calendar.set(Calendar.MILLISECOND, 0)
//        calendar.set(Calendar.SECOND, 0)
//        calendar.set(Calendar.MINUTE, 0)
//        calendar.set(Calendar.HOUR_OF_DAY, 0)
//        calendar.set(Calendar.DAY_OF_MONTH, 1)
//        return calendar.timeInMillis
//    }
//
//    private fun Long.isBefore(it: Long): Boolean {
//        return Date(this).before(Date(it))
//    }
//
//    private fun String.isBefore(it: Long): Boolean{
//        val dateArray = this.split(".")
//
//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.MILLISECOND, 0)
//        calendar.set(Calendar.SECOND, 0)
//        calendar.set(Calendar.MINUTE, 0)
//        calendar.set(Calendar.HOUR_OF_DAY, 0)
//        calendar.set(Calendar.DAY_OF_MONTH,dateArray[0].toInt())
//        calendar.set(Calendar.MONTH,dateArray[1].toInt()-1)
//        calendar.set(Calendar.YEAR,dateArray[2].toInt())
//
//        return calendar.timeInMillis.isBefore(it)
//    }
//
//    private fun getDateStringFormat(it: Long): String{
//        return SimpleDateFormat("dd.MM.yyyy").format(Date(it))
//    }
//
//    private fun placeHolderVisible(visible: Boolean){
//        binding.packedCountShimmer.isVisible = visible
//        binding.packedVolumeShimmer.isVisible = visible
//        binding.submittedCountShimmer.isVisible = visible
//        binding.submittedVolumeShimmer.isVisible = visible
//        binding.kpiAmountShimmer.isVisible = visible
//        binding.receivedSalaryAmountShimmer.isVisible = visible
//        binding.rewashReceivedCountShimmer.isVisible = visible
//        binding.rewashReceivedVolumeShimmer.isVisible = visible
//        binding.arrivalTimeShimmer.isVisible = visible
//        binding.goneTimeShimmer.isVisible = visible
//
//        binding.packedCount.isGone = visible
//        binding.packedVolume.isGone = visible
//        binding.submittedCount.isGone = visible
//        binding.submittedVolume.isGone = visible
//        binding.kpiAmount.isGone = visible
//        binding.receivedSalaryAmount.isGone = visible
//        binding.rewashReceivedCount.isGone = visible
//        binding.rewashReceivedVolume.isGone = visible
//        binding.arrivalTime.isGone = visible
//        binding.goneTime.isGone = visible
//
//        binding.packedCountShimmer.changeShimmerState(visible)
//        binding.packedVolumeShimmer.changeShimmerState(visible)
//        binding.submittedCountShimmer.changeShimmerState(visible)
//        binding.submittedVolumeShimmer.changeShimmerState(visible)
//        binding.kpiAmountShimmer.changeShimmerState(visible)
//        binding.receivedSalaryAmountShimmer.changeShimmerState(visible)
//        binding.rewashReceivedCountShimmer.changeShimmerState(visible)
//        binding.rewashReceivedVolumeShimmer.changeShimmerState(visible)
//        binding.arrivalTimeShimmer.changeShimmerState(visible)
//        binding.goneTimeShimmer.changeShimmerState(visible)
//    }
//
//    private fun ShimmerFrameLayout.changeShimmerState(state: Boolean){
//        if (state){
//            this.startShimmer()
//        }else{
//            this.stopShimmer()
//        }
//    }
//
//    private fun isSearching(bool:Boolean){
//        if (bool){
//            searchingDialogManager.show()
//        }else{
//            searchingDialogManager.dismiss()
//        }
//    }
//
//}