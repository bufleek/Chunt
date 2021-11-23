package com.sports.crichunt.ui.stages

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelLazy
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sports.crichunt.R
import com.sports.crichunt.data.models.Stage
import com.sports.crichunt.ui.stages.fixtures.FixturesFragment
import com.sports.crichunt.ui.stages.standings.StandingsFragment
import com.sports.crichunt.utils.CricHunt
import com.sports.crichunt.utils.MyViewModels
import com.sports.crichunt.utils.ViewModelFactory

class StageActivity : AppCompatActivity() {
    private lateinit var tabStage: TabLayout
    private lateinit var viewPagerStage: ViewPager2
    private lateinit var tvStageName: TextView
    private lateinit var adView: AdView
    private var startTab: Int = 0

    val stageViewModel by ViewModelLazy(
        StageViewModel::class,
        { viewModelStore },
        { ViewModelFactory(MyViewModels.STAGE((application as CricHunt).stageRepo)) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stage)
        tabStage = findViewById(R.id.tab_stage)
        viewPagerStage = findViewById(R.id.viewPager_stage)
        tvStageName = findViewById(R.id.tv_stage_name)
        adView = findViewById(R.id.adView)

        MobileAds.initialize(this){
            loadAd()
        }

        viewPagerStage.adapter = StageViewPagerAdapter()
        TabLayoutMediator(tabStage, viewPagerStage) { tab, position ->
            tab.text = stageFragments[position]
        }.attach()

        intent?.let {
            stageViewModel.stage.value = Gson().fromJson(
                it.getStringExtra(KEY_STAGE),
                object : TypeToken<Stage?>() {}.type
            )
            startTab = it.getIntExtra(KEY_STAGE_TAB, 0)
        }

        stageViewModel.stage.observe(this, {
            var text = it?.name
            it?.league?.let { lg ->
                text += ", ${lg.name}"
            }
            tvStageName.text = text
        })

        viewPagerStage.currentItem = if (startTab < stageFragments.size) startTab else 0
    }

    companion object {
        const val KEY_STAGE = "STAGE"
        const val KEY_STAGE_TAB = "STAGE_TAB"
        private val stageFragments = arrayOf("Fixtures", "Standings")
    }

    private fun loadAd() {
        val adRequest = AdRequest.Builder()
            .build()
        adView.loadAd(adRequest)
    }

    inner class StageViewPagerAdapter : FragmentStateAdapter(supportFragmentManager, lifecycle) {
        override fun getItemCount(): Int = stageFragments.size

        override fun createFragment(position: Int): Fragment = when (position) {
            1 -> StandingsFragment()
            else -> FixturesFragment()
        }
    }
}