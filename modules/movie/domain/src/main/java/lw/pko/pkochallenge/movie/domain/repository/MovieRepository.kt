package lw.pko.pkochallenge.movie.domain.repository

import lw.pko.pkochallenge.movie.domain.model.MovieResponse

interface MovieRepository {
    suspend fun getNowPlayingMovies(pageNumber: Int): MovieResponse
    suspend fun searchMovies(query: String, pageNumber: Int): MovieResponse
}