package com.edcast.myapplication.domain

import com.edcast.myapplication.data.model.People
import com.edcast.myapplication.data.netwok.client.PeopleDetailApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetPeopleDetailUseCase @Inject constructor(private val peopleDetailApiClient: PeopleDetailApiClient) {

    private val apiDateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    private val appDateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault())

    sealed class Result {
        object Loading : Result()
        class Success(val data: People) : Result()
        class Failure(val error: String) : Result()
    }

    fun execute(url: String): Flow<Result> {
        return flow {
            emit(Result.Loading)
            val response = peopleDetailApiClient.getPeopleDetail(url)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                val apiDateFormat = apiDateFormat.parse(body.created!!)
                val createdTimeStamp = appDateFormat.format(apiDateFormat!!)
                val height = try {
                    body.height?.toFloat()?.div(100)
                } catch (e: Exception) {
                    0f
                }
                val people =
                    People(
                        createdTimeStamp,
                        "$height m",
                        "${body.mass} Kg",
                        body.name!!,
                        body.url!!
                    )

                emit(Result.Success(people))
            } else {
                emit(Result.Failure("Get detail api failed"))
            }

        }.catch {
            emit(Result.Failure(it.localizedMessage ?: "Get detail api failed"))
        }
    }
}