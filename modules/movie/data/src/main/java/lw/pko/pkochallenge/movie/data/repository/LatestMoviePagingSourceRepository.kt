package lw.pko.pkochallenge.movie.data.repository

import lw.pko.pkochallenge.movie.domain.model.Movie
import lw.pko.pkochallenge.movie.domain.repository.BasePagingSource
import lw.pko.pkochallenge.movie.domain.repository.LatestMoviePagingSource
import lw.pko.pkochallenge.movie.domain.repository.MovieRepository

class LatestMoviePagingSourceRepository(private val movieRepository: MovieRepository) : LatestMoviePagingSource {

    private var currentPagingSourceFactory: BasePagingSource<Movie>? = null

    override fun create(): BasePagingSource<Movie> {
        return object : BasePagingSource<Movie>(
            request = { pageNumber -> movieRepository.getNowPlayingMovies(pageNumber).results }
        ) {}.also { currentPagingSourceFactory = it }
    }

    override fun invalidate() {
        currentPagingSourceFactory?.invalidate()
    }
}