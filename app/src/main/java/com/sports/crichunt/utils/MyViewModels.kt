package com.sports.crichunt.utils

import com.sports.crichunt.data.repos.FixtureRepo
import com.sports.crichunt.data.repos.MainRepo
import com.sports.crichunt.data.repos.PlayerRepo
import com.sports.crichunt.data.repos.StageRepo

sealed class MyViewModels {
    data class MAIN(val mainRepo: MainRepo) : MyViewModels()
    data class FIXTURE(val fixtureRepo: FixtureRepo) : MyViewModels()
    data class STAGE(val stageRepo: StageRepo) : MyViewModels()
    data class PLAYER(val playerRepo: PlayerRepo) : MyViewModels()
}
