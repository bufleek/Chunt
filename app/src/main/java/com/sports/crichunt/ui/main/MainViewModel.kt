package com.sports.crichunt.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sports.crichunt.data.models.Fixture
import com.sports.crichunt.data.models.RequestState
import com.sports.crichunt.data.models.Season
import com.sports.crichunt.data.repos.MainRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepo: MainRepo) : ViewModel() {
    val stagesRequestState: LiveData<RequestState> get() = mainRepo.stagesRequestState
    val liveFixturesRequestState: LiveData<RequestState> get() = mainRepo.liveFixturesRequestState
    val newsRequestState: LiveData<RequestState> get() = mainRepo.newsRequestState
    val seasons: LiveData<ArrayList<Season>> get() = mainRepo.seasons

    fun getPagedUpcomingFixtures(): Flow<PagingData<Fixture>> {
        return mainRepo.getPagedUpcomingFixtures().cachedIn(viewModelScope)
    }

    fun getPagedFinishedFixtures(): Flow<PagingData<Fixture>> {
        return mainRepo.getPagedFinishedFixtures().cachedIn(viewModelScope)
    }

    fun getStages() {
        viewModelScope.launch {
            mainRepo.getStages()
        }
    }

    fun getLiveFixtures() {
        viewModelScope.launch {
            mainRepo.getLiveFixtures()
        }
    }

    fun getNewsArticles() {
        viewModelScope.launch {
            mainRepo.getNewsArticles()
        }
    }
}