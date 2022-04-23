package com.majestaDev.blitzcalcsession.util

import com.majestaDev.blitzcalcsession.api.WargamingApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance(region: String) {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.wotblitz.${getDomain(region)}/wotb/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: WargamingApi by lazy {
        retrofit.create(WargamingApi::class.java)
    }

    private fun getDomain(region: String): String {
        return if (region == "na") {
            "com"
        } else {
            region
        }
    }
}