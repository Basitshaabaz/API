package com.ili.digital.assessmentproject.data.datasource.remote

import com.ili.digital.assessmentproject.data.model.MarsPhoto
import com.ili.digital.assessmentproject.data.model.MarsPhotosResponse
import com.ili.digital.assessmentproject.data.remote.ApiInterface
import retrofit2.Response

class MockApiService : ApiInterface {

    var mockResponse: Response<MarsPhotosResponse>? = null


    fun setData(marsPhotos: List<MarsPhoto>) {
        mockResponse = Response.success(MarsPhotosResponse(marsPhotos))
    }

    override suspend fun getPhotos(
        rover: String,
        sol: Int,
        apiKey: String,
        page: Int
    ): Response<MarsPhotosResponse> {

        return mockResponse ?: Response.error(400, null)
    }

}