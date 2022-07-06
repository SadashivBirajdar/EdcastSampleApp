package com.edcast.myapplication.usecase

import com.edcast.myapplication.data.model.PeopleDetailResponse
import com.edcast.myapplication.data.netwok.client.PeopleDetailApiClient
import com.edcast.myapplication.domain.GetPeopleDetailUseCase
import com.google.gson.Gson
import dev.olog.flow.test.observer.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class GetPeopleDetailUseCaseTest {

    @MockK
    lateinit var peopleDetailApiClient: PeopleDetailApiClient

    private lateinit var getPeopleDetailUseCase: GetPeopleDetailUseCase
    private val url = "https://swapi.dev/api/people/1"

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getPeopleDetailUseCase = GetPeopleDetailUseCase(peopleDetailApiClient)
    }

    @Test
    fun test_execute_apiCall_success() {

        val mockResponse = Response.success(PeopleDetailResponse())

        coEvery { peopleDetailApiClient.getPeopleDetail(any()) } returns mockResponse

        val flow = getPeopleDetailUseCase.execute(url)

        runBlocking {
            flow.test(this) {
                assertValue {
                    it is GetPeopleDetailUseCase.Result.Success
                }
            }
        }

    }

    @Test
    fun test_execute_api_failure() {

        val errorJson = "{  \"statusCode\": 0\n }"
        val result = Gson().toJson(errorJson)
        val errorBody = result.toResponseBody("application/json".toMediaTypeOrNull())
        val errorResponse = okhttp3.Response.Builder()
            .code(404)
            .message("Not found")
            .protocol(Protocol.HTTP_1_1)
            .request(Request.Builder().url(url).build())
            .build()

        val mockResponse = Response.error<PeopleDetailResponse>(errorBody, errorResponse)

        coEvery { peopleDetailApiClient.getPeopleDetail(any()) } returns mockResponse

        val flow = getPeopleDetailUseCase.execute(url)
        runBlocking {
            flow.test(this) {
                assertValue {
                    it is GetPeopleDetailUseCase.Result.Failure
                }
            }
        }

    }
}