package com.sports.crichunt.ui.main

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelLazy
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.sports.crichunt.R
import com.sports.crichunt.ui.fixtures.FixturesHomeFragment
import com.sports.crichunt.ui.more.MoreFragment
import com.sports.crichunt.ui.news.NewsFragment
import com.sports.crichunt.ui.stages.list.StagesFragment
import com.sports.crichunt.utils.CricHunt
import com.sports.crichunt.utils.MyViewModels
import com.sports.crichunt.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var adView: AdView
    lateinit var bottomNav: BottomNavigationView
    private lateinit var fragmentContainer: FrameLayout

    val mainViewModel by ViewModelLazy(
        MainViewModel::class,
        { viewModelStore },
        { ViewModelFactory(MyViewModels.MAIN((application as CricHunt).mainRepo)) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adView = findViewById(R.id.adView)
        bottomNav = findViewById(R.id.bottom_nav_main)
        fragmentContainer = findViewById(R.id.fragment_container)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FixturesHomeFragment(), null)
            .commit()

        bottomNav.setOnItemSelectedListener {
            val fr: Fragment? = when (it.itemId) {
                R.id.action_more -> MoreFragment()
                R.id.action_stages -> StagesFragment()
                R.id.action_news -> NewsFragment()
                else -> FixturesHomeFragment()
            }
            fr?.let {
                supportFragmentManager.beginTransaction()
                    .addToBackStack("main")
                    .replace(R.id.fragment_container, it, null)
                    .commit()
            }
            true
        }

        MobileAds.initialize(this) {
            loadAd()
        }

        (application as CricHunt).initInterstitial()

        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                Snackbar.make(
                    this,
                    fragmentContainer,
                    "A new version of our app is available",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("Cancel") {}
                    .setAction("Update") {
                        appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            this,
                            UPDATE_REQUEST
                        )
                    }.show()
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val UPDATE_REQUEST = 100
    }

    private fun loadAd() {
        val adRequest = AdRequest.Builder()
            .build()
        adView.loadAd(adRequest)
    }
}