package com.sports.crichunt.ui.fixture.lineups

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.data.models.Player
import com.sports.crichunt.databinding.BindingPlayerLineup

class LineupsAdapter(
    private val onPlayerClicked: (Player) -> Unit
) : RecyclerView.Adapter<ViewHolderLineups>() {
    private val lineups = ArrayList<Player>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderLineups {
        return ViewHolderLineups(
            BindingPlayerLineup.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ) {
            onPlayerClicked(lineups[it])
        }
    }

    override fun onBindViewHolder(holder: ViewHolderLineups, position: Int) {
        holder.bind(lineups[position])
    }

    override fun getItemCount(): Int = lineups.size

    fun updateData(lineups: ArrayList<Player>) {
        this.lineups.clear()
        this.lineups += lineups
        notifyDataSetChanged()
    }
}