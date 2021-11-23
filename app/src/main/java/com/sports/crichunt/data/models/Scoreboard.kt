package com.sports.crichunt.data.models

data class Scoreboard(
    val id: Int,
    val team_id: Int,
    val type: String,
    val scoreboard: String,
    val wide: String,
    val noball_runs: String,
    val noball_balls: String,
    val bye: String,
    val leg_bye: String,
    val penalty: String,
    val total: String,
    val overs: String,
    val wickets: String,
)
