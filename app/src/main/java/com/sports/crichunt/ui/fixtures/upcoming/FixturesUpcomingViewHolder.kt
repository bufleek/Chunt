package com.sports.crichunt.ui.fixtures.upcoming

import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.R
import com.sports.crichunt.data.models.Fixture
import com.sports.crichunt.databinding.BindingItemUpcomingFixture
import com.sports.crichunt.utils.getCountDownString
import com.sports.crichunt.utils.getDate
import com.sports.crichunt.utils.stringDateToMillis

class FixturesUpcomingViewHolder(
    private val binding: BindingItemUpcomingFixture,
    private val isAStageMatch: Boolean = false,
    private val onFixtureClicked: (Int) -> Unit,
    private val onStageClicked: (Int, Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    private val root = binding.root.findViewById<View>(R.id.root)
    private val tvStageName = root.findViewById<TextView>(R.id.tv_stage_name)
    private val tvPointsTable = root.findViewById<TextView>(R.id.tv_fixture_points_table)
    private val tvTime = root.findViewById<TextView>(R.id.tv_start_time)
    private var countDown: CountDownTimer? = null

    init {
        tvStageName.setOnClickListener { onStageClicked(bindingAdapterPosition, 0) }
        tvPointsTable.setOnClickListener { onStageClicked(bindingAdapterPosition, 1) }
        root.setOnClickListener {
            onFixtureClicked(bindingAdapterPosition)
        }
    }

    fun bind(fixture: Fixture) {
        binding.fixture = fixture
        if (fixture.stage == null || isAStageMatch) {
            tvStageName.visibility = View.GONE
            tvPointsTable.visibility = View.GONE
        }
        countDown?.cancel()
        val time = stringDateToMillis(fixture.starting_at)?.minus(System.currentTimeMillis())
        countDown = object : CountDownTimer(time ?: 0L, 1000) {
            override fun onTick(p0: Long) {
                tvTime.text = getCountDownString(p0)
            }

            override fun onFinish() {
                getDate(tvTime, fixture.starting_at)
            }

        }
        countDown?.start()
    }
}