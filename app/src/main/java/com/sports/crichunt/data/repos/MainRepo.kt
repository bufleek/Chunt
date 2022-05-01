package com.sports.crichunt.data.repos

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sports.crichunt.data.models.Feed
import com.sports.crichunt.data.models.Fixture
import com.sports.crichunt.data.models.RequestState
import com.sports.crichunt.data.paging.FixturesPagingSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepo: BaseRepo() {
    private val TAG = "MainRepo"
    val newsRequestState: MutableLiveData<RequestState> = MutableLiveData()

    private fun getFixturesFlow(status: String): Flow<PagingData<Fixture>>
    {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 2
            ),
            pagingSourceFactory = { FixturesPagingSource(apiService, status) }
        ).flow
    }

    fun getLiveFixtures(): Flow<PagingData<Fixture>>{
        return getFixturesFlow("live")
    }

    fun getFixturesResults(): Flow<PagingData<Fixture>>{
        return getFixturesFlow("concluded")
    }

    fun getScheduledFixtures(): Flow<PagingData<Fixture>>{
        return getFixturesFlow("scheduled")
    }

    fun getNewsArticles() {
        newsRequestState.value = RequestState.Loading
        newsService.getNewsArticles().enqueue(object : Callback<Feed> {
            override fun onResponse(
                call: Call<Feed>,
                response: Response<Feed>
            ) {
                if (!response.isSuccessful) {
                    newsRequestState.value =
                        RequestState.Error("Failed to get news articles, Something went wrong")
                } else {
                    newsRequestState.value = RequestState.Success(response.body())
                }
            }

            override fun onFailure(call: Call<Feed>, t: Throwable) {
                newsRequestState.value =
                    RequestState.Error("Failed to get news articles, Try checking your connection")
            }
        })
    }
}