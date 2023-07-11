package com.ili.digital.assessmentproject.data.datasource.roomDB.typeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ili.digital.assessmentproject.data.model.MarsRover

class RoverConverter {

    @TypeConverter
    fun roverToJson(ratchet: MarsRover): String? {
        val type = object : TypeToken<MarsRover>() {}.type

        val json = Gson().toJson(ratchet, type)
        return json
    }

    @TypeConverter
    fun jsonToCamera(encryptedJson: String): MarsRover {
        val gson = Gson()
        val type = object :
            TypeToken<MarsRover>() {}.type

        return gson.fromJson(encryptedJson, type)
    }
}