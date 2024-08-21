package lw.pko.pkochallenge.movie.domain.repository

import lw.pko.pkochallenge.movie.domain.model.Movie

interface LatestMoviePagingSource {
    fun create(): BasePagingSource<Movie>
    fun invalidate()
}