package com.ili.digital.assessmentproject.data.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bumptech.glide.load.HttpException
import com.ili.digital.assessmentproject.data.datasource.roomDB.AppDatabase
import com.ili.digital.assessmentproject.data.model.MarsPhoto
import com.ili.digital.assessmentproject.data.model.RoverType
import com.ili.digital.assessmentproject.data.remote.ApiInterface
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MarsPhotoRemoteMediator(
    private val marsApi: ApiInterface,
    private val database: AppDatabase,
    private val roverType: RoverType
) : RemoteMediator<Int, MarsPhoto>() {

    // Define initial page
    private companion object {
        const val INITIAL_PAGE = 1
    }

    // Maintain a variable for next page.
    private var nextPage: Int = INITIAL_PAGE

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MarsPhoto>
    ): MediatorResult {

        return try {
            when (loadType) {
                LoadType.REFRESH -> refreshData()
                LoadType.PREPEND -> MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> appendData(state)
            }
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun refreshData(): MediatorResult {
        // Reset page number to initial page on refresh.
        nextPage = INITIAL_PAGE
        return appendData(null)
    }

    private suspend fun appendData(state: PagingState<Int, MarsPhoto>?): MediatorResult {
        // End of pagination when last item is null
        if (state != null && state.lastItemOrNull() == null) {
            return MediatorResult.Success(endOfPaginationReached = true)
        }

        val sol = when (roverType) {
            RoverType.CURIOSITY, RoverType.OPPORTUNITY -> 1000
            RoverType.SPIRIT -> 10
        }

        val apiResponse = marsApi.getPhotos(roverType.typeName, sol, page = nextPage)
        if (apiResponse.isSuccessful) {
            val photos = apiResponse.body()?.photos?.map { photo ->
                photo.copy(
                    camera_name = photo.camera.name,
                    rover_name = photo.rover.name
                )
            }

            database.withTransaction {
                if (state == null) {
                    // Clear all data on refresh
                    database.marsPhotoDao().clearAll()
                }
                // Insert new data only if API response is successful
                database.marsPhotoDao().insertAll(photos!!)
            }

            // Increase page number for next load after successful load.
            nextPage++

            return MediatorResult.Success(endOfPaginationReached = photos?.isEmpty()!!)
        }

        return MediatorResult.Error(throwable = Exception(apiResponse.message()))
    }
}

