package lw.pko.pkochallenge.movie.domain.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState

abstract class BasePagingSource<V : Any>(
    private val request: suspend (nextPage: Int) -> List<V>
) : PagingSource<Int, V>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, V> {
        val nextPage = params.key ?: 1
        return runCatching { request(nextPage) }
            .fold(
                onSuccess = { response ->
                    LoadResult.Page(
                        data = response,
                        prevKey = if (nextPage == 1) null else nextPage - 1,
                        nextKey = if (response.isEmpty()) null else nextPage + 1
                    )
                },
                onFailure = { e ->
                    LoadResult.Error(e)
                }
            )
    }

    override val jumpingSupported: Boolean
        get() = super.jumpingSupported

    override fun getRefreshKey(state: PagingState<Int, V>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let { closestPage ->
                closestPage.prevKey?.plus(1) ?: closestPage.nextKey?.minus(1)
            }
        }
    }
}