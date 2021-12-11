package com.sports.crichunt.data.models

data class Batting(
    val team_id: Int,
    val active: Boolean,
    val player_id: Int,
    val ball: String,
    val score_id: Int,
    val score: String,
    val four_x: String,
    val six_x: String,
    val batsmanout_id: String?,
    val fow_score: String,
    val fow_balls: String,
    val rate: String,
    val scoreboard: String,
    val bowling_player_id: Int?
)
