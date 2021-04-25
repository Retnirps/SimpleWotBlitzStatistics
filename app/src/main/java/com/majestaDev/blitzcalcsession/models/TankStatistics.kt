package com.majestaDev.blitzcalcsession.models

import com.google.gson.JsonElement

class TankStatistics(tankStatistics: JsonElement) {
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