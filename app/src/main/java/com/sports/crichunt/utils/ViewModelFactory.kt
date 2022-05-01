package com.sports.crichunt.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sports.crichunt.ui.fixture.FixtureViewModel
import com.sports.crichunt.ui.main.MainViewModel

class ViewModelFactory(private val viewModel: MyViewModels) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(viewModel){
            is MyViewModels.FIXTURE -> FixtureViewModel(viewModel.fixtureRepo) as T
            is MyViewModels.MAIN -> MainViewModel(viewModel.mainRepo) as T
        }
    }
}