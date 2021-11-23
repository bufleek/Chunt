package com.sports.crichunt.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sports.crichunt.data.api.ApiEndpoints
import com.sports.crichunt.data.models.Fixture
import retrofit2.awaitResponse

class FixturesPagingSource(
    private val apiService: ApiEndpoints,
    private val status: String,
    private val dates: String
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
            val response = apiService.getFixtures(status, dates)
                .awaitResponse()
            if (!response.isSuccessful) {
                return LoadResult.Error(
                    Throwable(
                        response.body()?.message?.message ?: "Request failed, something went wrong"
                    )
                )
            }
            val data = response.body()?.data ?: ArrayList()
            return LoadResult.Page(
                data = data,
                prevKey = if (nextPageNumber == STARTING_PAGE) null else nextPageNumber,
                nextKey = if (data.isNullOrEmpty()) null else nextPageNumber + 1
            )

        } catch (e: Exception) {
            return LoadResult.Error(Throwable(Throwable("Request failed, try checking your connection")))
        }
    }

    companion object {
        private const val STARTING_PAGE = 1
    }
}