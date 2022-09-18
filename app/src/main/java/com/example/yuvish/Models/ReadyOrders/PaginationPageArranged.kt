package com.example.yuvish.Models.ReadyOrders

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.yuvish.retrofit.RetrofitService
import retrofit2.HttpException

class PaginationPageArranged (
    private val retrofitService: RetrofitService, val driver: Int, val type: String
): PagingSource<Int, ReadyOrdersItem>() {
    override fun getRefreshKey(state: PagingState<Int, ReadyOrdersItem>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReadyOrdersItem> {
        return try {
            val pageNumber = params.key ?: 1
            val pageSize = params.loadSize
            val response = retrofitService.arranged(pageNumber, driver, type)

            if (response.code() == 200) {
                val readyOrdersItem = response.body()!!
                val nextPageNumber = if (readyOrdersItem.size < pageSize) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                return LoadResult.Page(readyOrdersItem, prevPageNumber, nextPageNumber)
            } else {
                return LoadResult.Error(HttpException(response))
            }
        }catch (t: Throwable){
            LoadResult.Error(t)
        }
    }

}