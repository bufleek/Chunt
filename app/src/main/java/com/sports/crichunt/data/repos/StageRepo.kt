package com.sports.crichunt.data.repos

import androidx.lifecycle.MutableLiveData
import com.sports.crichunt.data.models.BaseResponse
import com.sports.crichunt.data.models.RequestState
import com.sports.crichunt.data.models.StageFixtures
import com.sports.crichunt.data.models.Standing
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StageRepo : BaseRepo() {
    val standingsRequestState: MutableLiveData<RequestState> = MutableLiveData()
    val stageFixturesRequestState: MutableLiveData<RequestState> = MutableLiveData()

    fun getStandings(stageId: Int) {
        standingsRequestState.value = RequestState.Loading
        apiService.getStandings(stageId)
            .enqueue(object : Callback<BaseResponse<ArrayList<Standing>>> {
                override fun onResponse(
                    call: Call<BaseResponse<ArrayList<Standing>>>,
                    response: Response<BaseResponse<ArrayList<Standing>>>
                ) {
                    if (response.isSuccessful) {
                        standingsRequestState.value =
                            RequestState.Success(response.body()?.data ?: ArrayList())
                    } else {
                        standingsRequestState.value = RequestState.Error(
                            response.body()?.message?.message
                                ?: "Failed to get series data, something went wrong."
                        )
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<ArrayList<Standing>>>,
                    t: Throwable
                ) {
                    standingsRequestState.value =
                        RequestState.Error("Failed to get series data, Try checking your connection.")
                }

            })
    }

    fun getStageFixtures(stageId: Int) {
        stageFixturesRequestState.value = RequestState.Loading
        apiService.getStageFixtures(stageId)
            .enqueue(object : Callback<BaseResponse<StageFixtures>> {
                override fun onResponse(
                    call: Call<BaseResponse<StageFixtures>>,
                    response: Response<BaseResponse<StageFixtures>>
                ) {

                    if (response.isSuccessful) {
                        stageFixturesRequestState.value =
                            RequestState.Success(response.body()?.data)
                    } else {
                        stageFixturesRequestState.value = RequestState.Error(
                            response.body()?.message?.message
                                ?: "Request failed, Something went wrong."
                        )
                    }
                }

                override fun onFailure(call: Call<BaseResponse<StageFixtures>>, t: Throwable) {
                    standingsRequestState.value =
                        RequestState.Error("Request failed, Try checking your connection.")
                }
            })
    }
}