package lw.pko.pkochallenge.movie.presentation.dashboard

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class MovieViewEntity(
    val movies: List<MovieItemViewEntity>
) : Parcelable

@Immutable
@Parcelize
data class MovieItemViewEntity(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val isFavourite: Boolean,
    val releaseDate: String,
    val overview: String,
    val voteAverage: String
) : Parcelable{
    companion object {
        val EMPTY = MovieItemViewEntity(
            id = 0,
            title = "",
            imageUrl = "",
            isFavourite = false,
            releaseDate = "",
            overview = "",
            voteAverage = ""
        )
    }
}