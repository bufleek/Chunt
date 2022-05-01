package com.sports.crichunt.ui.fixture.scoreboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.R
import com.sports.crichunt.data.models.Fixture
import com.sports.crichunt.data.models.Inning
import com.sports.crichunt.utils.ViewHolder
import com.sports.crichunt.utils.ViewHolderTableSixCol
import com.sports.crichunt.utils.ViewHolderTitle

class ScoreboardAdapter(
    private val fixture: Fixture
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val data: ArrayList<Ui> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
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

            else -> ViewHolder(layoutInflater.inflate(R.layout.item_empty, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolderTitle) {
            holder.bind((data[position] as Ui.Title).title)
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

    fun updateData(innings: ArrayList<Inning>) {
        data.clear()

        for (inning in innings) {
            val team =
                if (inning.team == fixture.team_a.id) fixture.team_a else fixture.team_b
            data.add(Ui.Title("${team.name} scoreboard ${inning.inning}"))

            val battingTableData: ArrayList<ArrayList<String>> = ArrayList()
            for (batting in inning.batting) {
                battingTableData.add(
                    arrayListOf(
                        batting.batsman,
                        batting.balls,
                        batting.fours,
                        batting.sixes,
                        batting.strike_rate,
                        batting.runs,
                        if (batting.active) "ACTIVE" else "false",
                        batting.out
                    )
                )
            }
            if (battingTableData.isNotEmpty()) {
                addBattingHeader(battingTableData)
                data.add(Ui.Batting(battingTableData))
            }

            val bowlingTableData: ArrayList<ArrayList<String>> = ArrayList()
            for (bowling in inning.bowling) {
                bowlingTableData.add(
                    arrayListOf(
                        bowling.bowler,
                        bowling.overs,
                        bowling.maidens,
                        bowling.runs,
                        bowling.wickets,
                        bowling.no_balls,
                        if (bowling.active) "ACTIVE" else "false"
                    )
                )
            }
            if (bowlingTableData.isNotEmpty()) {
                addBowlingHeader(bowlingTableData)
                data.add(Ui.Bowling(bowlingTableData))
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
            is Ui.Title -> TYPE_TITLE
            is Ui.Bowling -> TYPE_BOWLING
            is Ui.Batting -> TYPE_BATTING
        }
    }

    companion object {
        private const val TYPE_TITLE = 1
        private const val TYPE_BATTING = 2
        private const val TYPE_BOWLING = 3
    }
}