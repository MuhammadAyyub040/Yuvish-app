package com.example.yuvish.Models.Cleaning

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.yuvish.retrofit.RetrofitService
import retrofit2.HttpException

class PaginationPageCleaning (
    private val retrofitService: RetrofitService,
    private val status: String
    ):PagingSource<Int, RewashReceipt>() {
    override fun getRefreshKey(state: PagingState<Int, RewashReceipt>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, RewashReceipt> {
        return try {
            val pageNumber = params.key ?: 1
            val pageSize = params.loadSize
            val response = retrofitService.cleaning(status, pageNumber)
            Log.d("testresponse", response.code().toString())

            if (response.code() == 200) {
                val rewashReceipt = response.body()!!
                val nextPageNumber = if (rewashReceipt.size < pageSize) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                return PagingSource.LoadResult.Page(rewashReceipt, prevPageNumber, nextPageNumber)
            } else {
                return PagingSource.LoadResult.Error(HttpException(response))
            }
        }catch (t: Throwable){
            PagingSource.LoadResult.Error(t)
        }
    }

}