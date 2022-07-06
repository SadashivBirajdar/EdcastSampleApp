package com.edcast.myapplication.di

import com.edcast.myapplication.data.netwok.ApiManager
import com.edcast.myapplication.data.netwok.client.PeopleDetailApiClient
import com.edcast.myapplication.data.netwok.client.PeopleListApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiProvider {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return ApiManager.getRetrofit()
    }

    @Provides
    @Singleton
    fun providePeopleListApiClient(retrofit: Retrofit): PeopleListApiClient {
        return PeopleListApiClient(retrofit.create(PeopleListApiClient.PeopleApi::class.java))
    }

    @Provides
    @Singleton
    fun providePeopleDetailApiClient(retrofit: Retrofit): PeopleDetailApiClient {
        return PeopleDetailApiClient(retrofit.create(PeopleDetailApiClient.PeopleDetailApi::class.java))
    }

}