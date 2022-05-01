package com.sports.crichunt.utils

import com.sports.crichunt.data.repos.FixtureRepo
import com.sports.crichunt.data.repos.MainRepo

sealed class MyViewModels {
    data class MAIN(val mainRepo: MainRepo) : MyViewModels()
    data class FIXTURE(val fixtureRepo: FixtureRepo) : MyViewModels()
}
