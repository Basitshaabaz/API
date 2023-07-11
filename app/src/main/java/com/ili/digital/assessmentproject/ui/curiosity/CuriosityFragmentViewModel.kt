package com.ili.digital.assessmentproject.ui.curiosity

import android.app.Application
import com.ili.digital.assessmentproject.data.model.ScreenState
import com.ili.digital.assessmentproject.data.remote.ApiResult
import com.ili.digital.assessmentproject.data.respositories.MarsPhotosRepository
import com.ili.digital.assessmentproject.data.model.MarsPhotosResponse
import com.ili.digital.assessmentproject.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class CuriosityFragmentViewModel @Inject constructor(
    private val apiRepository: MarsPhotosRepository,
    application: Application
) : BaseViewModel(application) {

    private val _opportunityState = MutableStateFlow(ScreenState())
    val opportunityState: StateFlow<ScreenState> = _opportunityState.asStateFlow()
    private var currentPage = 1


    /**
     * Fetches the initial Curiosity data from the API.
     * @param page The page number to fetch (default is 1).
     */
    suspend fun getCuriosity(page: Int = 1) {
        currentPage = page
        apiRepository.fetchCuriosity(currentPage).collect { result ->
            when (result) {
                is ApiResult.Loading -> {
                    _opportunityState.update {
                        it.copy(isLoading = true)
                    }
                }
                is ApiResult.Success -> {
                    val cameraList = extractCameraList(result.data)
                    _opportunityState.update {
                        it.copy(
                            photoList = result.data?.photos ?: emptyList(),
                            isLoading = false,
                            cameraList = cameraList
                        )
                    }
                }
                is ApiResult.Error -> {
                    _opportunityState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Error Occurred"
                        )
                    }
                }
            }
        }
    }

    /**
     * Loads more Curiosity data by fetching the next page.
     */
    suspend fun loadMoreCuriosity() {
        val nextPage = currentPage + 1
        getCuriosity(nextPage)
    }

    /**
     * Extracts the unique camera names from the MarsPhotosResponse.
     * @param marsPhotosResponse The response containing Mars photos.
     * @return The set of unique camera names.
     */
    private fun extractCameraList(marsPhotosResponse: MarsPhotosResponse?): HashSet<String> {
        val cameraList = mutableSetOf<String>()
        marsPhotosResponse?.photos?.forEach { photo ->
            cameraList.add(photo.camera.name)
        }
        return cameraList.toHashSet()
    }

}