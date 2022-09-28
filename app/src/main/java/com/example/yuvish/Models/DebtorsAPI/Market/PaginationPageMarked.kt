package com.example.yuvish.Models.DebtorsAPI.Market

import android.content.ContentValues.TAG
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.yuvish.retrofit.RetrofitService
import retrofit2.HttpException

class PaginationPageMarked(
    private val retrofitService: RetrofitService, val driver: Int, val type: String
) : PagingSource<Int, MarketPaginationItem>() {
    override fun getRefreshKey(state: PagingState<Int, MarketPaginationItem>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MarketPaginationItem> {
        return try {
            val pageNumber = params.key ?: 1
            val pageSize = params.loadSize
            val response = retrofitService.marketPagination(pageNumber, driver, type)
            Log.e(TAG, "load: ${response.body()}" )
            if (response.code() == 200) {
                Log.e(TAG, "load: ${response.body()}")
                val marketPaginationItem = response.body()!!
                val nextPageNumber = if (marketPaginationItem.size < pageSize) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                return LoadResult.Page(marketPaginationItem, prevPageNumber, nextPageNumber)
            } else {
                return LoadResult.Error(HttpException(response))
            }
        } catch (t: Throwable) {
            LoadResult.Error(t)
        }
    }
}