package lw.pko.pkochallenge.movie.data.persistence

import lw.pko.persistence.SharedPreferencesPersistence
import lw.pko.pkochallenge.movie.domain.persistence.FavouriteMoviesRepository

private const val FAVOURITE_MOVIES_KEY = "favourite_movies"

class FavouriteMoviesLocalRepository(private val sharedPreferencesPersistence: SharedPreferencesPersistence) : FavouriteMoviesRepository {

    override fun add(movieId: Int) {
        getFavouriteMovies().toMutableList().apply {
            add(movieId)
            sharedPreferencesPersistence.putList(FAVOURITE_MOVIES_KEY, this)
        }
    }

    override fun remove(movieId: Int) {
        getFavouriteMovies().toMutableList().apply {
            remove(movieId)
            sharedPreferencesPersistence.putList(FAVOURITE_MOVIES_KEY, this)
        }
    }

    override fun isFavourite(movieId: Int): Boolean {
        return getFavouriteMovies().contains(movieId)
    }

    override fun getFavouriteMovies(): List<Int> {
        return sharedPreferencesPersistence.getList(FAVOURITE_MOVIES_KEY, Int::class)
    }

    override fun toggleFavourite(id: Int, isFavourite:Boolean) {
        if (isFavourite) {
            remove(id)
        } else {
            add(id)
        }
    }

}