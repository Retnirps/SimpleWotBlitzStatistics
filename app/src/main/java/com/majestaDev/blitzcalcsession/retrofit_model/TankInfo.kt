package com.majestaDev.blitzcalcsession.retrofit_model

data class TankInfo(
    val data: Map<String, TankInfoValue>
)

data class TankInfoValue(
    val tier: Int,
    val images: ImagesValue,
    val name: String,
    val nation: String
)

data class ImagesValue(
    val preview: String
)
