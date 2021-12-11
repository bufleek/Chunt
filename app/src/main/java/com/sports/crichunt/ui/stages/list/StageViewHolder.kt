package com.sports.crichunt.ui.stages.list

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sports.crichunt.R
import com.sports.crichunt.data.models.Season
import com.sports.crichunt.data.models.Stage

class StageViewHolder(
    itemView: View,
    private val onSquadClicked: (Int) -> Unit
) :
    RecyclerView.ViewHolder(itemView) {
    private val tvStageName: TextView = itemView.findViewById(R.id.stage_name)
    private val tvLeagueName: TextView = itemView.findViewById(R.id.league_name)
    private val imgLeague: ImageView = itemView.findViewById(R.id.league_img)
    private val tvSeason: TextView = itemView.findViewById(R.id.tv_year)

    init {
        itemView.setOnClickListener { onSquadClicked(absoluteAdapterPosition) }
    }

    fun bind(stage: Stage, season: Season?) {
        tvStageName.text = stage.name
        tvLeagueName.text = stage.league?.name
        if (stage.league == null) {
            imgLeague.visibility = View.GONE
            tvLeagueName.visibility = View.GONE
        }
        Glide.with(tvStageName.context).load(stage.league?.image_path).into(imgLeague)
        if (season == null) {
            tvSeason.text = season?.name
        } else {
            tvSeason.visibility = View.GONE
        }
    }
}