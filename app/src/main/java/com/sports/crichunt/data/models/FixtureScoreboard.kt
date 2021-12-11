package com.sports.crichunt.data.models

data class FixtureScoreboard(
    var batting: ArrayList<Batting>,
    val bowling: ArrayList<Bowling>,
    val scoreboards: ArrayList<Scoreboard>,
    val balls: ArrayList<Ball>,
    val runs: ArrayList<Run>,
    val manofmatch: Player?,
    val lineup: ArrayList<Player>?,
    val localteam: Team,
    val visitorteam: Team,
)
