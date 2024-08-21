package lw.pko.pkochallenge.movie.domain.usecases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSourceFactory
import kotlinx.coroutines.flow.Flow
import lw.pko.pkochallenge.movie.domain.model.Movie
import lw.pko.pkochallenge.movie.domain.repository.LatestMoviePagingSource
import javax.inject.Inject

class GetNowPlayingMoviesUseCase @Inject constructor(
    private val latestMoviePagingSource: LatestMoviePagingSource
) {
    fun invoke(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = PagingSourceFactory { latestMoviePagingSource.create() }
        ).flow
    }

    fun invalidate() {
        latestMoviePagingSource.invalidate()
    }

}