package com.sports.crichunt.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sports.crichunt.data.models.Fixture
import com.sports.crichunt.data.models.RequestState
import com.sports.crichunt.data.repos.MainRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepo: MainRepo) : ViewModel() {
    val newsRequestState: LiveData<RequestState> get() = mainRepo.newsRequestState

    fun getScheduledFixtures(): Flow<PagingData<Fixture>> {
        return mainRepo.getScheduledFixtures().cachedIn(viewModelScope)
    }

    fun getFixturesResults(): Flow<PagingData<Fixture>> {
        return mainRepo.getFixturesResults().cachedIn(viewModelScope)
    }

    fun getLiveFixtures(): Flow<PagingData<Fixture>> {
        return mainRepo.getLiveFixtures().cachedIn(viewModelScope)
    }

    fun getNewsArticles(){
        viewModelScope.launch {
            mainRepo.getNewsArticles()
        }
    }
}