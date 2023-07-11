package com.ili.digital.assessmentproject.data.datasource

import com.ili.digital.assessmentproject.data.remote.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import retrofit2.Response

open class BaseApiDataSource {
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Flow<ApiResult<T>> {
        return flow<ApiResult<T>> {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) emit(ApiResult.Success(body))
            } else {
                emit(ApiResult.Error(response.errorBody().toString()))
            }
        }.catch {
            emit(ApiResult.Error(it.message ?: it.toString()))
        }.onStart {
            emit(ApiResult.Loading())
        }.flowOn(Dispatchers.IO)
    }
}