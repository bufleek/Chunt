package com.sports.crichunt.ui.news

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sports.crichunt.R
import com.sports.crichunt.data.models.Article
import com.sports.crichunt.databinding.BindingNewsActivity

class NewsDetailActivity : AppCompatActivity() {
    private lateinit var adView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = BindingNewsActivity.inflate(layoutInflater)
        setContentView(binding.root)
        val articleJson = intent?.getStringExtra(KEY_ARTICLE)
        articleJson?.let {
            binding.article =
                Gson().fromJson<Article>(articleJson, object : TypeToken<Article>() {}.type)
        }
        adView = findViewById(R.id.adView)
        MobileAds.initialize(this) {
            loadAd()
        }
    }

    private fun loadAd() {
        val adRequest = AdRequest.Builder()
            .build()
        adView.loadAd(adRequest)
    }

    companion object {
        const val KEY_ARTICLE = "ARTICLE"
    }
}