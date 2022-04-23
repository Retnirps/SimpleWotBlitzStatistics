package com.majestaDev.blitzcalcsession.model

data class TankStatisticDto(
    val tankId: Long,
    val damageDealt: Long,
    val battles: Int,
    val percentageOfWins: Int,
)