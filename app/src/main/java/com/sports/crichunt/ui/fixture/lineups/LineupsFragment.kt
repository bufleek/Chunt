package com.sports.crichunt.ui.fixture.lineups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.sports.crichunt.R
import com.sports.crichunt.data.models.Lineups
import com.sports.crichunt.data.models.Player
import com.sports.crichunt.data.models.RequestState
import com.sports.crichunt.ui.fixture.FixtureActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LineupsFragment : Fragment() {
    private lateinit var tabLineups: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var states: FrameLayout
    private val lineupsViewPagerAdapter by lazy { LineupsViewpagerAdapter() }

    private val fixtureViewModel by lazy { (requireActivity() as FixtureActivity).fixtureViewModel }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lineups, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLineups = view.findViewById(R.id.tab_lineups)
        viewPager = view.findViewById(R.id.viewpager_lineups)
        states = view.findViewById(R.id.state_lineups)

        viewPager.adapter = lineupsViewPagerAdapter
        TabLayoutMediator(tabLineups, viewPager) { tab, position ->
            tab.text = lineupsFragments[position]
        }.attach()

        fixtureViewModel.fixtureLineupsRequestState.observe(viewLifecycleOwner, {
            states.removeAllViews()
            when (it) {
                is RequestState.Error -> states.addView(
                    View.inflate(
                        requireContext(),
                        R.layout.item_error,
                        null
                    ).apply {
                        findViewById<TextView>(R.id.tv_error).text = it.error
                        findViewById<Button>(R.id.btn_reload).setOnClickListener { fixtureViewModel.getLineups() }
                    })
                RequestState.Loading -> states.addView(
                    View.inflate(
                        requireContext(),
                        R.layout.item_loading,
                        null
                    )
                )
                is RequestState.Success<*> -> {
                    if (it.result is Lineups) {
                        if (it.result.lineup.isNullOrEmpty()) {
                            showEmptyState()
                        } else {
                            keys = it.result.lineup.groupBy { player -> player.lineup.team_id }.keys
                            lineupsFragments.clear()
                            lineups.clear()
                            val fixture = fixtureViewModel.fixture.value!!
                            for (key in keys) {
                                if (fixture.localteam?.id == key) {
                                    lineupsFragments.add(fixture.localteam.name)
                                } else if (fixture.visitorteam?.id == key) {
                                    lineupsFragments.add(fixture.visitorteam.name)
                                }
                                lineups.add(ArrayList(it.result.lineup.filter { player -> player.lineup.team_id == key }))
                            }
                            lineupsViewPagerAdapter.notifyItemRangeChanged(0, 2)
                        }
                    } else {
                        showEmptyState()
                    }
                }
                null -> fixtureViewModel.getLineups()
            }
        })
    }

    private fun showEmptyState() {
        states.addView(View.inflate(requireContext(), R.layout.item_empty_state, null).apply {
            findViewById<TextView>(R.id.tv_empty_message).text = "No lineups available"
        })
    }

    companion object {
        private val lineupsFragments = arrayListOf<String>()
        private val lineups  = ArrayList<ArrayList<Player>>()
        private var keys: Set<Int> = setOf()
    }

    inner class LineupsViewpagerAdapter : FragmentStateAdapter(
        requireActivity().supportFragmentManager,
        lifecycle
    ) {
        override fun getItemCount(): Int = lineupsFragments.size

        override fun createFragment(position: Int): Fragment = LineUpFragment.newInstance(lineups[position])
    }
}