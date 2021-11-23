package com.sports.crichunt.ui.stages.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.R
import com.sports.crichunt.data.models.Season
import com.sports.crichunt.data.models.Stage

class StagesAdapter(
    private val onStageClicked: (Stage) -> Unit
) : RecyclerView.Adapter<StageViewHolder>() {
    private val stages: ArrayList<Stage> = ArrayList()
    private val seasons: ArrayList<Season> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_stage, parent, false)
        return StageViewHolder(view) {
            onStageClicked(stages[it])
        }
    }

    override fun onBindViewHolder(holder: StageViewHolder, position: Int) {
        val stage = stages[position]
        val season = seasons.find { it.id == stage.season_id }
        holder.bind(stage, season)
    }

    override fun getItemCount(): Int {
        return stages.size
    }

    fun updateData(stages: ArrayList<Stage>) {
        this.stages.clear()
        this.stages.addAll(stages)
        notifyDataSetChanged()
    }

    fun updateSeasons(seasons: ArrayList<Season>) {
        this.seasons.clear()
        this.seasons += seasons
        notifyDataSetChanged()
    }
}
