package com.sports.crichunt.data.models

data class League(
    val id: Int,
    val season_id: Int,
    val country_id: Int,
    val name: String,
    val code: String,
    val image_path: String?
)
