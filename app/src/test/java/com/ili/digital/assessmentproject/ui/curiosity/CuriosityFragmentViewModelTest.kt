package com.ili.digital.assessmentproject.ui.curiosity

import android.app.Application
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ili.digital.assessmentproject.data.datasource.roomDB.AppDatabase
import com.ili.digital.assessmentproject.data.datasource.roomDB.dao.MarsPhotoDao
import com.ili.digital.assessmentproject.data.model.RoverType
import com.ili.digital.assessmentproject.data.remote.ApiInterface
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


class CuriosityFragmentViewModelTest {
    @Mock
    private lateinit var mockDatabase: AppDatabase

    @Mock
    private lateinit var mockApiService: ApiInterface

    @Mock
    private lateinit var mockApplication: Application

    @Mock
    private lateinit var mockDao: MarsPhotoDao

    private lateinit var viewModel: CuriosityFragmentViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(mockDatabase.marsPhotoDao()).thenReturn(mockDao)
        viewModel = CuriosityFragmentViewModel(mockDatabase, mockApiService, mockApplication)
    }

    @Test
    fun `updateFilter updates currentFilter`() = runBlockingTest {
        val filter = "new filter"
        viewModel.updateFilter(filter)

        assertEquals(filter, viewModel.currentFilter.first())
    }

    @Test
    fun `clearFilter clears currentFilter`() = runBlockingTest {
        viewModel.clearFilter()

        assertEquals(null, viewModel.currentFilter.first())
    }

    @Test
    fun `updateRoverType updates roverType`() = runBlockingTest {
        val roverType = RoverType.CURIOSITY
        viewModel.updateRoverType(roverType)

        assertEquals(roverType, viewModel.roverType.first())
    }

    @Test
    fun `getCameraList gets camera list from database`() = runBlockingTest {
        val roverType = RoverType.CURIOSITY
        val cameraList = hashSetOf("Camera1", "Camera2", "Camera3")
        `when`(mockDao.getUniqueCameraNames(roverType.typeName)).thenReturn(listOf(*cameraList.toTypedArray()))

        val result = viewModel.getCameraList(roverType)

        assertEquals(cameraList, result)
    }


}
