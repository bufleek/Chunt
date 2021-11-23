package com.sports.crichunt.data.models

data class Ball(
    val team_id: Int,
    val ball: String,
    val scoreboard: String,
    val batsman: Player,
    val bowler: Player,
    val score: Score,
)
