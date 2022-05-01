package com.sports.crichunt.data.models

data class Inning(
    val id: Int,
    val inning: String,
    val fall_of_wickets: String,
    val team: Int,
    val fixture: Int,
    val scores: ArrayList<Score>,
    val batting: ArrayList<Batting>,
    val bowling: ArrayList<Bowling>
)
