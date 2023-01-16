package com.example.yuvish.retrofit

import com.airbnb.lottie.L
import com.example.yuvish.models.ArrangedSubmit.Submit
import com.example.yuvish.models.ArrangedSubmit.SubmittingOrder
import com.example.yuvish.models.ArrangedSubmit.SubmittingOrderResponse
import com.example.yuvish.models.Authorization.UserToken
import com.example.yuvish.models.BarcodeApi.Order
import com.example.yuvish.models.baseIndikatorsIndex.*
import com.example.yuvish.models.Cleaning.CleaningData
import com.example.yuvish.models.CommonSettings
import com.example.yuvish.models.DebtorsAPI.Market.*
import com.example.yuvish.models.DebtorsPackage.DebtOff
import com.example.yuvish.models.HolatPaneli.WashingStatusAPI
import com.example.yuvish.models.HolatPaneli.TransportStatusAPI
import com.example.yuvish.models.NewOrder.*
import com.example.yuvish.models.ReadyOrders.Arranging
import com.example.yuvish.models.ReadyOrders.Autocomplete
import com.example.yuvish.models.ReadyOrders.ReadyOrdersItem
import com.example.yuvish.models.ReadyOrders.ViewOrderItem
import com.example.yuvish.models.Registration.Registration
import com.example.yuvish.models.Registration.ServiceTypeItem
import com.example.yuvish.models.Setting.ResponseSetting
import com.example.yuvish.models.Setting.Setting
import com.example.yuvish.models.Setting.UpdateSetting
import com.example.yuvish.models.Tokchalar.ShelfItem
import com.example.yuvish.models.Warehouse.BasePagingModel
import com.example.yuvish.models.Warehouse.WarehouseData
import com.example.yuvish.models.addCostumer.addCostumerResponse
import com.example.yuvish.models.addCostumer.createOrder
import com.example.yuvish.models.globalSearch.SearchReceiptResult
import com.example.yuvish.models.searchCustomer.SearchReceipt
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {

    @GET("common_settings")
    fun getCommonSettings(): Call<CommonSettings>

    //LoginPage

    @FormUrlEncoded
    @POST("login")
    fun createUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<UserToken>

    //GlobalSearchFragment requests

    @POST("search_by_kv_id")
    fun searchReceiptById(
        @Body searchReceipt: SearchReceipt
    ): Call<SearchReceiptResult>

    //Washing

    @GET("order/yuvish")
   suspend fun cleaning(
        @Query("status") status: String,
        @Query("page") page: Int
    ): Response<BasePagingModel<CleaningData>>

    @POST("with_barcode_for_packaging")
     fun getOrderIdByBarcode(
        @Query("barcode") barcode: String
    ): Call<Int>

    @POST("with_barcode_for_packaging")
     fun getOrderById(
        @Query("order_id") orderId: Int
    ): Call<Order>

     @GET("order_cleaning/{order_id}")
     fun registrationApi(
         @Path("order_id") orderId: Int
     ): Call<Registration>

     @POST("add_one_product")
     fun addOrders(
         @Query("order_id") orderId: Int,
         @Query("xizmat_id") xizmatId: Int
     ): Call<String?>

     @GET("xizmatlar")
     fun serviceType(): Call<List<ServiceTypeItem>>

    @GET("tokchalar")
    fun getShelfs(): Call<List<ShelfItem>>

   @GET("holat_paneli")
   fun statusBar(): Call<WashingStatusAPI>

   //Arranged

   @GET("clean/tayyor")
   suspend fun arranged(
       @Query("page") page: Int,
       @Query("driver") driver: Int,
       @Query("type") type: String
   ): Response<BasePagingModel<ReadyOrdersItem>>

   @GET("holat_paneli_for_transport")
   fun transportStatusBar(): Call<TransportStatusAPI>

   @GET("clean/transport")
   fun autocomplete(): Call<List<Autocomplete>>

   @GET("order_items/{order_id}")
   fun viewOrder(
       @Path("order_id") order_Id: Int
   ): Call<ViewOrderItem>

    @POST("order/tartib")
    fun arranging(
        @Body arranging: Arranging
    ): Call<Arranging>

    //submit

   @GET("order/yakuniy/{order_id}")
   fun submit(
       @Path("order_id") order_Id: Int
   ): Call<Submit>

   @GET("payment_methods")
   fun paymentTypes(): Call<List<String>>

   @POST("order/yakun/{id}")
   fun submittingOrder(
       @Path("id") orderId: Int,
       @Body submittingOrder: SubmittingOrder
   ): Call<SubmittingOrderResponse>

   @POST("ombor/to/{order_id}")
   fun transferWarehouse(
       @Path("order_id") order_Id: Int
   ): Call<ResponseDetail>

    @PUT("order/qayta")
    fun productTransferToRewash(
        @Query("id") productId: Int
    ): Call<String?>

   // Warehouse

    @GET("ombor")
    suspend fun warehouse(
        @Query("page") page: Int
    ): Response<BasePagingModel<WarehouseData>>

   @POST("ombor/tartib/{order_id}")
   fun ordersWarehouse(
       @Path("order_id") order_Id: Int
   ): Call<ResponseDetail>

   //Debtors

   @GET("nasiya/{id}")
   fun debtorCustomer(
       @Path("id") debtId: Int
   ): Call<Debt>

   @GET("nasiya")
   suspend fun marketPagination(
       @Query("page") page: Int,
       @Query("driver") driver: Int,
       @Query("type") type: String
   ): Response<BasePagingModel<MarketPaginationItem>>

   @GET("nasiya/filter")
   fun filterDebtors(): Call<List<FilterDebtorsItem>>

    @POST("nasiya/{nasiya_id}")
    fun requestPayDebt(
        @Path("nasiya_id") debtId: Int,
        @Body payDebt: Paydebt
    ): Call<ResponseDetail>

    @POST("nasiya/kechish/{nasiya_id}")
    fun requestDebtOff(
        @Path("nasiya_id") debtId: Int,
        @Body debtOff: DebtOff
    ): Call<ResponseDetail>

    @POST("nasiya/end/{nasiya_id}")
    fun putDebt(
        @Path("nasiya_id") debtId: Int,
        @Body debtPayDate: DebtPayDate
    ): Call<ResponseDetail>

    //BaseFragment

    @GET("korsatkich")
    fun searchIndicators(
        @Query("from") fromDate: String,
        @Query("to") toDate: String
    ): Call<SearchIndicatorsResult>

    //pagination

    @GET("korsatkich_yuvildi")
    suspend fun getWashedIndicators(
        @Query("from") fromDate: String,
        @Query("to") toDate: String,
        @Query("page") page: Int
    ): Response<WashedIndicator>

    @GET("korsatkich_yuvildi")
    fun getWashedIndicator(
        @Query("from") fromDate: String,
        @Query("to") toDate: String,
        @Query("page") page: Int
    ): Call<WashedIndicator>

    @GET("korsatkich/topshirildi")
    suspend fun getSubmittedIndicators(
        @Query("from") fromDate: String,
        @Query("to") toDate: String,
        @Query("page") page: Int
    ): Response<BasePagingModel<SubmittedIndicatorOrder>>

    @GET("footer_count")
    fun getTableFooter(
        @Query("action") action: String
    ): Call<List<IndicatorProduct>>

    //pagination

    @GET("qayta")
    suspend fun getReceivedRewashIndicators(
        @Query("from") fromDate: String,
        @Query("to") toDate: String,
        @Query("page") page: Int
    ): Response<ReceivedRewashIndicator>

    @GET("qayta")
    fun getReceivedRewashIndicator(
        @Query("from") fromDate: String,
        @Query("to") toDate: String,
        @Query("page") page: Int
    ): Call<ReceivedRewashIndicator>

    @GET("kpi")
    suspend fun getKpiIndicators(
        @Query("from") fromDate: String,
        @Query("to") toDate: String,
        @Query("page") page: Int
    ): Response<KpiIndicator>

    @GET("kpi")
    fun getKpiIndicator(
        @Query("from") fromDate: String,
        @Query("to") toDate: String,
        @Query("page") page: Int
    ): Call<KpiIndicator>

    @GET("maosh")
    fun getReceivedSalaryIndicators(
        @Query("from") fromDate: String,
        @Query("to") toDate: String,
        @Query("page") page: Int
    ): Call<ReceivedSalaryIndicator>

   //Setting

   @GET("profile")
   fun profile(): Call<Setting>

   @PUT("user/{id}/update")
   fun updateUser(
       @Path("id") id :Int,
       @Body updateSetting: UpdateSetting
   ): Call<ResponseSetting>

   //Add customer

    @POST("order/add/{costumer_id}")
    fun createOrderByCustomerId(
        @Path("costumer_id") customerId: Int
    ): Call<createOrder>

    @POST("search_costumer")
    fun searchCustomer(
        @Query("content") content: String
    ): Call<List<SearchCustomerResult>>

   @POST("costumer/add")
   fun addCustomer(
       @Body postCustomer: PostCustomer
   ): Call<addCostumerResponse>

   @GET("manba")
   fun getSources(): Call<List<Sources>>

    @GET("millatlar")
    fun getNationalities(): Call<List<Nationality>>

    @GET("xizmatlar")
    fun getServices(): Call<List<Service>>

    @GET("order/{order_id}/acceptance")
    fun getNewOrderById(
        @Path("order_id") orderId: Int
    ): Call<Order>

    @PUT("order/add_product/{order_id}")
    fun putNewOrder(
        @Path("order_id") orderId: Int,
        @Body putOrder: PutOrder
    ): Call<ResponseDetail>

    @GET("/order/{order_id}/confirmation")
    fun getConfirmOrderById(
        @Path("order_id") orderId: Int
    ): Call<ConfirmOrder?>

    @PUT("/order_tasdiqlash/{orderr_id}")
    fun putConfirmationOrder(
        @Path("orderr_id") orderId: Int,
        @Body putConfirmOrder: PutConfirmOrder
    ): Call<String?>

    // GetSizeFragment

    @GET("order_sizing")
    fun getSizingOrderById(
        @Query("order_id") orderId: Int
    ): Call<SizingOrder>

    @PUT("product_sizing")
    fun putProductSize(
        @Query("id") productId: Int,
        @Body productSize: ProductSize
    ): Call<String?>

    @POST("add_one_product")
    fun addOneProduct(
        @Query("order_id") orderId: Int,
        @Query("xizmat_id") serviceId: Int
    ): Call<String?>
}