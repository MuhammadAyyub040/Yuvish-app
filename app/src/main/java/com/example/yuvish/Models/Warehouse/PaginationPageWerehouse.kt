package com.example.yuvish.Models.Warehouse

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.yuvish.retrofit.RetrofitService
import retrofit2.HttpException

class PaginationPageWerehouse (
    private val retrofitService: RetrofitService,
): PagingSource<Int, WarehouseData>() {
    override fun getRefreshKey(state: PagingState<Int, WarehouseData>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, WarehouseData> {
        return try {
            val pageNumber = params.key ?: 1
            val pageSize = params.loadSize
            val response = retrofitService.warehouse(pageNumber)

            if (response.isSuccessful) {
                val warehouseData = response.body()!!
                val nextPageNumber = if (warehouseData.data.size < pageSize) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                return LoadResult.Page(warehouseData.data, prevPageNumber, nextPageNumber)
            } else {
                return LoadResult.Error(HttpException(response))
            }
        }catch (t: Throwable){
            LoadResult.Error(t)
        }
    }

}