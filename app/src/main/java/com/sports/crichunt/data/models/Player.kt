package com.sports.crichunt.data.models

data class Player(
    val id: Int,
    val fullname: String,
    val image_path: String,
    val dateofbirth: String?,
    val gender: String,
    val battingstyle: String?,
    val bowlingstyle: String?,
    val lineup: Lineup,
    val position: Position
)
