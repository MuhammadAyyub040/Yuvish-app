package com.example.yuvish.Models.Authorization

data class UserToken(
    val access_token: String,
    val role: String
)