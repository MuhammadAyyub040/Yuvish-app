package com.example.yuvish.Models.Warehouse

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.yuvish.retrofit.RetrofitService
import retrofit2.HttpException

class PaginationPageWerehouse (
    private val retrofitService: RetrofitService,
): PagingSource<Int, OrdersOmborItem>() {
    override fun getRefreshKey(state: PagingState<Int, OrdersOmborItem>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, OrdersOmborItem> {
        return try {
            val pageNumber = params.key ?: 1
            val pageSize = params.loadSize
            val response = retrofitService.warehouse(pageNumber)
            Log.d("testresponse", response.toString())

            if (response.isSuccessful) {
                val ordersOmborItem = response.body()!!
                val nextPageNumber = if (ordersOmborItem.size < pageSize) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                return PagingSource.LoadResult.Page(ordersOmborItem, prevPageNumber, nextPageNumber)
            } else {
                return PagingSource.LoadResult.Error(HttpException(response))
            }
        }catch (t: Throwable){
            PagingSource.LoadResult.Error(t)
        }
    }

}