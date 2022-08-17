package com.example.yuvish.retrofit

import com.example.yuvish.Models.UserToken
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {

    @FormUrlEncoded
    @POST("token")
    fun createUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<UserToken>

}