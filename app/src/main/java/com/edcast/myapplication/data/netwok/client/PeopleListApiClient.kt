package com.edcast.myapplication.data.netwok.client

import com.edcast.myapplication.data.model.PeopleListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

/**
 * Retrofit service for api to get star wars character list
 * */
class PeopleListApiClient @Inject constructor(private val peopleApi: PeopleApi) {

    interface PeopleApi {
        @GET("people")
        suspend fun getPeoples(@Query("page") pageNumber: Int?): Response<PeopleListResponse>
    }

    suspend fun getPeoples(pageNumber: Int?):
            Response<PeopleListResponse> {
        return peopleApi.getPeoples(pageNumber)
    }
}