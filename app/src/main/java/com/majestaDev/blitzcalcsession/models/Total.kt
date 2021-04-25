package com.majestaDev.blitzcalcsession.models

import com.google.gson.JsonElement

class Total(total: JsonElement) {
    var battles: Int = total.asJsonObject
        .get("statistics")
        .asJsonObject.get("all")
        .asJsonObject.get("battles").asInt
    var wins: Int = total.asJsonObject
        .get("statistics")
        .asJsonObject.get("all")
        .asJsonObject.get("wins").asInt
    var damageDealt: Int = total.asJsonObject
        .get("statistics")
        .asJsonObject.get("all")
        .asJsonObject.get("damage_dealt").asInt
}