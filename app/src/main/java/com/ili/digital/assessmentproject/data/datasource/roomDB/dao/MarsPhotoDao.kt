package com.ili.digital.assessmentproject.data.datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ili.digital.assessmentproject.data.model.MarsPhoto
import kotlinx.coroutines.flow.Flow

@Dao
interface MarsPhotoDao {

    @get:Query("SELECT * FROM marsphoto")
    val liveFlowMarsPhoto: Flow<List<MarsPhoto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: MarsPhoto)

    @Delete
    suspend fun delete(contact: MarsPhoto)



}