package lw.pko.pkochallenge.movie.domain.repository

import lw.pko.pkochallenge.movie.domain.model.Movie

interface SearchMoviePagingSource {
    fun create(query: String): BasePagingSource<Movie>
    fun invalidate()
}