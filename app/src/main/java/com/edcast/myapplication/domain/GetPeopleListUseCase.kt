package com.edcast.myapplication.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.edcast.myapplication.data.model.People
import com.edcast.myapplication.data.model.PeopleListResponse
import com.edcast.myapplication.data.netwok.client.PeopleListApiClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPeopleListUseCase @Inject constructor(private val peopleListApiClient: PeopleListApiClient) {

    fun execute(): Flow<PagingData<People>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 1),
            pagingSourceFactory = { PeopleListDataSource(peopleListApiClient) }
        ).flow
    }
}