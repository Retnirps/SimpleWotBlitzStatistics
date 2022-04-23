package com.majestaDev.blitzcalcsession.retrofit_model

import com.google.gson.annotations.SerializedName

data class Player(
    val data: List<DataValue>
)

data class DataValue(
    val nickname: String,
    @SerializedName("account_id")
    val accountId: Long
)