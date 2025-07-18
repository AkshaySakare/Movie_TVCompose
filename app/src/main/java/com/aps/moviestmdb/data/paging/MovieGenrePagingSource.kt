package com.aps.moviestmdb.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

import com.aps.moviestmdb.data.remote.ApiService
import com.aps.moviestmdb.data.models.Movies

import kotlinx.coroutines.delay

class MovieGenrePagingSource(private val apiService: ApiService, private val tags: Int) :
    PagingSource<Int, Movies>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {
        return try {
            val nextPage = params.key ?: 1
            delay(3000L)
            val response = apiService.getGenreWiseMovieList(genresId = tags, page = nextPage)

            LoadResult.Page(
                data = response.results,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (response.page >= response.totalPages) null else response.page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movies>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
