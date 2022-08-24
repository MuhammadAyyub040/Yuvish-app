package com.example.yuvish.retrofit

import com.example.yuvish.Models.Cleaning.RewashReceipt
import com.example.yuvish.Models.Authorization.UserToken
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

   @POST("orders_take_from_ombor")
   fun ordersWarehouse(
       @Query("order_id") orderId: Int
   ): Call<String?>

    @GET("order_cleaning/{order_id}")
    fun ordersCleaning(
        @Path("order_id") orderId: Int
    ): Response<String?>
}