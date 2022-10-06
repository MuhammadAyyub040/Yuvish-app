package com.example.yuvish.retrofit

import com.example.yuvish.Models.ArrangedSubmit.PaymentTypesItem
import com.example.yuvish.Models.ArrangedSubmit.Submit
import com.example.yuvish.Models.Cleaning.RewashReceipt
import com.example.yuvish.Models.Authorization.UserToken
import com.example.yuvish.Models.BarcodeApi.Order
import com.example.yuvish.Models.BaseIndikatorsIndex.*
import com.example.yuvish.Models.CommonSettings
import com.example.yuvish.Models.DebtorsPackage.ConfirmDebt
import com.example.yuvish.Models.DebtorsPackage.Debtors
import com.example.yuvish.Models.DebtorsAPI.Market.FilterDebtorsItem
import com.example.yuvish.Models.DebtorsAPI.Market.MarketPaginationItem
import com.example.yuvish.Models.DebtorsAPI.Market.Paydebt
import com.example.yuvish.Models.DebtorsPackage.DebtOff
import com.example.yuvish.Models.HolatPaneli.WashingStatusAPI
import com.example.yuvish.Models.HolatPaneli.TransportStatusAPI
import com.example.yuvish.Models.NewOrder.*
import com.example.yuvish.Models.ReadyOrders.Arranging
import com.example.yuvish.Models.ReadyOrders.Autocomplete
import com.example.yuvish.Models.ReadyOrders.ReadyOrdersItem
import com.example.yuvish.Models.ReadyOrders.ViewOrderItem
import com.example.yuvish.Models.Registration.Registration
import com.example.yuvish.Models.Registration.ServiceTypeItem
import com.example.yuvish.Models.Setting.ResponseSetting
import com.example.yuvish.Models.Setting.Setting
import com.example.yuvish.Models.Setting.UpdateSetting
import com.example.yuvish.Models.Tokchalar.ShelfItem
import com.example.yuvish.Models.Warehouse.OrdersOmborItem
import com.example.yuvish.Models.globalSearch.SearchReceiptResult
import com.example.yuvish.Models.searchCustomer.SearchReceipt
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {

    @GET("common_settings")
    fun getCommonSettings(): Call<CommonSettings>

    //LoginPage

    @FormUrlEncoded
    @POST("token")
    fun createUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<UserToken>

    //GlobalSearchFragment requests

    @POST("/search_by_kv_id")
    fun searchReceiptById(
        @Body searchReceipt: SearchReceipt
    ): Call<SearchReceiptResult>

    //Washing

    @GET("orders_by_status/{status}")
   suspend fun cleaning(
        @Path("status") status: String,
        @Query("page") page: Int
    ): Response<List<RewashReceipt>>

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

    @GET("/tokchalar")
    fun getShelfs(): Call<List<ShelfItem>>

   @GET("holat_paneli")
   fun statusBar(): Call<WashingStatusAPI>

   //Arranged

   @GET("giving_orders")
   suspend fun arranged(
       @Query("page") page: Int,
       @Query("driver") driver: Int,
       @Query("type") type: String
   ): Response<List<ReadyOrdersItem>>

   @GET("holat_paneli_for_transport")
   fun transportStatusBar(): Call<TransportStatusAPI>

   @GET("ready_order_filter")
   fun autocomplete(
   ): Call<List<Autocomplete>>

   @GET("order_items/{order_id}")
   fun viewOrder(
       @Path("order_id") order_Id: Int
   ): Call<ViewOrderItem>

   @GET("ready_order_view/{order_id}")
   fun submit(
       @Path("order_id") order_Id: Int
   ): Call<Submit>

   @GET("tolov_turlari")
   fun paymentTypes(): Call<List<PaymentTypesItem>>

   @POST("zakazni_topshirish/{order_id}")
   fun submittingOrder(
       @Path("order_id") orderId: Int,
       @Query("berilgan_summa") givenAmount: Int,
       @Query("tolov_turi") paymentType: String
   ): Call<Int?>

   @PUT("omborga_otkazish/{order_id}")
   fun transferWarehouse(
       @Path("order_id") order_Id: Int
   ): Call<String?>

   @PUT("arranging")
   fun arranging(
       @Body arranging: Arranging
   ): Call<Arranging>

    @GET("orders_ombor")
    suspend fun warehouse(
        @Query("page") page: Int
    ): Response<List<OrdersOmborItem>>

   @POST("orders_take_from_ombor")
   fun ordersWarehouse(
       @Query("order_id") order_Id: Int
   ): Call<String?>

   //Debtors

   @GET("nasiya_get_one")
   fun debtorCustomer(
       @Query("id") id: Int
   ): Call<Debtors>

   @GET("nasiyalar")
   suspend fun marketPagination(
       @Query("page") page: Int,
       @Query("driver") driver: Int,
       @Query("type") type: String
   ): Response<List<MarketPaginationItem>>

   @GET("nasiya_filter")
   fun filterDebtors(): Call<List<FilterDebtorsItem>>

    @POST("nasiya_tolash")
    fun requestPayDebt(
        @Body payDebt: Paydebt
    ): Call<String?>

    @POST("/nasiya_send_off")
    fun requestDebtOff(
        @Body debtOff: DebtOff
    ): Call<String?>

    @PUT("nasiya_put_date")
    fun putDebt(
        @Body confirmDebt: ConfirmDebt
    ): Call<String?>

    //BaseFragment

    @GET("korsatkich_index")
    fun searchIndicators(
        @Query("from_date") fromDate: String,
        @Query("to_date") toDate: String
    ): Call<SearchIndicatorsResult>

    @GET("korsatkich_mahsulot_yuvildi")
    fun getWashedIndicators(
        @Query("from_date") fromDate: String,
        @Query("to_date") toDate: String,
        @Query("page") page: Int
    ): Call<WashedIndicator>

    @GET("korsatkich_mahsulot_topshirildi")
    fun getSubmittedIndicators(
        @Query("from_date") fromDate: String,
        @Query("to_date") toDate: String,
        @Query("page") page: Int
    ): Call<SubmittedIndicator>

    @GET("qayta_yuvishga_ol")
    fun getReceivedRewashIndicators(
        @Query("from_date") fromDate: String,
        @Query("to_date") toDate: String,
        @Query("page") page: Int
    ): Call<ReceivedRewashIndicator>

    @GET("kunlik_maoshlar")
    fun getKpiIndicators(
        @Query("from_date") fromDate: String,
        @Query("to_date") toDate: String,
        @Query("page") page: Int
    ): Call<KpiIndicator>

    @GET("korsatkich_olgan_maoshlarim")
    fun getReceivedSalaryIndicators(
        @Query("from_date") fromDate: String,
        @Query("to_date") toDate: String,
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

    @POST("order/add")
    fun createOrderByCustomerId(
        @Query("costumer_id") customerId: Int
    ): Call<Int?>

    @POST("/search_costumer")
    fun searchCustomer(
        @Query("content") content: String
    ): Call<List<SearchCustomerResult>>

   @POST("costumer/create/operator")
   fun addCustomer(
       @Body postCustomer: PostCustomer
   ): Call<Int?>

   @GET("manbalar")
   fun getSources(): Call<List<Sources>>

    @GET("millatlar")
    fun getNationalities(): Call<List<Nationality>>

    @GET("xizmatlar")
    fun getServices(): Call<List<Service>>

    @GET("order/{order_id}/acceptance")
    fun getNewOrderById(
        @Path("order_id") orderId: Int
    ): Call<Order>

    @PUT("order/{order_id}/acceptance")
    fun putNewOrder(
        @Path("order_id") orderId: Int,
        @Body putOrder: PutOrder
    ): Call<String?>

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