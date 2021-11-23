package com.sports.crichunt.ui.fixture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sports.crichunt.data.models.Fixture
import com.sports.crichunt.data.models.RequestState
import com.sports.crichunt.data.repos.FixtureRepo
import kotlinx.coroutines.launch

class FixtureViewModel(private val fixtureRepo: FixtureRepo) : ViewModel() {
    val fixture: MutableLiveData<Fixture> = MutableLiveData()
    val fixtureInfoRequestState: LiveData<RequestState?> get() = fixtureRepo.fixtureInfoRequestState
    val fixtureLineupsRequestState: LiveData<RequestState?> get() = fixtureRepo.fixtureLineupsRequestState
    val fixtureScoreboardRequestState: LiveData<RequestState?> get() = fixtureRepo.fixtureScoreboardRequestState

    fun initialize(){
        fixtureRepo.fixtureLineupsRequestState.value = null
        fixtureRepo.fixtureScoreboardRequestState.value = null
        fixtureRepo.fixtureInfoRequestState.value = null
    }

    fun getFixtureInfo() {
        viewModelScope.launch {
            fixture.value?.let { fixtureRepo.getFixturesInfo(it.id) }
        }
    }

    fun getFixtureLive(){
        viewModelScope.launch {
            fixture.value?.let { fixtureRepo.getFixtureScoreboard(it.id) }
        }
    }

    fun getLineups() {
        viewModelScope.launch {
            fixture.value?.let { fixtureRepo.getLineups(it.id) }
        }
    }
}