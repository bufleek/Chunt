package com.sports.crichunt.utils

import android.app.Application
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback
import com.sports.crichunt.R
import com.sports.crichunt.data.repos.FixtureRepo
import com.sports.crichunt.data.repos.MainRepo

class CricHunt : Application() {
    val mainRepo by lazy { MainRepo() }
    val fixtureRepo by lazy { FixtureRepo() }
    var mAdManagerInterstitialAd: AdManagerInterstitialAd? = null

    fun initInterstitial() {
        if (mAdManagerInterstitialAd == null) {
            val adRequest = AdManagerAdRequest.Builder().build()
            AdManagerInterstitialAd.load(
                this,
                getString(R.string.interstistial_ad_id),
                adRequest,
                object : AdManagerInterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        mAdManagerInterstitialAd = null
                    }

                    override fun onAdLoaded(interstitialAd: AdManagerInterstitialAd) {
                        mAdManagerInterstitialAd = interstitialAd
                    }
                })
        }
    }
}