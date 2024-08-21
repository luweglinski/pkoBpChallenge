package lw.pko.pkochallenge.movie.data.repository

import lw.pko.pkochallenge.movie.domain.model.Movie
import lw.pko.pkochallenge.movie.domain.repository.BasePagingSource
import lw.pko.pkochallenge.movie.domain.repository.MovieRepository
import lw.pko.pkochallenge.movie.domain.repository.SearchMoviePagingSource

class SearchMoviesPagingSourceRepository(
    private val movieRepository: MovieRepository
) : SearchMoviePagingSource {

    private var currentPagingSourceFactory: BasePagingSource<Movie>?=null

    override fun create(query: String): BasePagingSource<Movie> {
        return  object : BasePagingSource<Movie>(
            request = { pageNumber -> movieRepository.searchMovies(query, pageNumber).results }
        ) {}.also { currentPagingSourceFactory = it }
    }

    override fun invalidate() {
        currentPagingSourceFactory?.invalidate()
    }

}