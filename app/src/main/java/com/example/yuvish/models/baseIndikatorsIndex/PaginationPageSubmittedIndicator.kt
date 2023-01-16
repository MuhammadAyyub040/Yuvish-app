package com.example.yuvish.models.baseIndikatorsIndex

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.yuvish.retrofit.RetrofitService
import retrofit2.HttpException

class PaginationPageSubmittedIndicator (
    private val retrofitService: RetrofitService,
    private val fromDate: String,
    private val toDate: String
    ): PagingSource<Int, SubmittedIndicatorOrder>() {
    override fun getRefreshKey(state: PagingState<Int, SubmittedIndicatorOrder>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
        Log.e("TAG", "load: ")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SubmittedIndicatorOrder> {
        Log.e("TAG", "load: ")
        return try {
            val pageNumber = params.key ?: 1
            val pageSize = params.loadSize
            val response = retrofitService.getSubmittedIndicators(fromDate, toDate, pageNumber)
            Log.e("TAG", "load: ${response.code()} ${response.body()}")

            if (response.code() == 200) {
                Log.e("TAG", "load: ${response.code()} ${response.body()}")
                val submittedIndicatorOrder = response.body()!!
                val nextPageNumber = if (submittedIndicatorOrder.data.size < pageSize) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                return LoadResult.Page(submittedIndicatorOrder.data, prevPageNumber, nextPageNumber)
            } else {
                return LoadResult.Error(HttpException(response))
            }
        }catch (t: Throwable){
            t.printStackTrace()
            LoadResult.Error(t)
        }
    }

}