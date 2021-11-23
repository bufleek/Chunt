package com.sports.crichunt.ui.fixtures.live

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.sports.crichunt.R
import com.sports.crichunt.data.models.Fixture
import com.sports.crichunt.data.models.RequestState
import com.sports.crichunt.ui.fixture.FixtureActivity
import com.sports.crichunt.ui.main.MainActivity
import com.sports.crichunt.ui.main.MainViewModel
import com.sports.crichunt.ui.stages.fixtures.FixturesAdapter
import com.sports.crichunt.utils.CricHunt
import com.sports.crichunt.utils.MyViewModels
import com.sports.crichunt.utils.ViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LiveFixturesFragment : Fragment() {
    private var refreshJob: Job? = null
    private lateinit var states: FrameLayout
    private val fixturesAdapter by lazy {
        FixturesAdapter(
            false,
            {
                startActivity(Intent(requireContext(), FixtureActivity::class.java).apply {
                    putExtra(FixtureActivity.FIXTURE, Gson().toJson(it))
                })
            },
            { stage, tab ->
                (requireActivity() as MainActivity).launchStageActivity(stage, tab)
            })
    }
    private val mainViewModel by ViewModelLazy(
        MainViewModel::class,
        { requireActivity().viewModelStore },
        { ViewModelFactory(MyViewModels.MAIN(((requireActivity().application) as CricHunt).mainRepo)) }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upcoming, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        states = view.findViewById(R.id.fl_upcoming_state)
        view.findViewById<RecyclerView>(R.id.rv_upcoming).apply {
            adapter = fixturesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        mainViewModel.liveFixturesRequestState.observe(viewLifecycleOwner) {
            states.removeAllViews()
            when (it) {
                is RequestState.Success<*> -> {
                    if (it.result is ArrayList<*>) {
                        fixturesAdapter.updateData(it.result as ArrayList<Fixture>)
                        if (fixturesAdapter.itemCount != 0) {
                            refreshLive()
                        }
                    }
                    if (fixturesAdapter.itemCount == 0) {
                        states.removeAllViews()
                        states.addView(
                            View.inflate(
                                requireContext(),
                                R.layout.item_empty_state,
                                null
                            ).apply {
                                findViewById<TextView>(R.id.tv_empty_message).text =
                                    "There are no live fixtures to show."
                            })
                    }
                }
                RequestState.Loading -> {
                    if (fixturesAdapter.itemCount == 0) {
                        states.addView(
                            View.inflate(
                                requireContext(),
                                R.layout.item_loading,
                                null
                            )
                        )
                    }
                }
                is RequestState.Error -> if (fixturesAdapter.itemCount == 0) {
                    states.addView(
                        View.inflate(requireContext(), R.layout.item_error, null).apply {
                            findViewById<TextView>(R.id.tv_error).text = it.error
                            findViewById<Button>(R.id.btn_reload).setOnClickListener { mainViewModel.getLiveFixtures() }
                        })
                } else {
                    refreshLive()
                }
            }

        }

        mainViewModel.getLiveFixtures()
    }

    private fun refreshLive() {
        refreshJob?.cancel()
        refreshJob = lifecycleScope.launch {
            delay(REFRESH_INTERVAL)
            mainViewModel.getLiveFixtures()
        }
        refreshJob?.start()
    }

    companion object {
        private const val REFRESH_INTERVAL = 20000L
    }
}