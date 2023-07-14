package com.ili.digital.assessmentproject.adapters

import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.ili.digital.assessmentproject.data.model.MarsPhoto
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GridAdapterTest {

    private lateinit var gridAdapter: GridAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var mockContext: Context
    private lateinit var context: Context
    private lateinit var onClickListener: GridAdapter.OnClickListener
    private lateinit var parent: ViewGroup

    @Before
    fun setUp() {
        mockContext = mock(Context::class.java)
        parent = mock(ViewGroup::class.java)
        onClickListener = mock(GridAdapter.OnClickListener::class.java)
        gridAdapter = GridAdapter(onClickListener)
        recyclerView = RecyclerView(mockContext)
        context = ApplicationProvider.getApplicationContext<Context>()

        `when`(parent.context).thenReturn(mockContext)
    }

//    @Test
//    fun testOnBindViewHolder() = runBlocking{
//        val marsPhoto = MarsPhoto(1, img_src = "rover name")
//        val marsPhotoList = listOf(marsPhoto)
//
//        val viewHolder = gridAdapter.onCreateViewHolder(parent, 0)
//        gridAdapter.submitData(PagingData.from(marsPhotoList))
//
//        viewHolder.bind(marsPhoto)
//
//        assertEquals(viewHolder.binding.tvId.text, marsPhoto.id.toString())
//        // Add other assertions based on what you expect to see in the view
//    }

    @Test
    fun testOnBindViewHolder() = runBlockingTest {
        // Prepare test data
        val marsPhoto = MarsPhoto(1, img_src = "rover name")
        val marsPhotoList = listOf(marsPhoto)

        // When data is submitted to the adapter
        gridAdapter.submitData(PagingData.from(marsPhotoList))

        // Then the RecyclerView should display the data
        val viewHolder = gridAdapter.onCreateViewHolder(parent, 0)
        gridAdapter.onBindViewHolder(viewHolder, 0)

        // Assert that viewHolder has been bound with marsPhoto data
        // Assuming that your viewHolder has a method named 'getItem'
        // which returns the MarsPhoto associated with the viewHolder
        assertEquals(gridAdapter.getItemMars(0)?.id, marsPhoto.id)
    }

    @Test
    fun testOnClickListener() {
        val marsPhoto = MarsPhoto(1, img_src =  "camera name")
        val viewHolder = gridAdapter.onCreateViewHolder(parent, 0)
        viewHolder.itemView.performClick()

        verify(onClickListener, times(1)).clickListener(marsPhoto)
    }
}
