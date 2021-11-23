package com.sports.crichunt.ui.fixtures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sports.crichunt.R
import com.sports.crichunt.ui.fixtures.live.LiveFixturesFragment
import com.sports.crichunt.ui.fixtures.results.CompleteFixturesFragment
import com.sports.crichunt.ui.fixtures.upcoming.UpcomingFragment
import com.sports.crichunt.ui.main.MainActivity

class FixturesHomeFragment : Fragment() {
    private lateinit var tabMain: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fixtures_home, container, false)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).bottomNav.menu.findItem(R.id.action_matches)
            .isChecked = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabMain = view.findViewById(R.id.tab_main)
        viewPager = view.findViewById(R.id.view_pager_main)
        viewPager.adapter = MainViewPagerAdapter()

        TabLayoutMediator(tabMain, viewPager) { tab, index ->
            tab.text = fragmentsMain[index]
        }.attach()
    }

    inner class MainViewPagerAdapter :
        FragmentStateAdapter(requireActivity().supportFragmentManager, lifecycle) {
        override fun getItemCount(): Int {
            return fragmentsMain.size
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                1 -> LiveFixturesFragment()
                2 -> CompleteFixturesFragment()
                else -> UpcomingFragment()
            }
        }

    }

    companion object {
        private val fragmentsMain = arrayOf("Upcoming", "Live", "Results")
    }
}