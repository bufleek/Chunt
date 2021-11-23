package com.sports.crichunt.ui.fixture.scoreboard

import android.content.Intent
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
import com.sports.crichunt.data.models.FixtureScoreboard
import com.sports.crichunt.data.models.RequestState
import com.sports.crichunt.ui.fixture.FixtureActivity
import com.sports.crichunt.ui.player.PlayerActivity
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FixtureScoreboardFragment : Fragment() {
    private var refreshJob: Job? = null
    private lateinit var recycler: RecyclerView
    private lateinit var states: FrameLayout

    private val scoreboardAdapter by lazy {
        fixturesViewModel.fixture.value?.let {
            ScoreboardAdapter(
                it
            ){
                startActivity(Intent(requireContext(), PlayerActivity::class.java).apply {
                    putExtra(PlayerActivity.KEY_PLAYER, Gson().toJson(it))
                })
            }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        states = view.findViewById(R.id.state_fixture_scoreboard)
        recycler = view.findViewById(R.id.recycler_fixture_scoreboard)

        recycler.apply {
            adapter = scoreboardAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        fixturesViewModel.fixtureScoreboardRequestState.observe(viewLifecycleOwner, {
            states.removeAllViews()
            when (it) {
                is RequestState.Error -> if (scoreboardAdapter?.itemCount == 0) {
                    states.addView(
                        View.inflate(
                            requireContext(),
                            R.layout.item_error,
                            null
                        ).apply {
                            findViewById<TextView>(R.id.tv_error).text = it.error
                            findViewById<Button>(R.id.btn_reload).setOnClickListener {
                                fixturesViewModel.getFixtureLive()
                            }
                        })
                } else refreshScorecard()

                RequestState.Loading -> states.addView(
                    View.inflate(
                        requireContext(),
                        R.layout.item_loading,
                        null
                    )
                )
                is RequestState.Success<*> -> {
                    val results = it.result
                    if (results is FixtureScoreboard) {
                        scoreboardAdapter?.updateData(results)
                    }
                    if(scoreboardAdapter?.itemCount == 0){
                        states.addView(View.inflate(requireContext(), R.layout.item_empty_state, null).apply {
                            findViewById<TextView>(R.id.tv_empty_message).text = "No scoreboard data available"
                        })
                    }
                    refreshScorecard()
                }
            }
        })

        fixturesViewModel.getFixtureLive()
    }

    private fun refreshScorecard() {
        val fixture = fixturesViewModel.fixture.value!!
        if (fixture.live) {
            refreshJob?.cancel()
            refreshJob = lifecycleScope.launch {
                delay(REFRESH_INTERVAL)
                fixturesViewModel.getFixtureInfo()
            }
            refreshJob?.start()
        }
    }

    companion object {
        private const val REFRESH_INTERVAL = 20000L
    }
}