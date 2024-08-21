package lw.pko.pkochallenge.movie.data.services

import lw.pko.pkochallenge.movie.domain.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int,
        @Query("language") language: String = "en-US"
    ): MovieResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("language") language: String = "en-US"
    ): MovieResponse
}