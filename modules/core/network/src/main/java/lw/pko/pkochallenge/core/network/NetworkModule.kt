package lw.pko.pkochallenge.core.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

const val BASE_URL = "BASE_URL"
const val TMDB_ACCESS_TOKEN = "TMDB_ACCESS_TOKEN"
const val AUTHORIZED_OKHTTP = "AUTHORIZED_OKHTTP"

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    @Named(AUTHORIZED_OKHTTP)
    fun provideOkHttpClient(authorizationInterceptor: AuthorizationInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authorizationInterceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private val json = Json { ignoreUnknownKeys = true }
    private val contentType = "application/json".toMediaType()

    @Singleton
    @Provides
    fun provideRetrofit(
        @Named(AUTHORIZED_OKHTTP)  okHttpClient: OkHttpClient,
        @Named(BASE_URL) baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory(contentType))
        .client(okHttpClient)
        .build()

}