package com.sports.crichunt.ui.fixtures.live

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
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.R
import com.sports.crichunt.ui.fixtures.FixturesPagerAdapter
import com.sports.crichunt.ui.main.MainViewModel
import com.sports.crichunt.utils.CricHunt
import com.sports.crichunt.utils.MyViewModels
import com.sports.crichunt.utils.PagerStateAdapter
import com.sports.crichunt.utils.ViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LiveFixturesFragment : Fragment() {
    private var refreshJob: Job? = null
    private lateinit var states: FrameLayout
    private val fixturesAdapter by lazy {
        FixturesPagerAdapter(
            "LIVE",
            {
//            CLICKED
            },
        )
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

    private fun showError(message: String) {
        val errorView = View.inflate(requireContext(), R.layout.item_error, null)
        errorView.findViewById<TextView>(R.id.tv_error).text = message
        errorView.findViewById<Button>(R.id.btn_reload).setOnClickListener {
            fixturesAdapter.refresh()
        }
        states.addView(errorView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        states = view.findViewById(R.id.fl_upcoming_state)
        view.findViewById<RecyclerView>(R.id.rv_upcoming).apply {
            adapter = fixturesAdapter.withLoadStateFooter(footer = PagerStateAdapter {
                fixturesAdapter.retry()
            })
            layoutManager = LinearLayoutManager(requireContext())
        }

        fixturesAdapter.addLoadStateListener {
            when (val loadState = it.refresh) {
                is LoadState.NotLoading -> {
                    states.removeAllViews()
                    if (fixturesAdapter.itemCount == 0) {
                        showError("No live fixture found")
                    }
                }
                LoadState.Loading -> {
                    states.removeAllViews()
                    states.addView(View.inflate(requireContext(), R.layout.item_loading, null))
                }
                is LoadState.Error -> {
                    states.removeAllViews()
                    showError(
                        loadState.error.message ?: "Failed to load fixtures, something went wrong"
                    )
                }
            }
        }

        lifecycleScope.launch {
            mainViewModel.getLiveFixtures().collectLatest {
                fixturesAdapter.submitData(it)
            }
        }
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