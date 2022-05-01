package com.sports.crichunt.data.api

import com.sports.crichunt.data.models.Feed
import com.sports.crichunt.data.models.Fixture
import com.sports.crichunt.data.models.Inning
import com.sports.crichunt.data.models.PagedResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiEndpoints {
    @GET("/api/fixtures/{status}/?")
    fun getFixtures(@Path("status") status: String, @Query("page")page: Int): Call<PagedResponse<Fixture>>

    @GET("api/fixtures/{id}/scorecard/")
    fun getScorecard(@Path("id") fixtureId: Int): Call<ArrayList<Inning>>

    @GET("/rss/khabar/sports/cricket.xml")
    fun getNewsArticles(): Call<Feed>
}