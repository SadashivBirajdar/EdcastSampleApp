package com.edcast.myapplication.di

import com.edcast.myapplication.data.netwok.client.PeopleDetailApiClient
import com.edcast.myapplication.data.netwok.client.PeopleListApiClient
import com.edcast.myapplication.domain.GetPeopleDetailUseCase
import com.edcast.myapplication.domain.GetPeopleListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object UseCaseProvider {

    @Provides
    fun getPeopleListUseCase(peopleListApiClient: PeopleListApiClient): GetPeopleListUseCase {
        return GetPeopleListUseCase(peopleListApiClient)
    }

    @Provides
    fun getPeopleDetailUseCase(peopleDetailApiClient: PeopleDetailApiClient): GetPeopleDetailUseCase {
        return GetPeopleDetailUseCase(peopleDetailApiClient)
    }

}