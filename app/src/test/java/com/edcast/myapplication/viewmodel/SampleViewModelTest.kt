package com.edcast.myapplication.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asFlow
import androidx.paging.PagingSource
import com.edcast.myapplication.data.model.People
import com.edcast.myapplication.domain.GetPeopleDetailUseCase
import com.edcast.myapplication.domain.GetPeopleListUseCase
import com.edcast.myapplication.ui.SampleViewModel
import com.edcast.myapplication.ui.list.ListAdapter
import com.jraska.livedata.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SampleViewModelTest {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @MockK
    lateinit var getPeopleDetailUseCase: GetPeopleDetailUseCase

    @MockK
    lateinit var getPeopleListUseCase: GetPeopleListUseCase

    @MockK
    lateinit var listAdapter: ListAdapter

    private lateinit var sampleViewModel: SampleViewModel

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)
        sampleViewModel = SampleViewModel(getPeopleListUseCase, getPeopleDetailUseCase)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_getPeople_success() = runBlockingTest {

        coEvery {
            getPeopleListUseCase.execute()
        } returns flow {
            PagingSource.LoadResult.Page(
                data = mutableListOf(),
                prevKey = null,
                nextKey = 1
            )
        }

        sampleViewModel.getPeoples()

        val job = launch {
            sampleViewModel.pagingData.asFlow().collectLatest {
                listAdapter.submitData(it)
            }
        }
        job.cancel()
    }

    @Test
    fun test_execute_getPeopleDetail_success() {

        coEvery {
            getPeopleDetailUseCase.execute(any())
        } returns flow { emit(GetPeopleDetailUseCase.Result.Success(People())) }


        sampleViewModel.getPeopleDetails()

        sampleViewModel.liveData.test().assertValue {
            it is SampleViewModel.Result.PeopleDetailSuccess
        }

    }
}