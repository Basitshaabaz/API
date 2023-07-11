package com.ili.digital.assessmentproject.data.remote

import com.ili.digital.assessmentproject.data.model.MarsPhotosResponse
import com.ili.digital.assessmentproject.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {


    @GET("rovers/{rover}/photos")
    suspend fun getPhotos(
        @Path("rover") rover: String,
        @Query("sol") sol: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int
    ): Response<MarsPhotosResponse>


}