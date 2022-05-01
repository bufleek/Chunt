package com.sports.crichunt.ui.fixture

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sports.crichunt.data.models.Fixture
import com.sports.crichunt.data.models.Inning
import com.sports.crichunt.data.repos.FixtureRepo
import kotlinx.coroutines.launch

class FixtureViewModel(private val fixtureRepo: FixtureRepo) : ViewModel() {
    val fixtureScorecard: LiveData<ArrayList<Inning>> get() = fixtureRepo._fixtureScorecard
    val scorecardLoading: LiveData<Boolean> get() = fixtureRepo._scorecardLoading
    val scorecardError: LiveData<String?> get() = fixtureRepo._scorecardError
    var fixture: Fixture? = null

    fun getScorecard() {
        fixture?.let {
            viewModelScope.launch {
                fixtureRepo.getScorecard(it.id)
            }
        }
    }

    fun initialize() {
        fixtureRepo._fixtureScorecard.value = ArrayList()
    }
}