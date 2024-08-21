package lw.pko.pkochallenge.common

import android.content.Context
import android.net.Uri
import coil.ImageLoader
import coil.disk.DiskCache
import coil.intercept.Interceptor
import coil.memory.MemoryCache
import coil.request.ImageResult
import coil.size.pxOrElse

interface ImageLoaderProvider {
    operator fun invoke(): ImageLoader
}

class DefaultImageLoaderProvider(private val context: Context) : ImageLoaderProvider {

    override fun invoke(): ImageLoader {
        return ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .components {
                add(AddWidthParamInterceptor())
            }
            .crossfade(true)
            .build()
    }

    class AddWidthParamInterceptor : Interceptor {

        private val posterAvailableSizes = listOf(92, 154, 185, 342, 500, 780)

        override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
            val request = chain.request
            val width = chain.size.width.pxOrElse { 500 }
            val closestAvailableWidth = findClosestSize(width,posterAvailableSizes)
            val urlString = request.data as String

            val newUri = Uri.parse(urlString.replace("width", "w$closestAvailableWidth")).buildUpon().build()

            val newRequest =
                request.newBuilder()
                    .data(newUri)
                    .build()

            return chain.proceed(newRequest)
        }

        private fun findClosestSize(targetSize: Int, availableSizes: List<Int>): Int {
            return availableSizes.minByOrNull { kotlin.math.abs(it - targetSize) } ?: targetSize
        }
    }


}