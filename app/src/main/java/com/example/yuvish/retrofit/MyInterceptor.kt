package com.example.yuvish.retrofit

import com.orhanobut.hawk.Hawk
import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = getToken()

        val request = chain.request()
            .newBuilder()
            .apply {
                if (token != null) {
                    addHeader(
                        "Authorization", "Bearer $token"
                    )
                }
            }
            .build()

        return chain.proceed(request)
    }

    private fun getToken(): String?{
        return Hawk.get("token", null)
    }
}