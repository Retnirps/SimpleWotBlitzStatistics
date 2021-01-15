package com.example.simplewotblitzstatistics.models

import com.google.gson.JsonElement
import kotlin.math.tan

class TankStatistics(/*_damageDealt: Int, _wins: Int, _battles: Int, _lastBattleTime: Long, _tankId: Long*/tankStatistics: JsonElement) {
//    val damageDealt: Int = _damageDealt
//    val wins: Int = _wins
//    val battles: Int = _battles
//    val lastBattleTime: Long = _lastBattleTime
//    val tankId: Long = _tankId

    var damageDealt: Int = tankStatistics.asJsonObject
        .get("all")
        .asJsonObject.get("damage_dealt").asInt
    var wins: Int = tankStatistics.asJsonObject
        .get("all")
        .asJsonObject.get("wins").asInt
    var battles: Int = tankStatistics.asJsonObject
        .get("all")
        .asJsonObject.get("battles").asInt
    var lastBattleTime: Long = tankStatistics.asJsonObject
        .get("last_battle_time")
        .asLong
    var tankId: Long = tankStatistics.asJsonObject
        .get("tank_id")
        .asLong
}