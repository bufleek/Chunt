package com.sports.crichunt.ui.stages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sports.crichunt.data.models.RequestState
import com.sports.crichunt.data.models.Stage
import com.sports.crichunt.data.repos.StageRepo
import kotlinx.coroutines.launch

class StageViewModel(
    private val stageRepo: StageRepo
) : ViewModel() {
    val stage: MutableLiveData<Stage?> = MutableLiveData()
    val standingsRequestState: LiveData<RequestState> get() = stageRepo.standingsRequestState
    val stageFixturesRequestState: LiveData<RequestState> get() = stageRepo.stageFixturesRequestState

    fun getFixtures(){
        viewModelScope.launch {
            stage.value?.let {
                stageRepo.getStageFixtures(it.id)
            }
        }
    }

    fun getStandings(){
        viewModelScope.launch {
            stage.value?.let {
                stageRepo.getStandings(it.id)
            }
        }
    }
}