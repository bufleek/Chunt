package com.sports.crichunt.data.models

data class FixtureInfo(
    val stage: Stage,
    val venue : Venue?,
    val referee: Referee?,
    val firstumpire: Umpire?,
    val secondumpire: Umpire?,
    val tvumpire: Umpire?,
    val scoreboards: ArrayList<Scoreboard>,
    val localteam: Team,
    val visitorteam: Team
)