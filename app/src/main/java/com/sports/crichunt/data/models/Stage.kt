package com.sports.crichunt.data.models

data class Stage(
    val id: Int,
    val league_id: Int,
    val season_id: Int,
    val name: String,
    val code: String,
    val type: String,
    val standings: Boolean,
    val league: League?
)
