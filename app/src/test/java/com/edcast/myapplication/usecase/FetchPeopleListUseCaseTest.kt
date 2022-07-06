package com.edcast.myapplication.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.edcast.myapplication.data.model.People
import com.edcast.myapplication.data.model.PeopleListResponse
import com.edcast.myapplication.data.netwok.client.PeopleListApiClient
import com.edcast.myapplication.domain.PeopleListDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals

class FetchPeopleListUseCaseTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @MockK
    lateinit var peopleListApiClient: PeopleListApiClient

    lateinit var peopleListDataSource: PeopleListDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        peopleListDataSource = PeopleListDataSource(peopleListApiClient)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_paging_source_success() = runTest {

        val mockResponse = Response.success(PeopleListResponse())

        coEvery {
            peopleListApiClient.getPeoples(any())
        } returns mockResponse

        val expected = PagingSource.LoadResult.Page<Int, People>(
            data = mutableListOf(),
            prevKey = null,
            nextKey = null
        )

        assertEquals(
            expected,
            peopleListDataSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            ),
        )

    }
}