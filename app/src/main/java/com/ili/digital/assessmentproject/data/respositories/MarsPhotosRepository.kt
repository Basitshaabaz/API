package com.ili.digital.assessmentproject.data.respositories

import com.ili.digital.assessmentproject.data.remote.ApiResult
import com.ili.digital.assessmentproject.data.model.MarsPhotosResponse
import kotlinx.coroutines.flow.Flow

interface MarsPhotosRepository {
    suspend fun fetchCuriosity(page: Int = 1): Flow<ApiResult<MarsPhotosResponse>>
    suspend fun fetchOpportunity(page: Int = 1): Flow<ApiResult<MarsPhotosResponse>>
    suspend fun fetchSpirit(page: Int = 1): Flow<ApiResult<MarsPhotosResponse>>
}