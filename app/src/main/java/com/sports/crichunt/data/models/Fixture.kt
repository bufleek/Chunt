package com.sports.crichunt.data.models

data class Fixture(
    val id: Int,
    val league_id: Int,
    val stage_id: Int,
    val round: String?,
    val localteam: Team?,
    val visitorteam: Team?,
    val starting_at: String,
    val type: String,
    val status: String,
    val live: Boolean,
    val note: String,
    val stage: Stage?,
    val venue: Venue?,
    val scoreboards: ArrayList<Scoreboard> = ArrayList()
)
