package com.sports.crichunt.ui.fixtures.live

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.R
import com.sports.crichunt.data.models.Fixture
import com.sports.crichunt.databinding.BindingItemLiveFixture

class FixturesLiveViewHolder(
    private val binding: BindingItemLiveFixture,
    private val isAStageMatch: Boolean = false,
    private val onFixtureClicked: (Int) -> Unit,
    private val onStageClicked: (Int, Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    private val tvStageName = itemView.findViewById<TextView>(R.id.tv_stage_name)
    private val tvPointsTable = itemView.findViewById<TextView>(R.id.tv_fixture_points_table)
    private val tvHomeScore: TextView = itemView.findViewById(R.id.tv_home_score)
    private val tvAwayScore: TextView = itemView.findViewById(R.id.tv_away_score)
    private val tvHomeOvers: TextView = itemView.findViewById(R.id.tv_home_overs)
    private val tvAwayOvers: TextView = itemView.findViewById(R.id.tv_away_overs)

    init {
        itemView.setOnClickListener {
            onFixtureClicked(bindingAdapterPosition)
        }
        tvStageName.setOnClickListener { onStageClicked(bindingAdapterPosition, 0) }
        tvPointsTable.setOnClickListener { onStageClicked(bindingAdapterPosition, 1) }
    }

    fun bind(fixture: Fixture) {
        binding.fixture = fixture
        if (fixture.stage == null || isAStageMatch) {
            tvStageName.visibility = View.GONE
            tvPointsTable.visibility = View.GONE
        }
        if (!fixture.scoreboards.isNullOrEmpty()) {
            val totalsBoard = fixture.scoreboards.filter { it.type.equals("total", true) }
            val homeBoard = totalsBoard.find { it.team_id == fixture.localteam?.id }
            val awayBoard = totalsBoard.find { it.team_id == fixture.visitorteam?.id }
            homeBoard?.let {
                tvHomeScore.text = "${it.total}/${it.wickets}"
                if (it.overs.isNotBlank()) {
                    tvHomeOvers.text = it.overs + " Ov"
                }
            }
            awayBoard?.let {
                tvAwayScore.text = "${it.total}/${it.wickets}"
                if (it.overs.isNotBlank()) {
                    tvAwayOvers.text = it.overs + " Ov"
                }
            }
        }
    }
}