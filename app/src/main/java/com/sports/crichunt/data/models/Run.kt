package com.sports.crichunt.data.models

data class Run(
    val id: Int,
    val team_id: Int,
    val inning: String,
    val score: String,
    val wickets: String,
    val overs: String,
    val pp1: String?,
    val pp2: String?,
    val pp3: String?,
)
