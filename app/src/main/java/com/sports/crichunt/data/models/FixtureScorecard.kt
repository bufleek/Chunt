package com.sports.crichunt.data.models

data class FixtureScorecard(
    val scoreboards: ArrayList<Scoreboard>,
    val balls: ArrayList<Ball>,
    val runs: ArrayList<Run>,
    val manofmatch: Player
)
