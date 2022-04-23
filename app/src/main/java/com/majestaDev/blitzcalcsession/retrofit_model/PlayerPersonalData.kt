package com.majestaDev.blitzcalcsession.retrofit_model

import com.google.gson.annotations.SerializedName

class PlayerPersonalData(
    val data:  Map<String, PlayerPersonalDataValue>
)

class PlayerPersonalDataValue(
    val statistics: StatisticsValue,
    val nickname: String,
    @SerializedName("account_id")
    val accountId: Long
)

class StatisticsValue(
    val all: AllValue,
)

class AllValue(
    @SerializedName("damage_dealt")
    val damageDealt: Long,
    val wins: Int,
    val battles: Int
)
