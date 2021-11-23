package com.sports.crichunt.ui.fixture.scoreboard

import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.data.models.Player
import com.sports.crichunt.databinding.BindingManOfTheMatch

class ViewHolderManOfTheMatch(
    private val binding: BindingManOfTheMatch,
    private val onManOfTheMatchClicked: (Player) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            binding.player?.let {
                onManOfTheMatchClicked(it)
            }
        }
    }

    fun bind(player: Player) {
        binding.player = player
    }
}