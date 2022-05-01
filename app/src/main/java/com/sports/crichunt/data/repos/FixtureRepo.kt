package com.sports.crichunt.data.repos

import androidx.lifecycle.MutableLiveData
import com.sports.crichunt.data.models.Inning
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FixtureRepo: BaseRepo() {
    val _fixtureScorecard = MutableLiveData<ArrayList<Inning>>()
    val _scorecardLoading = MutableLiveData<Boolean>(false)
    val _scorecardError = MutableLiveData<String?>(null)

    fun getScorecard(fixtureId: Int){
        try {
            _scorecardLoading.value = true
            _scorecardError.value = null
            apiService.getScorecard(fixtureId).enqueue(object : Callback<ArrayList<Inning>>{
                override fun onResponse(
                    call: Call<ArrayList<Inning>>,
                    response: Response<ArrayList<Inning>>
                ) {
                    _scorecardLoading.value = false
                    _scorecardError.value = null
                    if(!response.isSuccessful){
                        _scorecardError.value = "Failed to load scorecard, Something went wrong."
                    }else{
                        val data = response.body()
                        _fixtureScorecard.value = data ?: ArrayList()
                    }
                }

                override fun onFailure(call: Call<ArrayList<Inning>>, t: Throwable) {
                    _scorecardLoading.value = false
                    _scorecardError.value = "Failed to load scorecard, Something went wrong."
                }

            })
        }catch (e: Exception){
            _scorecardLoading.value = false
            _scorecardError.value = "Failed to load scorecard, Something went wrong."
        }
    }
}