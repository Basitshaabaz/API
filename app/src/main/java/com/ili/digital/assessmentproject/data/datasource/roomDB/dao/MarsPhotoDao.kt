package com.ili.digital.assessmentproject.data.datasource.roomDB.dao

import androidx.paging.PagingSource
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

    @Query("SELECT * FROM MarsPhoto")
    fun getPagingSource(): PagingSource<Int, MarsPhoto>

    @Query("SELECT * FROM MarsPhoto WHERE rover_name = :rover")
    fun getPagingSource(rover: String): PagingSource<Int, MarsPhoto>

    @Query("SELECT * FROM marsphoto WHERE camera_name LIKE :filter ORDER BY id ASC")
    fun getFilterPagingSource(filter: String): PagingSource<Int, MarsPhoto>

    @Query("SELECT DISTINCT camera_name FROM MarsPhoto WHERE rover_name = :roverName")
    fun getUniqueCameraNames(roverName: String): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: MarsPhoto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<MarsPhoto?>)

    @Delete
    suspend fun delete(contact: MarsPhoto)

    @Query("DELETE FROM MarsPhoto")
    suspend fun clearAll()



}