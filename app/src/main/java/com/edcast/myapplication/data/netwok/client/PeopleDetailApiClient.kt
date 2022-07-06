package com.edcast.myapplication.data.netwok.client

import com.edcast.myapplication.data.model.PeopleDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url
import javax.inject.Inject

/**
 * Retrofit service for api to get details of star wars character
 * */
class PeopleDetailApiClient @Inject constructor(private val peopleDetailApi: PeopleDetailApi) {

    interface PeopleDetailApi {
        @GET
        suspend fun getPeopleDetail(@Url url: String): Response<PeopleDetailResponse>
    }

    suspend fun getPeopleDetail(
        url: String
    ):
            Response<PeopleDetailResponse> {
        return peopleDetailApi.getPeopleDetail(url)
    }
}