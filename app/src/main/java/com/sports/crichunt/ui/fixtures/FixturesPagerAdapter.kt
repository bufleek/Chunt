package com.sports.crichunt.ui.fixtures

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.data.models.Fixture
import com.sports.crichunt.databinding.BindingItemLiveFixture
import com.sports.crichunt.ui.fixtures.live.FixturesLiveViewHolder
import com.sports.crichunt.utils.ViewHolder

class FixturesPagerAdapter(
    private val onFixtureClicked: (Fixture) -> Unit
) : PagingDataAdapter<Fixture, RecyclerView.ViewHolder>(FixtureDiffUtil()) {
    class FixtureDiffUtil : DiffUtil.ItemCallback<Fixture>() {
        override fun areItemsTheSame(oldItem: Fixture, newItem: Fixture): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Fixture, newItem: Fixture): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FixturesLiveViewHolder) {
            getItem(position)?.let {
                holder.bind(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            BindingItemLiveFixture.inflate(LayoutInflater.from(parent.context), parent, false).root
        )
    }
}