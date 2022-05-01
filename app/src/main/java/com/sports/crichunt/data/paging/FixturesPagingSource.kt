package com.sports.crichunt.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sports.crichunt.data.api.ApiEndpoints
import com.sports.crichunt.data.models.Fixture
import retrofit2.awaitResponse

private const val TAG = "FixturesPagingSource"
class FixturesPagingSource(
    private val apiService: ApiEndpoints,
    private val status: String
) : PagingSource<Int, Fixture>() {
    override fun getRefreshKey(state: PagingState<Int, Fixture>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Fixture> {
        val nextPageNumber = params.key ?: STARTING_PAGE

        try {
            val response = apiService.getFixtures(status, page=nextPageNumber)
                .awaitResponse()
            if (!response.isSuccessful) {
                return LoadResult.Error(
                    Throwable("Request failed, something went wrong")
                )
            }
            val data = response.body()
            var results = data?.results ?: ArrayList()
            if(status.equals("live", true)){
                for (fixture in results){
                    fixture.live = true
                }
            }
            return LoadResult.Page(
                data = results,
                prevKey = if (nextPageNumber == STARTING_PAGE) null else nextPageNumber,
                nextKey = if(data?.next != null) nextPageNumber + 1 else null
            )

        } catch (e: Exception) {
            Log.d(TAG, "load: ${e}")
            return LoadResult.Error(Throwable("Request failed, try checking your connection"))
        }
    }

    companion object {
        private const val STARTING_PAGE = 1
    }
}