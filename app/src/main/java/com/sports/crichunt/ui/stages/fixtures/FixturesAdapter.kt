package com.sports.crichunt.ui.stages.fixtures

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.data.models.Fixture
import com.sports.crichunt.databinding.BindingItemFinishedFixture
import com.sports.crichunt.databinding.BindingItemLiveFixture
import com.sports.crichunt.databinding.BindingItemUpcomingFixture
import com.sports.crichunt.ui.fixtures.live.FixturesLiveViewHolder
import com.sports.crichunt.ui.fixtures.results.FixturesFinishedViewHolder
import com.sports.crichunt.ui.fixtures.upcoming.FixturesUpcomingViewHolder

class FixturesAdapter(
    private val isFromStage: Boolean,
    private val onFixtureClicked: (Fixture) -> Unit,
//    private val onStageClicked: (Stage, Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val fixtures: ArrayList<Fixture> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_UPCOMING -> FixturesUpcomingViewHolder(
                BindingItemUpcomingFixture.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                isFromStage,
                onFixtureClicked = {
                    onFixtureClicked(fixtures[it])
                }
            )

            TYPE_FINISHED -> FixturesFinishedViewHolder(
                BindingItemFinishedFixture.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                isFromStage,
                onFixtureClicked = {
                    onFixtureClicked(fixtures[it])
                }
            )

            else -> FixturesLiveViewHolder(
                BindingItemLiveFixture.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                isFromStage,
                onFixtureClicked = {
                    onFixtureClicked(fixtures[it])
                }
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val fixture = fixtures[position]
        when (holder) {
//            is FixturesUpcomingViewHolder -> holder.bind(fixture)
//            is FixturesFinishedViewHolder -> holder.bind(fixture)
//            is FixturesLiveViewHolder -> holder.bind(fixture)
        }
    }

    override fun getItemCount(): Int = fixtures.size

    override fun getItemViewType(position: Int): Int {
        return when (fixtures[position].status) {
//            FixtureStatus.SCHEDULED -> TYPE_UPCOMING
//            FixtureStatus.FINISHED -> TYPE_FINISHED
            else -> TYPE_LIVE
        }
    }

    fun updateData(fixtures: ArrayList<Fixture>) {
        this.fixtures.clear()
        this.fixtures.addAll(fixtures)
        notifyDataSetChanged()
    }

    companion object {
        private const val TYPE_LIVE = 0
        private const val TYPE_UPCOMING = 1
        private const val TYPE_FINISHED = 2
    }
}