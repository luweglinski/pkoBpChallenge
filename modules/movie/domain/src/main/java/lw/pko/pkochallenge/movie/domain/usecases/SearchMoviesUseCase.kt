package lw.pko.pkochallenge.movie.domain.usecases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSourceFactory
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import lw.pko.pkochallenge.movie.domain.model.Movie
import lw.pko.pkochallenge.movie.domain.repository.SearchMoviePagingSource
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val searchMoviePagingSource: SearchMoviePagingSource
) {
    fun invoke(query: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = PagingSourceFactory { searchMoviePagingSource.create(query) }
        ).flow
    }

    fun invalidate() {
        searchMoviePagingSource.invalidate()
    }
}