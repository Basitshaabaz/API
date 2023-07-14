package com.ili.digital.assessmentproject.data.datasource



import android.app.Application
import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ili.digital.assessmentproject.data.datasource.remote.MockApiService
import com.ili.digital.assessmentproject.data.datasource.roomDB.AppDatabase
import com.ili.digital.assessmentproject.data.model.MarsPhoto
import com.ili.digital.assessmentproject.data.model.RoverType
import com.ili.digital.assessmentproject.data.remote.ApiInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@OptIn(ExperimentalPagingApi::class)
class MarsPhotoRemoteMediatorTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var apiInterface: MockApiService
    private lateinit var remoteMediator: com.ili.digital.assessmentproject.data.datasource.MarsPhotoRemoteMediator
    private lateinit var context: Context

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        apiInterface=MockApiService()
        appDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),AppDatabase::class.java).allowMainThreadQueries().build()
        context = ApplicationProvider.getApplicationContext()
        appDatabase = AppDatabase.getAppDatabase(context)

        // Initialize your real API interface here...
        // If the API interface requires a Retrofit instance, initialize it as well

        remoteMediator = MarsPhotoRemoteMediator(apiInterface, appDatabase, RoverType.CURIOSITY)
    }


    @Test
    fun testRefreshLoad() = runBlockingTest {
        val result = remoteMediator.load(LoadType.REFRESH, PagingState(emptyList(), null, PagingConfig(1), 0))
        // Check if the load operation failed


        assert(result is RemoteMediator.MediatorResult.Success)
    }

    @After
    fun tearDown() {
        appDatabase.close()
        testDispatcher.cleanupTestCoroutines()
    }
}
