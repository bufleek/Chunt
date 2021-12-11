package com.sports.crichunt.data.models

data class Bowling(
    val team_id: Int,
    val active: Boolean,
    val scoreboard: String,
    val player_id: Int,
    val overs: String,
    val medians: String,
    val runs: String,
    val wickets: String,
    val wide: String,
    val noball: String,
    val rate: String,
)
