package lw.pko.pkochallenge.movie.presentation.dashboard

import lw.pko.pkochallenge.movie.domain.model.Movie
import javax.inject.Inject

class MovieItemViewEntityMapper @Inject constructor() {

    fun map(movie: Movie, isFavourite: Boolean): MovieItemViewEntity {
        return MovieItemViewEntity(
            id = movie.id,
            title = movie.title.orEmpty(),
            imageUrl = movie.posterPath?.let { posterPath -> "https://image.tmdb.org/t/p/width/${posterPath.removePrefix("/")}" }.orEmpty(),
            isFavourite = isFavourite,
            releaseDate = movie.releaseDate.orEmpty(),
            overview = movie.overview.orEmpty(),
            voteAverage = movie.voteAverage.toString()
        )
    }
}