package com.ili.digital.assessmentproject.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ili.digital.assessmentproject.data.datasource.roomDB.typeConverters.CameraConverter
import com.ili.digital.assessmentproject.data.datasource.roomDB.typeConverters.RoverConverter

@Entity
@TypeConverters(
    CameraConverter::class,
    RoverConverter::class
)
data class MarsPhoto(
    @PrimaryKey
    var id: Int = 0,
    var sol: Int = 0,
    var camera: MarsCamera,
    var img_src: String = "",
    var earth_date: String = "",
    var rover: MarsRover
) {

}