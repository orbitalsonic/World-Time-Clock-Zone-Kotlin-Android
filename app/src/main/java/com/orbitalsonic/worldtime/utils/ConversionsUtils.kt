package com.orbitalsonic.worldtime.utils


import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orbitalsonic.worldtime.datamodel.WorldTimeEntity

object ConversionsUtils {

    fun fromObjectToStringZone(mObject: WorldTimeEntity): String {
        return Gson().toJson(mObject)
    }

    fun fromStringToObjectZone(mString: String): WorldTimeEntity {
        val noteType = object : TypeToken<WorldTimeEntity>() {}.type
        return Gson().fromJson(mString, noteType)
    }






}