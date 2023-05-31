package com.zell.intermediatesubmission.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zell.intermediatesubmission.api.ApiServices
import com.zell.intermediatesubmission.response.ListStoryItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryPagingSource @Inject constructor(
    private val apiServices: ApiServices,
    private val token: String,
    private val location: Int?) : PagingSource<Int, ListStoryItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val size = params.loadSize
            val responseData = apiServices.getAllStory(token, page, size, location)
            val stories = responseData.listStory?.filterNotNull() ?: emptyList()
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (stories.isNotEmpty()) page + 1 else null
            LoadResult.Page(stories, prevKey, nextKey)

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}