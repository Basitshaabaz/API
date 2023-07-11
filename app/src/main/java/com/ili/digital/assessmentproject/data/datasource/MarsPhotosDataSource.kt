package com.ili.digital.assessmentproject.data.datasource

import com.ili.digital.assessmentproject.data.remote.ApiResult
import com.ili.digital.assessmentproject.data.model.MarsPhotosResponse
import kotlinx.coroutines.flow.Flow

interface MarsPhotosDataSource {
    suspend fun fetchCuriosity(page:Int=1): Flow<ApiResult<MarsPhotosResponse>>
    suspend fun fetchOpportunity(page: Int=1): Flow<ApiResult<MarsPhotosResponse>>
    suspend fun fetchSpirit(page: Int=1): Flow<ApiResult<MarsPhotosResponse>>
}