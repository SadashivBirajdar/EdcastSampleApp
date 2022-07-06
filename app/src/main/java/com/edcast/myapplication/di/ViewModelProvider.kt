package com.edcast.myapplication.di

import com.edcast.myapplication.domain.GetPeopleDetailUseCase
import com.edcast.myapplication.domain.GetPeopleListUseCase
import com.edcast.myapplication.ui.SampleViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ViewModelProvider {

    @Provides
    fun provideViewModel(
        getPeopleListUseCase: GetPeopleListUseCase,
        getPeopleDetailUseCase: GetPeopleDetailUseCase
    ): SampleViewModel {
        return SampleViewModel(getPeopleListUseCase, getPeopleDetailUseCase)
    }
}