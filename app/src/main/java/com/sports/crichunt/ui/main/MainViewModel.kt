package com.sports.crichunt.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sports.crichunt.data.models.RequestState
import com.sports.crichunt.data.models.Season
import com.sports.crichunt.data.repos.MainRepo
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepo: MainRepo) : ViewModel() {
    val stagesRequestState: LiveData<RequestState> get() = mainRepo.stagesRequestState
    val liveFixturesRequestState: LiveData<RequestState> get() = mainRepo.liveFixturesRequestState
    val upcomingFixturesRequestState: LiveData<RequestState> get() = mainRepo.upcomingFixturesRequestState
    val finishedFixturesRequestState: LiveData<RequestState> get() = mainRepo.finishedFixturesRequestState
    val newsRequestState: LiveData<RequestState> get() = mainRepo.newsRequestState
    val seasons: LiveData<ArrayList<Season>> get() = mainRepo.seasons

    fun getUpcomingFixtures() {
        return mainRepo.getUpcomingFixtures()
    }

    fun getFinishedFixtures() {
        mainRepo.getFinishedFixtures()
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

    fun getNewsArticles(){
        viewModelScope.launch{
            mainRepo.getNewsArticles()
        }
    }
}