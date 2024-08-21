package lw.pko.pkochallenge.movie.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import lw.pko.persistence.SharedPreferencesPersistence
import lw.pko.pkochallenge.movie.data.persistence.FavouriteMoviesLocalRepository
import lw.pko.pkochallenge.movie.data.repository.LatestMoviePagingSourceRepository
import lw.pko.pkochallenge.movie.data.repository.MovieRestRepository
import lw.pko.pkochallenge.movie.data.repository.SearchMoviesPagingSourceRepository
import lw.pko.pkochallenge.movie.domain.persistence.FavouriteMoviesRepository
import lw.pko.pkochallenge.movie.domain.repository.LatestMoviePagingSource
import lw.pko.pkochallenge.movie.domain.repository.MovieRepository
import lw.pko.pkochallenge.movie.domain.repository.SearchMoviePagingSource
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Named

private const val FAVOURITE_MOVIES_SHARED_PREFS_KEY = "favourite_movies_shared_prefs"

@Module
@InstallIn(SingletonComponent::class)
object MovieDataModule {

    @Provides
    fun provideMovieRepository(retrofit: Retrofit): MovieRepository = MovieRestRepository(retrofit.create())

    @Provides
    fun provideLatestMoviePagingSourceRepository(
        movieRepository: MovieRepository
    ): LatestMoviePagingSource = LatestMoviePagingSourceRepository(movieRepository)

    @Provides
    fun provideSearchMoviePagingSourceRepository(
        movieRepository: MovieRepository
    ): SearchMoviePagingSource = SearchMoviesPagingSourceRepository(movieRepository)

    @Provides
    @Named(FAVOURITE_MOVIES_SHARED_PREFS_KEY)
    fun provideSharedPreferencesPersistence(
        @ApplicationContext context: Context
    ): SharedPreferencesPersistence = SharedPreferencesPersistence(context, FAVOURITE_MOVIES_SHARED_PREFS_KEY)

    @Provides
    fun provideFavouriteMoviesPersistence(
        @Named(FAVOURITE_MOVIES_SHARED_PREFS_KEY) sharedPreferencesPersistence: SharedPreferencesPersistence
    ): FavouriteMoviesRepository = FavouriteMoviesLocalRepository(sharedPreferencesPersistence)

}