package com.sports.crichunt.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sports.crichunt.data.models.Player
import com.sports.crichunt.data.models.RequestState
import com.sports.crichunt.data.repos.PlayerRepo
import kotlinx.coroutines.launch

class PlayerViewModel(private val playerRepo: PlayerRepo) : ViewModel() {
    val player = MutableLiveData<Player?>(null)
    val careerRequestState: LiveData<RequestState?> get() = playerRepo.careerRequestState

    fun initialize() {
        playerRepo.careerRequestState.value = null
    }

    fun getCareer() {
        viewModelScope.launch {
            player.value?.let {
                playerRepo.getCareer(it.id)
            }
        }
    }
}