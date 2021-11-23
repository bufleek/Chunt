package com.sports.crichunt.ui.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelLazy
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.sports.crichunt.R
import com.sports.crichunt.data.models.Player
import com.sports.crichunt.databinding.BindingPlayerActivity
import com.sports.crichunt.ui.player.batting.PlayerBattingFragment
import com.sports.crichunt.ui.player.bowling.PlayerBowlingFragment
import com.sports.crichunt.ui.player.info.PlayerInfoFragment
import com.sports.crichunt.utils.CricHunt
import com.sports.crichunt.utils.MyViewModels
import com.sports.crichunt.utils.ViewModelFactory
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlayerActivity : AppCompatActivity() {
    private lateinit var appbar: AppBarLayout
    private lateinit var tabPlayer: TabLayout
    private lateinit var viewpager: ViewPager2
    private lateinit var adView: AdView

    val playerViewModel by ViewModelLazy(
        PlayerViewModel::class,
        { viewModelStore },
        { ViewModelFactory(MyViewModels.PLAYER((application as CricHunt).playerRepo)) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = BindingPlayerActivity.inflate(layoutInflater)
        setContentView(binding.root)

        appbar = findViewById(R.id.appbar_player)
        tabPlayer = findViewById(R.id.tab_player)
        viewpager = findViewById(R.id.viewpager_player)
        adView = findViewById(R.id.adView)

        MobileAds.initialize(this){
            loadAd()
        }

        viewpager.adapter = PlayerViewpagerAdapter()
        TabLayoutMediator(tabPlayer, viewpager) { tab, position ->
            tab.text = playerFragments[position]
        }.attach()
        val playerJson = intent?.getStringExtra(KEY_PLAYER)
        playerViewModel.player.value =
            Gson().fromJson(playerJson, object : TypeToken<Player>() {}.type)
        playerViewModel.initialize()

        playerViewModel.player.observe(this, {
            it?.let { player -> binding.player = player }
        })
    }

    private fun loadAd() {
        val adRequest = AdRequest.Builder()
            .build()
        adView.loadAd(adRequest)
    }

    companion object {
        const val KEY_PLAYER = "PLAYER"
        private val playerFragments = arrayOf("Info", "Batting", "Bowling")
    }

    inner class PlayerViewpagerAdapter : FragmentStateAdapter(supportFragmentManager, lifecycle) {
        override fun getItemCount() = playerFragments.size

        override fun createFragment(position: Int) = when (position) {
            1 -> PlayerBattingFragment()
            2 -> PlayerBowlingFragment()
            else -> PlayerInfoFragment()
        }
    }
}