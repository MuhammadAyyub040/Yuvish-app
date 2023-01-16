package com.example.yuvish.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.yuvish.Adapters.SelectedServicesAdapter
import com.example.yuvish.models.BarcodeApi.Order
import com.example.yuvish.models.NewOrder.PutOrder
import com.example.yuvish.models.NewOrder.PutService
import com.example.yuvish.models.NewOrder.SelectedService
import com.example.yuvish.models.NewOrder.Service
import com.example.yuvish.R
import com.example.yuvish.databinding.FragmentListBinding
import com.example.yuvish.models.DebtorsAPI.Market.ResponseDetail
import com.example.yuvish.retrofit.ApiClient
import com.example.yuvish.retrofit.GlobalData
import com.example.yuvish.retrofit.isNull
import com.google.android.material.datepicker.MaterialDatePicker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ListFragment : Fragment(), SelectedServicesAdapter.CallBack {

    lateinit var binding: FragmentListBinding
    private lateinit var servicesAdapter: ArrayAdapter<String>
    private val selectedServicesAdapter: SelectedServicesAdapter by lazy {
        SelectedServicesAdapter(selectedServicesList, this, requireActivity())
    }

    private var orderId: Int? = null
    private var selectedServicePosition = 0
    private var selectedInvalidPosition = 0
    private var selectedStainPosition = 0
    private val selectedServicesList = ArrayList<SelectedService>()
    private var servicesList: ArrayList<Service>? = null
    private var order: Order? = null
    private var confirmList: List<String>? = null


    private var putOrder: PutOrder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = arguments?.getInt("customerId")
        if (orderId != null) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectedServicesRv.adapter = selectedServicesAdapter

        binding.services.setOnItemClickListener { parent, view, position, id ->
            selectedServicePosition = position
        }

        getService()

        if (confirmList.isNull()){
            confirmList = listOf(getString(R.string.no), getString(R.string.there_is))
        }

        if (order.isNull()){
            getNewOrder(orderId!!)
        }else{
            updateUI(order!!)
        }

        if (changeInvalidVisible()){
            updateInvalidDropDown()
        }

        if (changeStainVisible()){
            updateStainDropDown()
        }

        if (servicesList.isNull()){
            getService()
        }else{
            updateDropDown()
        }

        binding.addService.setOnClickListener {
            if (servicesList.isNull() || servicesList!!.isEmpty()){
                Toast.makeText(requireActivity(), getString(R.string.no_services), Toast.LENGTH_SHORT).show()
            }else if (binding.count.text.toString().isEmpty()){
                binding.count.error = getString(R.string.enter_quantities)
            }else {
                addService()
            }
        }

        binding.backStack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.date.setOnClickListener {
            showDatePicker()
        }

        binding.invalid.setOnItemClickListener { parent, view, position, id ->
            selectedInvalidPosition = position
        }

        binding.stain.setOnItemClickListener { parent, view, position, id ->
            selectedStainPosition = position
        }

        binding.registration.setOnClickListener {
            checkConfirmData()
        }

        binding.customerPickupLayout.setOnClickListener {
            binding.customerPickupCheckBox.isChecked = !binding.customerPickupCheckBox.isChecked
        }

        binding.customerPickupCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                binding.customerPickupImg.visibility = View.VISIBLE
            }else{
                binding.customerPickupImg.visibility = View.GONE
            }
        }

        binding.count.addTextChangedListener {
            val countStr = it.toString()
            val count = countStr.toIntOrNull() ?: 0

            if (count > 60){
                binding.count.setText(countStr.trimEndChar())
                binding.count.setSelection(countStr.length - 1)
                Toast.makeText(requireActivity(), getString(R.string.no_more_60_products), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun String.trimEndChar(): String{
        return if (this.isEmpty()){
            this
        }else {
            this.substring(0, this.length - 1)
        }
    }

    private fun getService() {
        ApiClient.retrofitService.getServices().enqueue(object : Callback<List<Service>> {
            override fun onResponse(call: Call<List<Service>>, response: Response<List<Service>>) {
                if (response.code() == 200) {
                    servicesList = response.body() as ArrayList<Service>?
                    val arrayAdapter2 = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, servicesList!!.map { it.xizmat_turi })
                    binding.services.setAdapter(arrayAdapter2)
                }
            }

            override fun onFailure(call: Call<List<Service>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Ma'lumot kelmadi", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getNewOrder(orderId: Int) {
        ApiClient.retrofitService.getNewOrderById(orderId).enqueue(object : Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.code() == 200) {
                    order = response.body()
                    binding.customerName.text = order?.costumer?.costumer_name
                    binding.address.text = order?.costumer?.costumer_addres
                    binding.receiptNumber.text = order?.nomer.toString()
                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), "Ma'lumot kelmadi", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun updateUI(order: Order){
        binding.customerName.text = order.costumer.costumer_name
        binding.address.text = order.costumer.costumer_addres
        binding.receiptNumber.text = order.nomer.toString()
    }

    private fun putNewOrder(orderId: Int, putOrder: PutOrder){
        ApiClient.retrofitService.putNewOrder(orderId, putOrder).enqueue(object : Callback<ResponseDetail>{
            override fun onResponse(call: Call<ResponseDetail>, response: Response<ResponseDetail>) {
                if (response.code() == 200){
                    findNavController().navigate(R.id.action_listFragment_to_confirmationOrderFragment, bundleOf(
                        "orderId" to orderId))
                }
            }

            override fun onFailure(call: Call<ResponseDetail>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(requireContext(), getString(R.string.put_error), Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun checkConfirmData(){
        val date = binding.date.text.toString()
        val comment = binding.comment.text.toString()

        when{
            selectedServicesList.isEmpty() -> {
                Toast.makeText(requireActivity(), getString(R.string.service_not_selected), Toast.LENGTH_SHORT).show()
            }
            date == "--.--.----" -> {
                Toast.makeText(requireActivity(), getString(R.string.date_empty), Toast.LENGTH_SHORT).show()
            }
            !date.isAfterToday() -> {
                Toast.makeText(requireActivity(), getString(R.string.after_today_date_error), Toast.LENGTH_SHORT).show()
            }
            else -> {
                putOrder = PutOrder(
                    if (selectedInvalidPosition == 0) "yo`q" else "bor",
                    if (selectedStainPosition == 0) "yo`q" else "bor",
                    changeDateStructure(date),
                    comment,
                    if (binding.customerPickupCheckBox.isChecked) 1 else 0,
                    selectedServicesList.map {
                        PutService(it.count, it.service.xizmat_id)
                    }
                )

                putNewOrder(orderId!!, putOrder!!)
            }
        }
    }

    private fun changeDateStructure(date: String): String {
        val dateArray = date.split(".") //dd.MM.yyyy
        return "${dateArray[2]}-${dateArray[1]}-${dateArray[0]}" //yyyy-MM-dd
    }

    private fun Long.isAfterToday(): Boolean{
        return Date(this).after(Date(Date().time - 86400000))
    }

    private fun String.isAfterToday(): Boolean{
        val dateArray = this.split(".")

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.DAY_OF_MONTH,dateArray[0].toInt())
        calendar.set(Calendar.MONTH,dateArray[1].toInt()-1)
        calendar.set(Calendar.YEAR,dateArray[2].toInt())

        return calendar.timeInMillis.isAfterToday()
    }

    private fun showDatePicker(){
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener {
            if (it.isAfterToday()){
                binding.date.setText(getDateStringFormat(it))
            }else{
                Toast.makeText(requireActivity(), getString(R.string.after_today_date_error), Toast.LENGTH_SHORT).show()
            }
        }

        picker.show(requireActivity().supportFragmentManager, "tag_picker")
    }

    private fun getDateStringFormat(it: Long): String{
        return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(it))
    }

    private fun addService(){
        selectedServicesList.add(0,
            SelectedService(servicesList!![selectedServicePosition], binding.count.text.toString().toInt())
        )
        selectedServicesAdapter.notifyItemInserted(0)
        selectedServicesAdapter.notifyItemRangeChanged(0, selectedServicesList.size)
        binding.count.text?.clear()

        servicesList!!.removeAt(selectedServicePosition)
        selectedServicePosition = 0
        updateDropDown()
    }

    private fun updateDropDown(){
        servicesAdapter = ArrayAdapter(
            requireActivity(), android.R.layout.simple_list_item_1, servicesList!!.map { it.xizmat_turi }
        )
        binding.services.setAdapter(servicesAdapter)

        if (servicesList!!.isNotEmpty()) {
            binding.services.setText(servicesList!![selectedServicePosition].xizmat_turi, false)
        }else{
            binding.services.text.clear()
        }
    }

    override fun deleteClickListener(position: Int) {
        servicesList!!.add(0, selectedServicesList[position].service)
        updateDropDown()

        selectedServicesList.removeAt(position)
        selectedServicesAdapter.notifyDataSetChanged()
    }

    private fun changeInvalidVisible(): Boolean {
        val invalidVisible = GlobalData.commonSettings?.order_brak == 1
        binding.invalidCard.isVisible = invalidVisible
        binding.invalidTitle.isVisible = invalidVisible
        return invalidVisible
    }

    private fun changeStainVisible(): Boolean {
        val stainVisible = GlobalData.commonSettings?.order_dog == 1
        binding.stainCard.isVisible = stainVisible
        binding.stainTitle.isVisible = stainVisible
        return stainVisible
    }

    private fun updateInvalidDropDown(){
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, confirmList!!)
        binding.invalid.setAdapter(adapter)
        binding.invalid.setText(confirmList!![selectedInvalidPosition], false)
    }

    private fun updateStainDropDown(){
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, confirmList!!)
        binding.stain.setAdapter(adapter)
        binding.stain.setText(confirmList!![selectedStainPosition], false)
    }


}