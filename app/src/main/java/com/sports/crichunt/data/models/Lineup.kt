package com.sports.crichunt.data.models

data class Lineup (
    val team_id: Int,
    val captain: Boolean,
    val wicketKeeper: Boolean,
    val substitution: Boolean
)