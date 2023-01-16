package com.example.yuvish.models.baseIndikatorsIndex

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.yuvish.retrofit.RetrofitService
import retrofit2.HttpException

class ReceivedRewashIndicatorPagingSource (
    private val fromDate: String,
    private val toDate: String,
    private val retrofitService: RetrofitService,
): PagingSource<Int, ReceivedRewashIndicatorOrder>() {
    override fun getRefreshKey(state: PagingState<Int, ReceivedRewashIndicatorOrder>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReceivedRewashIndicatorOrder> {
        return try {
            val pageNumber = params.key ?: 1
            val pageSize = params.loadSize
            val response = retrofitService.getReceivedRewashIndicators(fromDate, toDate, pageNumber)

            if (response.isSuccessful) {
                val receivedRewashIndicatorOrder = response.body()!!
                val nextPageNumber = if (receivedRewashIndicatorOrder.data.size < pageSize) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                return LoadResult.Page(receivedRewashIndicatorOrder.data, prevPageNumber, nextPageNumber)
            } else {
                return LoadResult.Error(HttpException(response))
            }
        }catch (t: Throwable){
            LoadResult.Error(t)
        }
    }

}