package com.sports.crichunt.data.models

data class Score(
    val id: Int,
    val runrate: String?,
    val score: String?,
    val overs: String,
    val full_score: String,
    val team: Int,
    val fixture: Int
)
