package com.sports.crichunt.data.models

data class Batting(
    val batsman: String,
    val runs: String,
    val balls: String,
    val fours: String,
    val sixes: String,
    val strike_rate: String,
    val out: String,
    val active: Boolean,
    val team: Int,
    val fixture: Int
)