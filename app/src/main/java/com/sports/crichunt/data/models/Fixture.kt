package com.sports.crichunt.data.models

data class Fixture(
    val id: Int,
    val series: Series,
    val date: String?,
    val venue: Venue,
    val status: String,
    val status_note: String,
    val featured: Boolean,
    val team_a: Team,
    val team_b: Team,
    var live: Boolean = false
)
