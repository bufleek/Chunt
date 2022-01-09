package com.sports.crichunt.ui.fixtures

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.data.models.Fixture
import com.sports.crichunt.data.models.Stage
import com.sports.crichunt.databinding.BindingItemFinishedFixture
import com.sports.crichunt.databinding.BindingItemUpcomingFixture
import com.sports.crichunt.ui.fixtures.results.FixturesFinishedViewHolder
import com.sports.crichunt.ui.fixtures.upcoming.FixturesUpcomingViewHolder

class FixturesPagerAdapter(
    private val onFixtureClicked: (Fixture) -> Unit,
    private val onStageClicked: (Stage, Int) -> Unit
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
        val fixture = getItem(position)
        fixture?.let {
            when (holder) {
                is FixturesUpcomingViewHolder -> holder.bind(it)
                else -> (holder as FixturesFinishedViewHolder).bind(it)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_UPCOMING -> {
                FixturesUpcomingViewHolder(
                    BindingItemUpcomingFixture.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    isAStageMatch = false,
                    onFixtureClicked = {
                        getItem(it)?.let { fixture ->
                            onFixtureClicked(fixture)
                        }
                    },
                    onStageClicked = { position, tab ->
                        getItem(position).let { fixture ->
                            fixture?.stage?.let { stage ->
                                onStageClicked(stage, tab)
                            }
                        }
                    })
            }

            else -> {
                FixturesFinishedViewHolder(
                    BindingItemFinishedFixture.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    isAStageMatch = false,
                    onFixtureClicked = {
                        getItem(it)?.let { fixture ->
                            onFixtureClicked(fixture)
                        }
                    },
                    onStageClicked = { position, tab ->
                        getItem(position).let { fixture ->
                            fixture?.stage?.let { stage ->
                                onStageClicked(stage, tab)
                            }
                        }
                    })
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position)?.status.equals("ns", true)) {
            return TYPE_UPCOMING
        }
        return TYPE_FINISHED
    }

    companion object {
        const val TYPE_UPCOMING = 100
        const val TYPE_FINISHED = 101
    }
}