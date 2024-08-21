package lw.pko.pkochallenge.movie.domain.persistence

interface FavouriteMoviesRepository {
    fun add(movieId: Int)
    fun remove(movieId: Int)
    fun isFavourite(movieId: Int): Boolean
    fun getFavouriteMovies(): List<Int>
    fun toggleFavourite(id: Int,isFavourite:Boolean)
}