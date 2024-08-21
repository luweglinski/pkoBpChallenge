package lw.pko.pkochallenge.movie.data.repository

import lw.pko.pkochallenge.movie.data.services.MovieService
import lw.pko.pkochallenge.movie.domain.model.MovieResponse
import lw.pko.pkochallenge.movie.domain.repository.MovieRepository

class MovieRestRepository(
    private val movieService: MovieService
) : MovieRepository {

    override suspend fun getNowPlayingMovies(pageNumber: Int): MovieResponse {
        return movieService.getNowPlayingMovies(pageNumber)
    }

    override suspend fun searchMovies(query: String, pageNumber: Int): MovieResponse {
        return movieService.searchMovies(query, pageNumber)
    }

}