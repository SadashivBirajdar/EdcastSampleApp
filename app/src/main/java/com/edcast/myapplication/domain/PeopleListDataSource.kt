package com.edcast.myapplication.domain

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.edcast.myapplication.data.model.People
import com.edcast.myapplication.data.model.PeopleListResponse
import com.edcast.myapplication.data.netwok.client.PeopleListApiClient
import java.util.*

class PeopleListDataSource(
    private val peopleListApiClient: PeopleListApiClient
) :
    PagingSource<Int, People>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, People> {
        return try {
            val pageNumber = params.key ?: 1
            val response = peopleListApiClient.getPeoples(pageNumber)
            val peopleListResponse = response.body()
            if (response.isSuccessful && response.body() != null) {
                val data = mapToOrderList(peopleListResponse!!)
                val next = peopleListResponse.next?.let {
                    Uri.parse(it).getQueryParameter("page")?.toInt()
                }
                LoadResult.Page(
                    data = data,
                    prevKey = null,
                    nextKey = next
                )
            } else {
                val throwable = Exception("Get peoples api failed")
                LoadResult.Error(throwable)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    /**
     * map response to UI model
     * */
    private fun mapToOrderList(peopleListResponse: PeopleListResponse): MutableList<People> {
        val orderList = mutableListOf<People>()
        peopleListResponse.results?.forEach { result ->
            val people =
                People(
                    result.created,
                    result.height,
                    result.mass,
                    result.name,
                    result.url
                )
            orderList.add(people)
        }
        return orderList
    }

    override fun getRefreshKey(state: PagingState<Int, People>): Int = 1

}