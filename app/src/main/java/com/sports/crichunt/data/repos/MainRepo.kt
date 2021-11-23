package com.sports.crichunt.data.repos

import androidx.lifecycle.MutableLiveData
import com.sports.crichunt.data.models.*
import com.sports.crichunt.utils.dateToString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainRepo : BaseRepo() {
    val liveFixturesRequestState: MutableLiveData<RequestState> = MutableLiveData()
    val upcomingFixturesRequestState: MutableLiveData<RequestState> = MutableLiveData()
    val finishedFixturesRequestState: MutableLiveData<RequestState> = MutableLiveData()
    val stagesRequestState: MutableLiveData<RequestState> = MutableLiveData()
    val newsRequestState: MutableLiveData<RequestState> = MutableLiveData()
    val seasons: MutableLiveData<ArrayList<Season>> = MutableLiveData(ArrayList())
    var upcomingDates = ""
    var finishedDates = ""
    var fetchedSeasonsCount = 0
    val fetchedSeasons = ArrayList<Season>()
    var stagesCount = 0

    init {
        var calendar = Calendar.getInstance()
        val currentDate = calendar.time
        calendar.add(Calendar.MONTH, 12)
        val upcomingToDate = calendar.time
        upcomingDates =
            currentDate.dateToString("yyyy-MM-dd") + "," + upcomingToDate.dateToString("yyyy-MM-dd")
        calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        val finishedToDate = calendar.time
        finishedDates =
            finishedToDate.dateToString("yyyy-MM-dd") + "," + currentDate.dateToString("yyyy-MM-dd")
    }

    fun getFinishedFixtures() {
        finishedFixturesRequestState.value = RequestState.Loading
        apiService.getFixtures(FixtureStatus.FINISHED, finishedDates)
            .enqueue(object : Callback<BaseResponse<ArrayList<Fixture>>> {
                override fun onResponse(
                    call: Call<BaseResponse<ArrayList<Fixture>>>,
                    response: Response<BaseResponse<ArrayList<Fixture>>>
                ) {
                    if (response.isSuccessful) {
                        finishedFixturesRequestState.value = RequestState.Success(
                            ArrayList(
                                response.body()?.data?.sortedByDescending { it.starting_at } ?: ArrayList()
                            )
                        )
                    } else {
                        finishedFixturesRequestState.value = RequestState.Error(
                            response.body()?.message?.message
                                ?: "Failed to get finished fixtures, something went wrong"
                        )
                    }
                }

                override fun onFailure(call: Call<BaseResponse<ArrayList<Fixture>>>, t: Throwable) {
                    finishedFixturesRequestState.value =
                        RequestState.Error("Failed to get finished fixtures, Try checking your connection")
                }

            })
    }

    fun getUpcomingFixtures() {
        upcomingFixturesRequestState.value = RequestState.Loading

        apiService.getFixtures(FixtureStatus.SCHEDULED, upcomingDates)
            .enqueue(object : Callback<BaseResponse<ArrayList<Fixture>>> {
                override fun onResponse(
                    call: Call<BaseResponse<ArrayList<Fixture>>>,
                    response: Response<BaseResponse<ArrayList<Fixture>>>
                ) {
                    if (response.isSuccessful) {
                        upcomingFixturesRequestState.value =
                            RequestState.Success(response.body()?.data ?: ArrayList())
                    } else {
                        upcomingFixturesRequestState.value = RequestState.Error(
                            response.body()?.message?.message
                                ?: "Failed to get upcoming fixtures, something went wrong"
                        )
                    }
                }

                override fun onFailure(call: Call<BaseResponse<ArrayList<Fixture>>>, t: Throwable) {
                    finishedFixturesRequestState.value =
                        RequestState.Error("Failed to get finished upcoming, Try checking your connection")
                }

            })
    }

    fun getStages() {
        stagesRequestState.value = RequestState.Loading
        apiService.getStages()
            .enqueue(object : Callback<BaseResponse<ArrayList<Stage>>> {
                override fun onResponse(
                    call: Call<BaseResponse<ArrayList<Stage>>>,
                    response: Response<BaseResponse<ArrayList<Stage>>>
                ) {
                    if (response.isSuccessful) {
                        val stages = ArrayList(response.body()?.data ?: ArrayList()).reversed()
                        stagesRequestState.value =
                            RequestState.Success(stages)
                        getSeasons(ArrayList(stages))
                    } else {
                        stagesRequestState.value = RequestState.Error(
                            response.body()?.message?.message
                                ?: "Failed to get fixtures something went wrong"
                        )
                    }
                }

                override fun onFailure(call: Call<BaseResponse<ArrayList<Stage>>>, t: Throwable) {
                    stagesRequestState.value =
                        RequestState.Error("Failed to get fixtures, Try checking your connection")
                }

            })
    }

    fun getLiveFixtures() {
        liveFixturesRequestState.value = RequestState.Loading
        apiService.getLiveFixtures().enqueue(object : Callback<BaseResponse<ArrayList<Fixture>>> {
            override fun onResponse(
                call: Call<BaseResponse<ArrayList<Fixture>>>,
                response: Response<BaseResponse<ArrayList<Fixture>>>
            ) {
                if (response.isSuccessful) {
                    liveFixturesRequestState.value =
                        RequestState.Success(response.body()?.data ?: ArrayList())
                } else {
                    liveFixturesRequestState.value = RequestState.Error(
                        response.body()?.message?.message
                            ?: "Failed to get live matches, Something went wrong."
                    )
                }
            }

            override fun onFailure(call: Call<BaseResponse<ArrayList<Fixture>>>, t: Throwable) {
                liveFixturesRequestState.value =
                    RequestState.Error("Failed to get live matches, Try checking your connection.")
            }
        })
    }

    private fun getSeasons(stages: ArrayList<Stage>) {
        fetchedSeasons.clear()
        fetchedSeasonsCount = 0
        stagesCount = stages.size
        for (stage in stages) {
            CoroutineScope(Dispatchers.IO).launch {
                getSeason(stage.season_id)
            }
        }
    }

    private fun getSeason(seasonId: Int) {
        apiService.getSeason(seasonId).enqueue(object : Callback<BaseResponse<Season>> {
            override fun onResponse(
                call: Call<BaseResponse<Season>>,
                response: Response<BaseResponse<Season>>
            ) {
                fetchedSeasonsCount++
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        fetchedSeasons.add(it)
                    }
                }
                if (stagesCount == fetchedSeasonsCount) {
                    seasons.value = fetchedSeasons
                }
            }

            override fun onFailure(call: Call<BaseResponse<Season>>, t: Throwable) {
                fetchedSeasonsCount++
                if (stagesCount == fetchedSeasonsCount) {
                    seasons.value = fetchedSeasons
                }
            }
        })
    }

    fun getNewsArticles(){
        newsRequestState.value = RequestState.Loading
        newsService.getNewsArticles().enqueue(object : Callback<Feed>{
            override fun onResponse(
                call: Call<Feed>,
                response: Response<Feed>
            ) {
                if(!response.isSuccessful){
                    newsRequestState.value = RequestState.Error("Failed to get news articles, Something went wrong")
                }else{
                    newsRequestState.value = RequestState.Success(response.body())
                }
            }

            override fun onFailure(call: Call<Feed>, t: Throwable) {
                newsRequestState.value = RequestState.Error("Failed to get news articles, Try checking your connection")
            }
        })
    }
}

