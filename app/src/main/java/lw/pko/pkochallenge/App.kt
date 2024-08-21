package lw.pko.pkochallenge

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import lw.pko.pkochallenge.common.ImageLoaderProvider
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), ImageLoaderFactory {

    @Inject
    lateinit var imageLoaderProvider: ImageLoaderProvider

    override fun newImageLoader(): ImageLoader = imageLoaderProvider()
}