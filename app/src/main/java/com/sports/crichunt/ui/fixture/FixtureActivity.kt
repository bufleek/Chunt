package com.sports.crichunt.ui.fixture

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelLazy
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sports.crichunt.R
import com.sports.crichunt.data.models.Fixture
import com.sports.crichunt.data.models.Team
import com.sports.crichunt.databinding.BindingFixtureActivity
import com.sports.crichunt.ui.fixture.info.FixtureInfoFragment
import com.sports.crichunt.ui.fixture.scoreboard.FixtureScoreboardFragment
import com.sports.crichunt.utils.CricHunt
import com.sports.crichunt.utils.MyViewModels
import com.sports.crichunt.utils.ViewModelFactory

class FixtureActivity : AppCompatActivity() {
    private lateinit var binding: BindingFixtureActivity
    private lateinit var tabFixture: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var tvHomeScore: TextView
    private lateinit var tvAwayScore: TextView
    var isFromStage = false
    private lateinit var adView: AdView
    lateinit var appBarLayout: AppBarLayout
    private val mAdManagerInterstitialAd get() = (application as CricHunt).mAdManagerInterstitialAd

    val fixtureViewModel by ViewModelLazy(
        FixtureViewModel::class,
        { viewModelStore },
        { ViewModelFactory(MyViewModels.FIXTURE((application as CricHunt).fixtureRepo)) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BindingFixtureActivity.inflate(layoutInflater)
        setContentView(binding.root)
        tabFixture = findViewById(R.id.tab_fixture)
        viewPager = findViewById(R.id.view_pager_fixture)
        adView = findViewById(R.id.adView)
        appBarLayout = findViewById(R.id.fixture_app_bar)

        tvHomeScore = findViewById(R.id.tv_local_scores)
        tvAwayScore = findViewById(R.id.tv_away_score)

        MobileAds.initialize(this) {
            loadAd()
        }
        fixtureViewModel.initialize()

        val fixtureString = intent?.getStringExtra(FIXTURE)
        isFromStage = intent?.getBooleanExtra(IS_FROM_STAGE, false) ?: false
        fixtureString?.let {
            fixtureViewModel.fixture =
                Gson().fromJson(it, object : TypeToken<Fixture>() {}.type)
            binding.fixture = fixtureViewModel.fixture
            fixtureViewModel.fixture?.let { fixture ->
                tvHomeScore.text = fixture.team_a.score?.full_score
                tvAwayScore.text = fixture.team_b.score?.full_score
            }
        }

        viewPager.adapter = FixtureViewPagerAdapter()
        TabLayoutMediator(tabFixture, viewPager) { tab, index ->
            tab.text = fixtureFragments[index]
        }.attach()
        (application as CricHunt).initInterstitial()
    }

    fun updateScore(teamA: Team, teamB: Team) {
        tvHomeScore.text = teamA.score?.full_score
        tvAwayScore.text = teamB.score?.full_score
    }

    private fun loadAd() {
        val adRequest = AdRequest.Builder()
            .build()
        adView.loadAd(adRequest)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        showInterstitialAd(FINISH)
    }

    fun showInterstitialAd(postAdAction: String) {
        val ad = (application as CricHunt).mAdManagerInterstitialAd
        if (ad != null) {
            initAd(postAdAction)
            ad.show(this)
        } else {
            performPostAdAction(postAdAction)
        }
    }

    private fun initAd(postAdAction: String) {
        mAdManagerInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                (application as CricHunt).mAdManagerInterstitialAd = null
                (application as CricHunt).initInterstitial()
                performPostAdAction(postAdAction)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                (application as CricHunt).initInterstitial()
                performPostAdAction(postAdAction)
            }

            override fun onAdShowedFullScreenContent() {
                (application as CricHunt).mAdManagerInterstitialAd = null
            }
        }
    }

    private fun performPostAdAction(postAdAction: String) {
        when (postAdAction) {
            FINISH -> {
                finish()
            }
        }
    }

    companion object {
        const val IS_FROM_STAGE = "FROM_STAGE"
        const val FIXTURE = "FIXTURE"
        private val fixtureFragments = arrayOf("Scoreboard")
        const val LAUNCH_STAGE = "LAUNCH_STAGE"
        const val FINISH = "FINISH"
    }

    inner class FixtureViewPagerAdapter : FragmentStateAdapter(supportFragmentManager, lifecycle) {
        override fun getItemCount(): Int {
            return fixtureFragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                1 -> FixtureInfoFragment()
                else -> FixtureScoreboardFragment()
            }
        }
    }
}