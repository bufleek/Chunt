package com.sports.crichunt.data.repos

import androidx.lifecycle.MutableLiveData
import com.sports.crichunt.data.models.BaseResponse
import com.sports.crichunt.data.models.CareerResponse
import com.sports.crichunt.data.models.RequestState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlayerRepo : BaseRepo() {
    val careerRequestState = MutableLiveData<RequestState?>(null)

    fun getCareer(playerId: Int) {
        careerRequestState.value = RequestState.Loading
        apiService.getPlayerCareer(playerId)
            .enqueue(object : Callback<BaseResponse<CareerResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<CareerResponse>>,
                    response: Response<BaseResponse<CareerResponse>>
                ) {
                    if (response.isSuccessful) {
                        careerRequestState.value =
                            RequestState.Success(response.body()?.data?.career ?: ArrayList())
                    } else {
                        careerRequestState.value = RequestState.Error(
                            response.body()?.message?.message
                                ?: "Failed to get player's career data, Something went wrong."
                        )
                    }
                }

                override fun onFailure(call: Call<BaseResponse<CareerResponse>>, t: Throwable) {
                    careerRequestState.value =
                        RequestState.Error("Failed to get player's career data, Try checking your connection.")
                }
            })
    }
}