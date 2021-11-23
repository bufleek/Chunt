package com.sports.crichunt.ui.fixture.lineups

import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.data.models.Player
import com.sports.crichunt.databinding.BindingPlayerLineup

class ViewHolderLineups(
    private val binding: BindingPlayerLineup,
    private val onPlayerClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener { onPlayerClicked(bindingAdapterPosition) }
    }

    fun bind(player: Player){
        binding.player = player
    }
}