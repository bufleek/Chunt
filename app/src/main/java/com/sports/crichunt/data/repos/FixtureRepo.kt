package com.sports.crichunt.data.repos

import androidx.lifecycle.MutableLiveData
import com.sports.crichunt.data.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FixtureRepo : BaseRepo() {
    val fixtureInfoRequestState: MutableLiveData<RequestState> = MutableLiveData(null)
    val fixtureScoreboardRequestState: MutableLiveData<RequestState> = MutableLiveData(null)
    val fixtureLineupsRequestState: MutableLiveData<RequestState> = MutableLiveData(null)

    fun getFixturesInfo(fixtureId: Int) {
        fixtureInfoRequestState.value = RequestState.Loading
        apiService.getFixtureInfo(fixtureId)
            .enqueue(object : Callback<BaseResponse<FixtureInfo>> {
                override fun onResponse(
                    call: Call<BaseResponse<FixtureInfo>>,
                    response: Response<BaseResponse<FixtureInfo>>
                ) {
                    if (response.isSuccessful) {
                        fixtureInfoRequestState.value =
                            RequestState.Success(response.body()?.data)
                    } else {
                        fixtureInfoRequestState.value = RequestState.Error(
                            response.body()?.message?.message
                                ?: "Could not get this fixture's info, something went wrong"
                        )
                    }
                }

                override fun onFailure(call: Call<BaseResponse<FixtureInfo>>, t: Throwable) {
                    fixtureInfoRequestState.value =
                        RequestState.Error("Could not get this fixture's info, Try checking your connection")
                }
            })
    }

    fun getFixtureScoreboard(fixtureId: Int) {
        fixtureScoreboardRequestState.value = RequestState.Loading
        apiService.getFixtureScoreboard(fixtureId)
            .enqueue(object : Callback<BaseResponse<FixtureScoreboard>> {
                override fun onResponse(
                    call: Call<BaseResponse<FixtureScoreboard>>,
                    response: Response<BaseResponse<FixtureScoreboard>>
                ) {
                    if (response.isSuccessful) {
                        fixtureScoreboardRequestState.value =
                            RequestState.Success(response.body()?.data)
                    } else {
                        fixtureScoreboardRequestState.value = RequestState.Error(
                            response.body()?.message?.message
                                ?: "Failed to load data, something went wrong."
                        )
                    }
                }

                override fun onFailure(call: Call<BaseResponse<FixtureScoreboard>>, t: Throwable) {
                    fixtureScoreboardRequestState.value =
                        RequestState.Error("Failed to load data, Try checking your connection")
                }
            })
    }

    fun getLineups(fixtureId: Int) {
        fixtureLineupsRequestState.value = RequestState.Loading
        apiService.getFixtureLineups(fixtureId).enqueue(object : Callback<BaseResponse<Lineups>> {
            override fun onResponse(
                call: Call<BaseResponse<Lineups>>,
                response: Response<BaseResponse<Lineups>>
            ) {
                if (response.isSuccessful) {
                    fixtureLineupsRequestState.value = RequestState.Success(response.body()?.data)
                }
                else {
                    fixtureLineupsRequestState.value = RequestState.Error(
                        response.body()?.message?.message
                            ?: "Failed to get lineups, Something went wrong."
                    )
                }
            }

            override fun onFailure(call: Call<BaseResponse<Lineups>>, t: Throwable) {
                fixtureLineupsRequestState.value =
                    RequestState.Error("Failed to get lineups, Try checking your connection.")
            }
        })
    }
}