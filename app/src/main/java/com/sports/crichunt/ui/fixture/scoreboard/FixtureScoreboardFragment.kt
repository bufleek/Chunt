package com.sports.crichunt.ui.fixture.scoreboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.R
import com.sports.crichunt.ui.fixture.FixtureActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FixtureScoreboardFragment : Fragment() {
    private var refreshJob: Job? = null
    private lateinit var recycler: RecyclerView
    private lateinit var states: FrameLayout

    private val scoreboardAdapter by lazy {
        fixturesViewModel.fixture?.let {
            ScoreboardAdapter(
                it
            )
        }
    }
    private val fixturesViewModel by lazy {
        (requireActivity() as FixtureActivity).fixtureViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fixture_scoreboard, container, false)
    }

    private fun showError(error: String) {
        states.addView(
            View.inflate(
                requireContext(),
                R.layout.item_error,
                null
            ).apply {
                findViewById<TextView>(R.id.tv_error).text = error
                findViewById<Button>(R.id.btn_reload).setOnClickListener {
                    fixturesViewModel.getScorecard()
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        states = view.findViewById(R.id.state_fixture_scoreboard)
        recycler = view.findViewById(R.id.recycler_fixture_scoreboard)

        recycler.apply {
            adapter = scoreboardAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        fixturesViewModel.fixtureScorecard.observe(viewLifecycleOwner) {
            states.removeAllViews()
            if (it.isEmpty() && !fixturesViewModel.scorecardLoading.value!! && fixturesViewModel.scorecardError.value == null) {
                showError("No scorecard to display")
            } else {
                scoreboardAdapter?.updateData(it)
            }
            refreshScorecard()
        }
        fixturesViewModel.scorecardLoading.observe(viewLifecycleOwner) {
            states.removeAllViews()
            if (it) {
                states.addView(
                    View.inflate(
                        requireContext(),
                        R.layout.item_loading,
                        null
                    )
                )
            }
        }

        fixturesViewModel.getScorecard()
    }

    private fun refreshScorecard() {
        val fixture = fixturesViewModel.fixture
        if (fixture?.live == true) {
            refreshJob?.cancel()
            refreshJob = lifecycleScope.launch {
                delay(REFRESH_INTERVAL)
                fixturesViewModel.getScorecard()
            }
            refreshJob?.start()
        }
    }

    companion object {
        private const val REFRESH_INTERVAL = 20000L
    }
}