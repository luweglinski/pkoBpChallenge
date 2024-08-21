package lw.pko.pkochallenge.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import lw.pko.pkochallenge.BuildConfig
import lw.pko.pkochallenge.common.DefaultImageLoaderProvider
import lw.pko.pkochallenge.common.ImageLoaderProvider
import lw.pko.pkochallenge.core.network.BASE_URL
import lw.pko.pkochallenge.core.network.TMDB_ACCESS_TOKEN
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context
    ): ImageLoaderProvider = DefaultImageLoaderProvider(context)

    @Provides
    @Singleton
    @Named(BASE_URL)
    fun provideBaseUrl(): String = BuildConfig.TMDB_URL

    @Provides
    @Singleton
    @Named(TMDB_ACCESS_TOKEN)
    fun providePixabayApiKey(): String = BuildConfig.TMDB_ACCESS_TOKEN

}