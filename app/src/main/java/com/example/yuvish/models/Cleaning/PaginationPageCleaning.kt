package com.example.yuvish.models.Cleaning

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.yuvish.retrofit.RetrofitService
import retrofit2.HttpException

class PaginationPageCleaning (
    private val retrofitService: RetrofitService,
    private val status: String
    ):PagingSource<Int, CleaningData>() {
    override fun getRefreshKey(state: PagingState<Int, CleaningData>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CleaningData> {
        return try {
            val pageNumber = params.key ?: 1
            val pageSize = params.loadSize
            val response = retrofitService.cleaning(status, pageNumber)

            if (response.code() == 200) {
                val cleaningData = response.body()!!
                val nextPageNumber = if (cleaningData.data.size < pageSize) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                return LoadResult.Page(cleaningData.data, prevPageNumber, nextPageNumber)
            } else {
                return LoadResult.Error(HttpException(response))
            }
        }catch (t: Throwable){
            t.printStackTrace()
            LoadResult.Error(t)
        }
    }

}