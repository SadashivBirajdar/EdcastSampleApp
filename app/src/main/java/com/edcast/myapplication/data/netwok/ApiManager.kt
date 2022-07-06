package com.edcast.myapplication.data.netwok

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Contains methods to fetch retrofit, okhttp instance
 */
object ApiManager {

    private const val BASE_URL = "https://swapi.dev/api/"
    private var okHttp: OkHttpClient? = null

    private var retrofit: Retrofit? = null

    /**
     * returns retrofit instance if already created
     * else create new one and return it
     * */
    fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .client(getOkHttpClient())
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
                )
                .build()
        }
        return retrofit!!
    }

    private fun getOkHttpClient(): OkHttpClient {
        if (okHttp == null)
            okHttp = getOkHttpBuilder().build()
        return okHttp!!
    }

    private fun getOkHttpBuilder(): OkHttpClient.Builder {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .pingInterval(10, TimeUnit.SECONDS)
            .addInterceptor(logging)
    }
}