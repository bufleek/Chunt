package com.sports.crichunt.data.api

import com.sports.crichunt.data.models.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiEndpoints {
    @GET("/api/v2.0/fixtures")
    fun getFixtures(
        @Query("filter[status]") status: String,
        @Query("filter[starts_between]") dates: String,
        @Query("sort") sort: String = "starting_at",
        @Query("api_token") apiToken: String = ApiService.apiToken,
        @Query("include") include: String = "visitorteam, localteam, stage, venue, scoreboards"
    ): Call<BaseResponse<ArrayList<Fixture>>>

    @GET("/api/v2.0/livescores")
    fun getLiveFixtures(
        @Query("api_token") apiToken: String = ApiService.apiToken,
        @Query("include") include: String = "visitorteam, localteam, stage, scoreboards"
    ): Call<BaseResponse<ArrayList<Fixture>>>

    @GET("/api/v2.0/stages")
    fun getStages(
        @Query("api_token") apiToken: String = ApiService.apiToken,
        @Query(value = "include") include: String = "league",
        @Query("sort") sort: String = "id"
    ): Call<BaseResponse<ArrayList<Stage>>>

    @GET("/api/v2.0/stages/{id}")
    fun getStageFixtures(
        @Path("id") stageId: Int,
        @Query("api_token") apiToken: String = ApiService.apiToken,
        @Query(value = "include") include: String = "fixtures, fixtures.localteam, fixtures.visitorteam"
    ): Call<BaseResponse<StageFixtures>>

    @GET("/api/v2.0/standings/stage/{id}")
    fun getStandings(
        @Path("id") stageId: Int,
        @Query("api_token") apiToken: String = ApiService.apiToken,
        @Query(value = "include") include: String = "team"
    ): Call<BaseResponse<ArrayList<Standing>>>

    @GET("/api/v2.0/fixtures/{id}")
    fun getFixtureInfo(
        @Path("id") fixtureId: Int,
        @Query("api_token") apiToken: String = ApiService.apiToken,
        @Query(value = "include") include: String = " referee, venue, stage, firstumpire, secondumpire, lineup, scoreboards, localteam, visitorteam"
    ): Call<BaseResponse<FixtureInfo>>

    @GET("/api/v2.0/fixtures/{id}")
    fun getFixtureScoreboard(
        @Path("id") fixtureId: Int,
        @Query("api_token") apiToken: String = ApiService.apiToken,
        @Query(value = "include") include: String = "batting, bowling, scoreboards, balls, runs, manofmatch, lineup"
    ): Call<BaseResponse<FixtureScoreboard>>

    @GET("/api/v2.0/fixtures/{id}")
    fun getFixtureLineups(
        @Path("id") fixtureId: Int,
        @Query("api_token") apiToken: String = ApiService.apiToken,
        @Query(value = "include") include: String = "lineup"
    ): Call<BaseResponse<Lineups>>

    @GET("/api/v2.0/players/{id}")
    fun getPlayerCareer(
        @Path("id") playerId: Int,
        @Query("api_token") apiToken: String = ApiService.apiToken,
        @Query(value = "include") include: String = "career"
    ): Call<BaseResponse<CareerResponse>>

    @GET("/api/v2.0/seasons/{id}")
    fun getSeason(
        @Path("id") seasonId: Int,
        @Query("api_token") apiToken: String = ApiService.apiToken,
    ): Call<BaseResponse<Season>>

    @GET("/rss/khabar/sports/cricket.xml")
    fun getNewsArticles(): Call<Feed>
}