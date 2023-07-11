package com.ili.digital.assessmentproject.data.datasource

import com.ili.digital.assessmentproject.data.remote.ApiResult
import com.ili.digital.assessmentproject.data.model.RoverType
import com.ili.digital.assessmentproject.data.remote.ApiInterface
import com.ili.digital.assessmentproject.data.model.MarsPhotosResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

open class MarsPhotosDataSourceImpl @Inject constructor(private val apiService: ApiInterface) :
    MarsPhotosDataSource, BaseApiDataSource() {


    override suspend fun fetchCuriosity(page: Int): Flow<ApiResult<MarsPhotosResponse>> =
        getResult { apiService.getPhotos(RoverType.CURIOSITY.typeName, 2000, page = page) }

    override suspend fun fetchOpportunity(page: Int): Flow<ApiResult<MarsPhotosResponse>> =
        getResult { apiService.getPhotos(RoverType.OPPORTUNITY.typeName, 2000, page = page) }


    override suspend fun fetchSpirit(page: Int): Flow<ApiResult<MarsPhotosResponse>> =
        getResult { apiService.getPhotos(RoverType.SPIRIT.typeName, 1, page = page) }

}