package com.ili.digital.assessmentproject.data.datasource.roomDB.typeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ili.digital.assessmentproject.data.model.MarsCamera

class CameraConverter {

    @TypeConverter
    fun cameraToJson(ratchet: MarsCamera): String? {
        val type = object : TypeToken<MarsCamera>() {}.type

        val json = Gson().toJson(ratchet, type)
        return json
    }

    @TypeConverter
    fun jsonToCamera(encryptedJson: String): MarsCamera {
        val gson = Gson()
        val type = object :
            TypeToken<MarsCamera>() {}.type

        return gson.fromJson(encryptedJson, type)
    }
}