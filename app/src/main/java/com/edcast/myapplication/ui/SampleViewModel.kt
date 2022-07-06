package com.edcast.myapplication.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.edcast.myapplication.data.model.People
import com.edcast.myapplication.domain.GetPeopleDetailUseCase
import com.edcast.myapplication.domain.GetPeopleListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SampleViewModel @Inject constructor(
    private val getPeopleListUseCase: GetPeopleListUseCase,
    private val getPeopleDetailUseCase: GetPeopleDetailUseCase
) :
    ViewModel() {

    private var selectedItem: People? = null
    private val _pagingData = MutableLiveData<PagingData<People>>()
    val pagingData = _pagingData
    private val _liveData = MutableLiveData<Result>()
    val liveData = _liveData

    /**
     * get peoples list using network call
     * */
    fun getPeoples() {
        viewModelScope.launch {
            getPeopleListUseCase.execute().cachedIn(viewModelScope).collectLatest {
                _pagingData.value = it
            }
        }
    }

    fun getPeopleDetails() {
        selectedItem?.url?.let { url ->
            viewModelScope.launch {
                getPeopleDetailUseCase.execute(url).collectLatest { result ->
                    when (result) {
                        GetPeopleDetailUseCase.Result.Loading -> {
                            _liveData.value = Result.Loading
                        }
                        is GetPeopleDetailUseCase.Result.Success -> {
                            _liveData.value = Result.PeopleDetailSuccess(result.data)
                        }
                        is GetPeopleDetailUseCase.Result.Failure -> {
                            _liveData.value = Result.Failure(result.error)
                        }
                    }
                }
            }
        }

    }

    fun setSelectedPeople(people: People) {
        this.selectedItem = people
    }

    sealed class Result {
        object Loading : Result()
        class PeopleDetailSuccess(val data: People) : Result()
        class Failure(val error: String) : Result()
    }
}