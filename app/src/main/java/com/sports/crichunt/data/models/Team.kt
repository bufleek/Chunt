package com.sports.crichunt.data.models

data class Team(
    val id: Int,
    val name: String,
    val logo_url: String,
    val score: Score?
)
