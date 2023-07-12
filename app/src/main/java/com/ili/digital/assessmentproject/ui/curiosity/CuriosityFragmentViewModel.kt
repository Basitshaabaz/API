package com.ili.digital.assessmentproject.ui.curiosity

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.ili.digital.assessmentproject.data.datasource.MarsPhotoRemoteMediator
import com.ili.digital.assessmentproject.data.datasource.roomDB.AppDatabase
import com.ili.digital.assessmentproject.data.model.MarsPhoto
import com.ili.digital.assessmentproject.data.model.RoverType
import com.ili.digital.assessmentproject.data.remote.ApiInterface
import com.ili.digital.assessmentproject.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject


@HiltViewModel
class CuriosityFragmentViewModel @Inject constructor(
    private val database: AppDatabase,
    private val apiService: ApiInterface,
    application: Application
) : BaseViewModel(application) {

    companion object {
        const val PAGE_SIZE = 20
        const val PREFETCH_DISTANCE = 10
    }


    private val _currentFilter = MutableStateFlow<String?>(null)
    val currentFilter: StateFlow<String?> = _currentFilter.asStateFlow()

    val roverType: MutableStateFlow<RoverType?> = MutableStateFlow(null)

    @OptIn(ExperimentalPagingApi::class)
    val marsPhotoFlow: Flow<PagingData<MarsPhoto>> = roverType.flatMapLatest { type ->
        _currentFilter.flatMapLatest { filter ->
            getPhotosFlow(filter, type)
        }
    }.cachedIn(viewModelScope)

    // Returns the flow of Mars photos based on the filter and rover type.
    @OptIn(ExperimentalPagingApi::class)
    private fun getPhotosFlow(filter: String?, type: RoverType?): Flow<PagingData<MarsPhoto>> {
        val config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false)

        return if (filter == null) {
            Pager(
                config = config,
                remoteMediator = MarsPhotoRemoteMediator(apiService, database, type!!),
                pagingSourceFactory = { database.marsPhotoDao().getPagingSource(type.typeName) }
            ).flow
        } else {
            Pager(
                config = config,
                pagingSourceFactory = { database.marsPhotoDao().getFilterPagingSource(filter) }
            ).flow
        }
    }

    // Update the filter for fetching Mars photos.
    fun updateFilter(newFilter: String?) {
        _currentFilter.value = newFilter
    }

    // Clear the filter for fetching Mars photos.
    fun clearFilter() {
        _currentFilter.value = null
    }

    // Update the rover type for fetching Mars photos.
    fun updateRoverType(newType: RoverType?) {
        roverType.value = newType
    }

    // Get the list of unique camera names based on rover type.
    fun getCameraList(rover: RoverType): HashSet<String> {
        return database.marsPhotoDao().getUniqueCameraNames(rover.typeName).toHashSet()
    }

    // Get the PagingSource for Mars photos based on rover type name.
    fun getPagingSource(typeName: String): PagingSource<Int, MarsPhoto> {
        return database.marsPhotoDao().getPagingSource(typeName)
    }
}
