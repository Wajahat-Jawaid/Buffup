package com.wajahat.buffup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.wajahat.buffdemo.ui.streams.StreamListViewModel
import com.wajahat.buffdemo.ui.streams.StreamListViewState
import com.wajahat.buffup.model.stream.Stream
import io.reactivex.Single
import io.reactivex.Single.error
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
@RunWith(JUnit4::class)
class StreamListViewModelTest {
    @Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private var viewModel: StreamListViewModel? = null
    @Mock
    var observer: Observer<StreamListViewState>? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = StreamListViewModel()
    }

    @Test
    fun testNull() {
        `when`(viewModel!!.loadStreams()).thenReturn(null)
        assertNotNull(viewModel!!.loadStreams())
        assertTrue(viewModel!!.getStreamListViewState().hasObservers())
    }

    @Test
    fun testApiFetchDataSuccess() {
        // Mock API response
        Mockito.doReturn(Single.just(Stream(0, "", "", "", null))).`when`(viewModel!!.loadStreams())
        viewModel!!.loadStreams()
        verify(observer)!!.onChanged(StreamListViewState.LOADING_STATE)
        verify(observer)!!.onChanged(StreamListViewState.SUCCESS_STATE)
    }

    @Test
    fun testApiFetchDataError() {
        Mockito.doReturn(error<Throwable>(Throwable("Api error"))).`when`(viewModel!!.loadStreams())
        viewModel!!.loadStreams()
        verify(observer)!!.onChanged(StreamListViewState.LOADING_STATE)
        verify(observer)!!.onChanged(StreamListViewState.ERROR_STATE)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        viewModel = null
    }
}