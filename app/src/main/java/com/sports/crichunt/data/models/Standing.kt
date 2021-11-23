package com.sports.crichunt.data.models

data class Standing(
    val position: String,
    val points: String,
    val played: String,
    val won: String,
    val lost: String,
    val draw: String,
    val noresult: String,
    val recent_form: ArrayList<String> = ArrayList(),
    val team: Team
)
