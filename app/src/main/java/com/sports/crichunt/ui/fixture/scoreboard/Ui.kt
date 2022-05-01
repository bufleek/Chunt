package com.sports.crichunt.ui.fixture.scoreboard

sealed class Ui {
    data class Title(val title: String) : Ui()
    data class Batting(val batting: ArrayList<ArrayList<String>>) : Ui()
    data class Bowling(val bowling: ArrayList<ArrayList<String>>) : Ui()
}
