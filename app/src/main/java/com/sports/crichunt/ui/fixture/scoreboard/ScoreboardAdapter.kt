package com.sports.crichunt.ui.fixture.scoreboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.R
import com.sports.crichunt.data.models.Fixture
import com.sports.crichunt.data.models.FixtureScoreboard
import com.sports.crichunt.data.models.Player
import com.sports.crichunt.databinding.BindingManOfTheMatch
import com.sports.crichunt.utils.ViewHolder
import com.sports.crichunt.utils.ViewHolderTableSixCol
import com.sports.crichunt.utils.ViewHolderTitle

class ScoreboardAdapter(
    private val fixture: Fixture,
    private val onManOfTheMatchClicked: (Player) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val data: ArrayList<Ui> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_MAN_OF_THE_MATCH -> ViewHolderManOfTheMatch(
                BindingManOfTheMatch.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            ){
                onManOfTheMatchClicked(it)
            }
            TYPE_TITLE -> ViewHolderTitle(
                layoutInflater.inflate(
                    R.layout.item_title,
                    parent,
                    false
                )
            )
            TYPE_BATTING -> ViewHolderTableSixCol(
                layoutInflater.inflate(
                    R.layout.item_table,
                    parent,
                    false
                )
            )
            TYPE_BOWLING -> ViewHolderTableSixCol(
                layoutInflater.inflate(
                    R.layout.item_table,
                    parent,
                    false
                )
            )
            TYPE_BALLS -> ViewHolderBalls(
                layoutInflater.inflate(
                    R.layout.item_table,
                    parent,
                    false
                )
            )
            else -> ViewHolder(layoutInflater.inflate(R.layout.item_empty, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolderManOfTheMatch) {
            holder.bind((data[position] as Ui.ManOfTheMatch).player)
            return
        }
        if (holder is ViewHolderTitle) {
            holder.bind((data[position] as Ui.Title).title)
            return
        }
        if (holder is ViewHolderBalls) {
            holder.bind((data[position] as Ui.Balls).balls)
            return
        }
        when (val uiData = data[position]) {
            is Ui.Batting -> {
                (holder as ViewHolderTableSixCol).bind(uiData.batting)
            }
            is Ui.Bowling -> {
                (holder as ViewHolderTableSixCol).bind(uiData.bowling)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(fixtureScoreboard: FixtureScoreboard) {
        data.clear()
        fixtureScoreboard.manofmatch?.let { data.add(Ui.ManOfTheMatch(it)) }

        for (scoreboard in fixtureScoreboard.scoreboards.filter { it.type.lowercase() == "total" }.reversed()) {
            val team =
                if (scoreboard.team_id == fixture.localteam?.id) fixture.localteam else fixture.visitorteam
            data.add(Ui.Title(team?.name + " scoreboard"))

            val battingTableData: ArrayList<ArrayList<String>> = ArrayList()
            for (batting in fixtureScoreboard.batting.filter { it.scoreboard == scoreboard.scoreboard }) {
                val player = fixtureScoreboard.lineup?.find { it.id == batting.player_id }
                val bowlingPlayer = fixtureScoreboard.lineup?.find { it.id == batting.bowling_player_id }
                player?.let {
                    battingTableData.add(
                        arrayListOf(
                            it.fullname,
                            batting.ball,
                            batting.four_x,
                            batting.six_x,
                            batting.rate,
                            batting.score,
                            if (batting.active) "ACTIVE" else "false",
                            bowlingPlayer?.fullname ?: ""
                        )
                    )
                }
            }
            if (battingTableData.isNotEmpty()) {
                addBattingHeader(battingTableData)
                data.add(Ui.Batting(battingTableData))
            }

            val bowlingTableData: ArrayList<ArrayList<String>> = ArrayList()
            for (bowling in fixtureScoreboard.bowling.filter { it.scoreboard == scoreboard.scoreboard }) {
                val player = fixtureScoreboard.lineup?.find { it.id == bowling.player_id }
                player?.let {
                    bowlingTableData.add(
                        arrayListOf(
                            it.fullname,
                            bowling.overs,
                            bowling.medians,
                            bowling.runs,
                            bowling.wickets,
                            bowling.rate,
                            if (bowling.active) "ACTIVE" else "false"
                        )
                    )
                }
            }
            if (bowlingTableData.isNotEmpty()) {
                addBowlingHeader(bowlingTableData)
                data.add(Ui.Bowling(bowlingTableData))
            }

            val ballsTableData =
                fixtureScoreboard.balls.filter { it.scoreboard == scoreboard.scoreboard }
            if (ballsTableData.isNotEmpty()) {
                data.add(Ui.Balls(ArrayList(ballsTableData)))
            }
        }
        notifyDataSetChanged()
    }

    private fun addBowlingHeader(bowling: ArrayList<ArrayList<String>>) {
        bowling.add(
            0, arrayListOf(
                "Bowling",
                "O",
                "M",
                "R",
                "W",
                "S.R"
            )
        )
    }

    private fun addBattingHeader(batting: ArrayList<ArrayList<String>>) {
        batting.add(
            0, arrayListOf(
                "Batting",
                "B",
                "4s",
                "6s",
                "S",
                "S.R"
            )
        )
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is Ui.ManOfTheMatch -> TYPE_MAN_OF_THE_MATCH
            is Ui.Title -> TYPE_TITLE
            is Ui.Bowling -> TYPE_BOWLING
            is Ui.Batting -> TYPE_BATTING
            is Ui.Balls -> TYPE_BALLS
            is Ui.Scoreboard -> TYPE_SCOREBOARD
        }
    }

    companion object {
        private const val TYPE_MAN_OF_THE_MATCH = 0
        private const val TYPE_TITLE = 1
        private const val TYPE_BATTING = 2
        private const val TYPE_BOWLING = 3
        private const val TYPE_BALLS = 4
        private const val TYPE_SCOREBOARD = 5
    }
}