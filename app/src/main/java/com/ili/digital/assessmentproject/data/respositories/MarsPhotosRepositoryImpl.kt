package com.ili.digital.assessmentproject.data.respositories

import com.ili.digital.assessmentproject.data.datasource.MarsPhotosDataSource
import com.ili.digital.assessmentproject.data.remote.ApiResult
import com.ili.digital.assessmentproject.data.model.MarsPhotosResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MarsPhotosRepositoryImpl @Inject constructor(private val dataSource: MarsPhotosDataSource) :
    MarsPhotosRepository {

    override suspend fun fetchCuriosity(page: Int): Flow<ApiResult<MarsPhotosResponse>>  {

        return dataSource.fetchCuriosity(page)
    }

    override suspend fun fetchOpportunity(page: Int): Flow<ApiResult<MarsPhotosResponse>> =
        dataSource.fetchOpportunity(page)

    override suspend fun fetchSpirit(page: Int): Flow<ApiResult<MarsPhotosResponse>> =
        dataSource.fetchSpirit(page)
}