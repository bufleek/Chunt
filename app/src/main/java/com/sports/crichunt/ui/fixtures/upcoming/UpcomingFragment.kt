package com.sports.crichunt.ui.fixtures.upcoming

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
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.sports.crichunt.R
import com.sports.crichunt.ui.fixture.FixtureActivity
import com.sports.crichunt.ui.fixtures.FixturesPagerAdapter
import com.sports.crichunt.ui.main.MainActivity
import com.sports.crichunt.utils.PagerStateAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UpcomingFragment : Fragment() {
    private lateinit var states: FrameLayout
    private val fixturesPagingAdapter by lazy {
        FixturesPagerAdapter(
            "SCHEDULED",
            {
                startActivity(Intent(requireContext(), FixtureActivity::class.java).apply {
                    putExtra(FixtureActivity.FIXTURE, Gson().toJson(it))
                })
            })
    }
    private val mainViewModel by lazy {
        (requireActivity() as MainActivity).mainViewModel
    }

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
            adapter = fixturesPagingAdapter.withLoadStateFooter(footer = PagerStateAdapter {
                fixturesPagingAdapter.refresh()
            })
            layoutManager = LinearLayoutManager(requireContext())
        }

        fixturesPagingAdapter.addLoadStateListener {
            states.removeAllViews()
            when (val loadState = it.refresh) {
                is LoadState.NotLoading -> {
                    if ((it.append !is LoadState.Loading) && fixturesPagingAdapter.itemCount == 0) {
                        showEmptyState()
                    }
                }
                LoadState.Loading -> {
                    states.addView(
                        View.inflate(
                            requireContext(),
                            R.layout.item_loading,
                            null
                        )
                    )
                }
                is LoadState.Error -> {
                    states.addView(
                        View.inflate(requireContext(), R.layout.item_error, null).apply {
                            findViewById<TextView>(R.id.tv_error).text =
                                loadState.error.localizedMessage
                            findViewById<Button>(R.id.btn_reload).setOnClickListener {
                                fixturesPagingAdapter.refresh()
                            }
                        })
                }
            }
        }

        lifecycleScope.launch {
            mainViewModel.getScheduledFixtures().collectLatest {
                states.removeAllViews()
                fixturesPagingAdapter.submitData(it)
            }
        }
    }

    private fun showEmptyState() {
        states.addView(View.inflate(requireContext(), R.layout.item_empty_state, null).apply {
            findViewById<TextView>(R.id.tv_empty_message).text =
                "There are no upcoming matches to display"
        })
    }

}