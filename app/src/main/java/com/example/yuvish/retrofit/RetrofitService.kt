package com.example.yuvish.retrofit

import com.example.yuvish.Models.ArrangedSubmit.Submit
import com.example.yuvish.Models.Cleaning.RewashReceipt
import com.example.yuvish.Models.Authorization.UserToken
import com.example.yuvish.Models.ReadyOrders.Autocomplete
import com.example.yuvish.Models.ReadyOrders.ReadyOrdersItem
import com.example.yuvish.Models.Setting.ResponseSetting
import com.example.yuvish.Models.Setting.Setting
import com.example.yuvish.Models.Setting.UpdateSetting
import com.example.yuvish.Models.Warehouse.OrdersOmborItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {

    @FormUrlEncoded
    @POST("token")
    fun createUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<UserToken>

    @GET("orders_by_status/{status}")
   suspend fun cleaning(
        @Path("status") status: String,
        @Query("page") page: Int
    ): Response<List<RewashReceipt>>

   @GET("orders_ombor")
   suspend fun warehouse(
       @Query("page") page: Int
   ): Response<List<OrdersOmborItem>>

   @GET("giving_orders")
   suspend fun arranged(
       @Query("page") page: Int,
       @Query("driver") driver: Int,
       @Query("type") type: String
   ): Response<List<ReadyOrdersItem>>

   @GET("ready_order_filter")
   fun autocomplete(
   ): Call<List<Autocomplete>>

   @GET("ready_order_view/{order_id}")
   fun submit(
       @Path("order_id") order_Id: Int
   ): Call<Submit>

   @POST("orders_take_from_ombor")
   fun ordersWarehouse(
       @Query("order_id") order_Id: Int
   ): Call<String?>

   @GET("profile")
   fun profile(
   ): Call<Setting>

   @PUT("user/{id}/update")
   fun updateUser(
       @Path("id") id :Int,
       @Body updateSetting: UpdateSetting
   ): Call<ResponseSetting>
}