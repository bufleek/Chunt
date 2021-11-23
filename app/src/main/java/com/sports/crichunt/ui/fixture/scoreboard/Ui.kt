package com.sports.crichunt.ui.fixture.scoreboard

import com.sports.crichunt.data.models.Ball
import com.sports.crichunt.data.models.Player

sealed class Ui {
    data class ManOfTheMatch(val player: Player) : Ui()
    data class Scoreboard(val scoreboard: com.sports.crichunt.data.models.Scoreboard) : Ui()
    data class Title(val title: String) : Ui()
    data class Batting(val batting: ArrayList<ArrayList<String>>) : Ui()
    data class Bowling(val bowling: ArrayList<ArrayList<String>>) : Ui()
    data class Balls(val balls: ArrayList<Ball>) : Ui()
}
