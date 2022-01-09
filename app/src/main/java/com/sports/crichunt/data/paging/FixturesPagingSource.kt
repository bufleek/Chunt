package com.sports.crichunt.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sports.crichunt.data.api.ApiEndpoints
import com.sports.crichunt.data.models.Fixture
import retrofit2.awaitResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FixturesPagingSource(
    private val apiService: ApiEndpoints,
    private val status: String
) : PagingSource<Int, Fixture>() {

    override val keyReuseSupported: Boolean
        get() = true

    override fun getRefreshKey(state: PagingState<Int, Fixture>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Fixture> {
        val nextPageNumber = params.key ?: STARTING_PAGE

        try {
            val dates = getDates(nextPageNumber)
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
                nextKey = nextPageNumber + 1
            )

        } catch (e: Exception) {
            return LoadResult.Error(Throwable(Throwable("Request failed, try checking your connection")))
        }
    }

    private fun getDates(nextPageNumber: Int): String{
        val startDate = if(status.equals("NS", true)){
            if(nextPageNumber == STARTING_PAGE) {
                Calendar.getInstance().timeInMillis
            }
            else {
                (Calendar.getInstance().apply {
                    add(Calendar.DATE, nextPageNumber)
                }).timeInMillis
            }
        }else{
            if(nextPageNumber == STARTING_PAGE) {
                Calendar.getInstance().timeInMillis
            }
            else {
                (Calendar.getInstance().apply {
                    add(Calendar.DATE, (nextPageNumber+1).unaryMinus())
                }).timeInMillis
            }
        }

        val endDate = if(status == "NS"){
            if(nextPageNumber == STARTING_PAGE) {
                Calendar.getInstance().apply {
                    add(Calendar.DATE, nextPageNumber)
                }.timeInMillis
            }
            else {
                (Calendar.getInstance().apply {
                    add(Calendar.DATE, nextPageNumber+1)
                }).timeInMillis
            }
        }else{
            if(nextPageNumber == STARTING_PAGE) {
                Calendar.getInstance().apply {
                    add(Calendar.DATE, nextPageNumber)
                }.timeInMillis
            }
            else {
                (Calendar.getInstance().apply {
                    add(Calendar.DATE, nextPageNumber.unaryMinus())
                }).timeInMillis
            }
        }
        return "${getDateFromMillis(startDate)},${getDateFromMillis(endDate)}"
    }

    private fun getDateFromMillis(millis: Long): String{
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(millis)
    }

    companion object {
        private const val STARTING_PAGE = 1
    }
}